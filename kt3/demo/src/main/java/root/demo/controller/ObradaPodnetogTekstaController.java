package root.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.mapper.MultipartFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.demo.Dto.*;
import root.demo.config.MyConfig;
import root.demo.model.*;
import root.demo.repository.NaucnaOblastRepository;
import root.demo.repository.RadRepository;
import root.demo.services.others.*;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;

import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;

@Controller
@RequestMapping("/obradateksta")
public class ObradaPodnetogTekstaController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessHelperService processHelperService;

    @Autowired
    private UserDbService userDbService;

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    private RadService radService;

    @Autowired
    private FileService fileService;

    @Autowired
    RadRepository radRepository;

    @Autowired
    AuthorizationService authorizationService;

    private String processName = "add_new_article_ver1";

    //AUTORIZACIJA PROCESA

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/authorize/process/newarticle", produces = "application/json")
    public @ResponseBody ResponseEntity<?> authorizeProcess() {
    //public void authorizeProcess(){
        //NAPRAVI GRUPU AUTORA
        String autoriGrupaId = MyConfig.groupAutor;
        Group autoriGroup = identityService.newGroup(autoriGrupaId);
        autoriGroup.setName("Authors");
        autoriGroup.setType("WORKFLOW");
        identityService.saveGroup(autoriGroup);

        //OMOGUCI DA PROCES MOGU DA POKRENU SAMO CLANOVI IZ GRUPE AUTORI
        Authorization authProcessDefinition = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
        authProcessDefinition.setGroupId(autoriGrupaId);
        authProcessDefinition.setResource(Resources.PROCESS_DEFINITION);
        authProcessDefinition.setResourceId(processName);
        authProcessDefinition.addPermission(Permissions.CREATE_INSTANCE);
        authorizationService.saveAuthorization(authProcessDefinition);

        Authorization authProcessInstance = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
        authProcessInstance.setGroupId(autoriGrupaId);
        authProcessInstance.setResource(Resources.PROCESS_INSTANCE);
        authProcessInstance.setResourceId("*");
        authProcessInstance.addPermission(Permissions.CREATE);
        authorizationService.saveAuthorization(authProcessInstance);

        //SVAKOG KORISNIKA SA ROLOM AUTOR UBACI U GRUPU AUTORA

        List<UserDb> autori = userDbService.findByRole(MyConfig.roleAutor);
        for (UserDb user : autori) {
            identityService.createMembership(user.getUsername(), autoriGrupaId);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //POCINJE PROCES OBRADE TEKSTA, VRACA SVA POLJA ZA FORMU ZA IZBOR CASOPISA
    @PreAuthorize("hasRole('AUTOR')")
    @GetMapping(path = "/start/{username}", produces = "application/json")
    public @ResponseBody FormFieldsDto startProcess(@PathVariable String username) {

        identityService.clearAuthentication();
        //authorizeProcess();
        identityService.setAuthenticatedUserId(username);

        ProcessInstance pi = null;
        try {
            pi = runtimeService.startProcessInstanceByKey(processName);
        }catch (AuthenticationException e){
            System.out.println(e.getMessage());
            return null;
        }
        System.out.println("PROCES OBRADE PODNETOG TEKSTA JE POKRENUT");
        System.out.println("--Username :" + identityService.getCurrentAuthentication().getUserId());

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        UserDb userDb = userDbService.findByUsername(username);
        if(userDb != null) {
            runtimeService.setVariable(pi.getId(),"starterIdVariable",username);

            List<NaucnaOblast> oblasti = naucnaOblastRepository.findAll();
            ObjectValue customOblasti = Variables.objectValue(oblasti)
                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                    .create();
            runtimeService.setVariable(pi.getId(),"oblasti",customOblasti);
        }
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA IZBOR CASOPISA
    @PreAuthorize("hasRole('AUTOR')")
    @PostMapping(path = "/post/pickmagazine/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA OBRADU PODNETOG TEKSTA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);
        String sellime =(String) map.get("selImeCasopisa");

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId,"selImeCasopisa",sellime);
        runtimeService.setVariable(processInstanceId,"tip_placanja",casopisService.getByNaziv(sellime).getKomeSePlaca());

        map = new HashMap<>();
        formService.submitTaskForm(taskId, map);

        boolean aktivna_clanarina = (boolean) runtimeService.getVariable(processInstanceId,"aktivna_clanarina");
        String tip_placanja = (String) runtimeService.getVariable(processInstanceId,"tip_placanja");
        String json = "{";
        json += " \"aktivna_clanarina\" : \"" + aktivna_clanarina + "\"";
        json += ",";
        json += " \"tip_placanja\" : \"" + tip_placanja + "\"";
        json += "}";
        return new ResponseEntity<>(json, HttpStatus.OK);

        //ValidationResults results = new ModelInstanceValidator(this, validators).validate();
        /*
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", dto);
        formService.submitTaskForm(taskId, map);

        if(runtimeService.getVariable(processInstanceId,"validacija").equals(false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
        */
    }

    //VRACANJE STATUSA PLACANJA CLANARINE
    @PreAuthorize("hasRole('AUTOR')")
    @PostMapping(path = "/post/status/{username}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> postStatus(@RequestBody String status, @PathVariable String username) {
        ResponseEntity re = processHelperService.getActiveTasks(username, processName);
        List<TaskDto> tasks = (List<TaskDto>) re.getBody();
        TaskDto activeTask = tasks.get(0);

        Task task = taskService.createTaskQuery().taskId(activeTask.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        if(status.toLowerCase().equals("success") == true) {
            runtimeService.setVariable(processInstanceId, "aktivna_clanarina", true);

            String imeCasopisa = (String) runtimeService.getVariable(processInstanceId,"selImeCasopisa");
            casopisService.addKorisnikSaAktivnomClanarinom(imeCasopisa,username);
        }

        HashMap<String, Object> map = new HashMap<>();
        formService.submitTaskForm(activeTask.getTaskId(), map);
        String json = "{ \"id\" : \"" + processInstanceId + "\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    //VRACA SVA POLJA ZA FORMU ZA DODAVANJE RADA
    @PreAuthorize("hasRole('AUTOR')")
    @GetMapping(path = "/get/podacioraduform/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTask(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA DODAVANJE RADA
    @PreAuthorize("hasRole('AUTOR')")
    @PostMapping(path = "/post/podacioraduform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormData(@RequestParam("file") MultipartFile file, @RequestParam("data") String data, @PathVariable String taskId) {
        System.out.println("FORMA ZA DODAVANJE RADA JE POSLATA");

        List<FormSubmissionDto> dto = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(data);
            for(Object o : actualObj){
                FormSubmissionDto fsd =  mapper.convertValue(o, FormSubmissionDto.class);
                dto.add(fsd);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String autor = (String) runtimeService.getVariable(processInstanceId,"starterIdVariable");

        Rad rad = new Rad();
        for(FormSubmissionDto obj : dto){
            if(obj.getFieldId().equals("naslov_rada")){
                rad.setNaslov((String) obj.getFieldValue());
            }else if(obj.getFieldId().equals("kljucni_pojmovi_rada")){
                rad.setKljucniPojmovi((String) obj.getFieldValue());
            }else if(obj.getFieldId().equals("apstrakt_rada")){
                rad.setApstrakt((String) obj.getFieldValue());
            }else if(obj.getFieldId().equals("naucna_oblast_rada")){
                rad.setNaucnaOblast((String) obj.getFieldValue());
            }else if(obj.getFieldId().equals("koautori")){
                Object temp = obj.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                List<KoAutor> koAutori = mapper.convertValue(temp, new TypeReference<List<KoAutor>>() { });
                rad.setKoautori(koAutori);
            }
        }

        Rad retVal = radService.addRad(rad,file, autor);
        if(retVal.getId() != null) {
            formService.submitTaskForm(task.getId(), map);
            return new ResponseEntity<>(retVal, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA AKTIVNE TASKOVE PO IMENU TASKA
    @PreAuthorize("hasRole('UREDNIK') or hasRole('AUTOR') or hasRole('RECENZENT')")
    @GetMapping(path = "/get/tasks/active/{username}/{taskName}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>>
    getActiveProcessByTaskNameForUser(@PathVariable String username, @PathVariable String taskName) {
        return processHelperService.getActiveTasksByName(username, processName, taskName);
    }

    //VRACA AKTIVNE TASKOVE PO IMENU TASKA
    @PreAuthorize("hasRole('UREDNIK') or hasRole('AUTOR') or hasRole('RECENZENT')")
    @GetMapping(path = "/get/tasks/multiple/active/{username}/{taskName}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>>
    getMultipleActiveProcessByTaskNameForUser(@PathVariable String username, @PathVariable String taskName) {
        return processHelperService.getMultipleActiveTasksByName(username, processName, taskName);
    }

    //VRACA SVA POLJA ZA FORMU ZA PREGLED RADA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/pregledanjeRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForPregledRadova(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA PREGLED RADA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/pregledanjeRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormData(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA PREGLED RADOVA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            boolean relevantan = (boolean) map.get("relevantan");
            formService.submitTaskForm(taskId, map);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA PREGLED RADA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/pregledanjePdfaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForPregledanjePdfa(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACA PDF FAJL
    @PreAuthorize("hasRole('UREDNIK') or hasRole('AUTOR') or hasRole('RECENZENT')")
    @ResponseBody
    @RequestMapping(value = "/get/file/{processInstance}", headers="Accept=*/*", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String processInstance) throws FileNotFoundException {
        // Get the remove file based on the fileaddress
        String naslovRada = (String) runtimeService.getVariable(processInstance,"naslov_rada");
        File pdfFile = fileService.getFile(naslovRada);
        // Set the input stream
        FileInputStream inputStream = new FileInputStream(pdfFile);
        // asume that it was a PDF file
        HttpHeaders responseHeaders = new HttpHeaders();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        responseHeaders.setContentLength(pdfFile.length());
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        // just in case you need to support browsers
        String val = "attachment; filename=" + pdfFile.getName();
        responseHeaders.put("Content-Disposition", Collections.singletonList(val));
        return new ResponseEntity<InputStreamResource> (inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    //VRACAJU SE PODACI SA FORME ZA PREGLED RADA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/pregledanjePdfaForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataPdf(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA PREGLED PDFOVA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            boolean formatiran = (boolean) map.get("formatiran");
            Integer timeInSec = (Integer) map.get("vreme_za_korekciju");
            if(timeInSec >= 0) {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA KOREKCIJU RADA
    @PreAuthorize("hasRole('AUTOR')")
    @GetMapping(path = "/get/korekcijaRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForKorekcijeRadova(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA IZMENU RADA
    @PreAuthorize("hasRole('AUTOR')")
    @PostMapping(path = "/post/korekcijaRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataIzmenaRada(@RequestParam("file") MultipartFile file, @RequestParam("data") String data, @PathVariable String taskId) {
        System.out.println("FORMA ZA KOREKCIJU RADA JE POSLATA");

        List<FormSubmissionDto> dto = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(data);
            for(Object o : actualObj){
                FormSubmissionDto fsd =  mapper.convertValue(o, FormSubmissionDto.class);
                dto.add(fsd);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String autor = (String) runtimeService.getVariable(processInstanceId,"starterIdVariable");

        Rad rad = new Rad();
        for(FormSubmissionDto obj : dto){
            if(obj.getFieldId().equals("naslov_rada")) {
                rad.setNaslov((String) obj.getFieldValue());
            }
        }

        Rad retVal = radService.editRad(rad,file);
        if(retVal != null) {
            try{
                map.remove("komentari_za_autora");
            }catch(Exception e){ }

            formService.submitTaskForm(task.getId(), map);
            return new ResponseEntity<>(retVal, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA IZBOR RECENZENATA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/izborRecenzenataForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto izborRecenzenataForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA IZBOR RECENZENATA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/izborRecenzenataForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataIzborRecenzenata(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA IZBOR RECENZENATA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            Object obj = map.get("recenzenti");
            ObjectMapper mapper = new ObjectMapper();
            List<String> recenzenti = mapper.convertValue(obj, new TypeReference<List<String>>() {});
            List<String> lista_izabranih_recenzenata = new ArrayList<>();
            List<String> allRec = new ArrayList<>();

            if(recenzenti == null || recenzenti.size() == 0){
                String nazivCasopisa = (String) runtimeService.getVariable(processInstanceId, "selImeCasopisa");
                CasopisDto casopis = casopisService.getByNaziv(nazivCasopisa);
                lista_izabranih_recenzenata.add(casopis.getUrednici().get(0).getUsername());  //glavni urednik obavlja ulogu recenzije, kao nema recenzenata
                allRec.add(casopis.getUrednici().get(0).getUsername());
            }else{
                for(String username : recenzenti){
                    //System.out.println("--Recenzija rada task kreiran za " + username);
                    lista_izabranih_recenzenata.add(username);    //username recenzenata se dodaje u listu
                }

                //svi recenzenti iz casopisa se postavljaju u listu
                String nazivCasopisa = (String) runtimeService.getVariable(processInstanceId, "selImeCasopisa");
                List<UserDto> recenzentiDb = casopisService.getByNaziv(nazivCasopisa).getRecenzenti();

                for(UserDto usr : recenzentiDb){
                    allRec.add(usr.getUsername());
                }
            }

            //map.put("recenzenti", allRec);
            map.remove("recenzenti");

            ObjectValue svi_rec = Variables.objectValue(allRec)
                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                    .create();
            runtimeService.setVariable(processInstanceId,"recenzenti",svi_rec);

            ObjectValue izabrani_rec = Variables.objectValue(lista_izabranih_recenzenata)
                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                    .create();
            runtimeService.setVariable(processInstanceId,"lista_izabranih_recenzenata",izabrani_rec);

            String naucnaOblast = (String) map.get("naucna_oblast_rada");
            Integer timeInSec = (Integer) map.get("vreme_za_rec");
            if(timeInSec >= 0 && naucnaOblast != "") {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA RECENZIJU RADOVA
    @PreAuthorize("hasRole('RECENZENT')")
    @GetMapping(path = "/get/recenzijaRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto recenzijaRadovaForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA RECENZIJU RADOVA
    @PreAuthorize("hasRole('RECENZENT')")
    @PostMapping(path = "/post/recenzijaRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataRecenzijaRadovaForm(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA RECENZIJU RADOVA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            String preporuka = (String) map.get("preporuka");
            String komentarZaAutora = (String) map.get("komentar_za_autora");
            String komentarZaUrednika = (String) map.get("komentar_za_urednika");
            String naslovRada = (String) map.get("naslov_rada");
            if(preporuka != "" && komentarZaAutora != "" && komentarZaUrednika != "" && naslovRada != "") {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA IZBOR NOVOG RECENZENTA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/izborNovogRecenzenta/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto izborNovogRecenzentaForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME IZBOR NOVOG RECENZENTA
    @PreAuthorize("hasRole('RECENZENT')")
    @PostMapping(path = "/post/izborNovogRecenzenta/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataIzborNovogRecenzentaForm(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA IZBOR NOVOG RECENZENTA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            String noviRecenzent = (String) map.get("novi_recenzent");
            Integer timeInSec = (Integer) map.get("vreme_za_rec");
            if(timeInSec >= 0 && noviRecenzent != "") {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA CITANJE KOMENTARA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/citanjeKomentaraForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto citanjeKomentaraForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //MORA OVAKO NESTO GROOVY ZEZAO...
        String processInstanceId = task.getProcessInstanceId();

        List<PrimitiveValue<String>> komm = (List<PrimitiveValue<String>>) runtimeService.getVariable(processInstanceId,"lista_komentara_za_urednika");
        List<PrimitiveValue<String>> preporuke = (List<PrimitiveValue<String>>) runtimeService.getVariable(processInstanceId,"lista_preporuka");

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> formFields = tfd.getFormFields();
        if(formFields != null){
            for(FormField field : formFields){
                if(field.getId().equals("komentari_za_urednika")){
                    Map<String, String> values = (Map<String, String>) field.getType().getInformation("values");
                    values.clear();
                    for(PrimitiveValue<String> s : komm){
                        values.put(s.getValue(), s.getValue());
                    }
                }else if(field.getId().equals("preporuke")){
                    Map<String, String> values = (Map<String, String>) field.getType().getInformation("values");
                    values.clear();
                    for(PrimitiveValue<String> s : preporuke){
                        values.put(s.getValue(), s.getValue());
                    }
                }
            }
        }

        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA CITANJE KOMENTARA
    @PreAuthorize("hasRole('RECENZENT')")
    @PostMapping(path = "/post/citanjeKomentaraForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataCitanjeKomentaraForm(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA CITANJE KOMENTARA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            String konacnaOdluka = (String) map.get("konacna_odluka");
            Integer timeInSec = (Integer) map.get("vreme_za_ispravku");

            map.remove("komentari_za_urednika");
            map.remove("preporuke");

            if(timeInSec >= 0 && konacnaOdluka != "") {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA ISPRAVKU RADOVA
    @PreAuthorize("hasRole('AUTOR')")
    @GetMapping(path = "/get/ispravkaRadovaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto ispravkaRadovaForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //MORA OVAKO NESTO GROOVY ZEZAO...
        String processInstanceId = task.getProcessInstanceId();

        List<PrimitiveValue<String>> komm = (List<PrimitiveValue<String>>) runtimeService.getVariable(processInstanceId,"lista_komentara_za_autora");

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> formFields = tfd.getFormFields();
        if(formFields != null){
            for(FormField field : formFields){
                if(field.getId().equals("komentari_za_autora")){
                    Map<String, String> values = (Map<String, String>) field.getType().getInformation("values");
                    values.clear();
                    for(PrimitiveValue<String> s : komm){
                        values.put(s.getValue(), s.getValue());
                    }
                }
            }
        }

        return processHelperService.createFormFieldsDto(task);
    }

    //VRACA SVA POLJA ZA FORMU ZA PREGLED IZMENA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/pregledIzmenaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto pregledIzmenaForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //MORA OVAKO NESTO GROOVY ZEZAO...
        String processInstanceId = task.getProcessInstanceId();

        List<PrimitiveValue<String>> komm = (List<PrimitiveValue<String>>) runtimeService.getVariable(processInstanceId,"lista_komentara_za_autora");

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> formFields = tfd.getFormFields();
        if(formFields != null){
            for(FormField field : formFields){
                if(field.getId().equals("komentari_koje_je_dob_autor")){
                    Map<String, String> values = (Map<String, String>) field.getType().getInformation("values");
                    values.clear();
                    for(PrimitiveValue<String> s : komm){
                        values.put(s.getValue(), s.getValue());
                    }
                }
            }
        }

        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA PREGLED IZMENA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/pregledIzmenaForm/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataPregledIzmenaForm(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA PREGLED IZMENA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        try {
            String konacnaOdluka = (String) map.get("odluka_urednika");
            map.remove("komentari_koje_je_dob_autor");

            if(konacnaOdluka != "") {
                formService.submitTaskForm(taskId, map);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
