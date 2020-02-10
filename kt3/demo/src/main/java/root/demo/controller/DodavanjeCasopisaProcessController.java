package root.demo.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import root.demo.Dto.FormFieldsDto;
import root.demo.Dto.FormSubmissionDto;
import root.demo.Dto.TaskDto;
import root.demo.services.others.ProcessHelperService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Kontroler za proces dodavanja casopisa

@Controller
@RequestMapping("/dodavanjecasopisa")
public class DodavanjeCasopisaProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private ProcessHelperService processHelperService;

    private String processName = "add_new_magazine_process_1";

    //POCINJE PROCES DODAVANJA CASOPISA, VRACA SVA POLJA ZA FORMU ZA DODAVANJE CASOPISA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/start/{username}", produces = "application/json")
    public @ResponseBody FormFieldsDto startReg(@PathVariable String username) {
        System.out.println("PROCES DODAVANJA CASOPISA JE POKRENUT");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processName);
        runtimeService.setVariable(pi.getId(),"username",username);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA DODAVANJE CASOPISA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/casopisform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA DODAVANJE CASOPISA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "casopis", dto);
        formService.submitTaskForm(taskId, map);

        if(runtimeService.getVariable(processInstanceId,"validacija1").equals(false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //VRACA UREDNIKU SVE TASKOVE ZA DODAVANJE UREDNIKA I RECENZENATA, I VRACA ADMINU SVE TASKOVE ZA PREGLED CASOPISA
    @PreAuthorize("hasRole('UREDNIK') or hasRole('ADMIN')")
    @GetMapping(path = "/get/tasks/active/{username}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> getActiveProcessTasksForUser(@PathVariable String username) {
        return processHelperService.getActiveTasks(username, processName);
    }

    //VRACA AKTIVNE TASKOVE PO IMENU TASKA
    @PreAuthorize("hasRole('UREDNIK') or hasRole('ADMIN')")
    @GetMapping(path = "/get/tasks/active/{username}/{taskName}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>>
    getActiveProcessByTaskNameForUser(@PathVariable String username, @PathVariable String taskName) {
        return processHelperService.getActiveTasksByName(username, processName, taskName);
    }

    //VRACA SVA POLJA ZA FORMU ZA DODAVANJE UREDNIKA I RECENZENATA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/dodavanjeUrednikaIRecenzenataform/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForRecenzenti(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA DODAVANJE UREDNIKA I RECENZENATA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/dodavanjeUrednikaIRecenzenataform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormData(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA DODAVANJE UREDNIKA I RECENZENATA JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "uredniciIRecenzenti", dto);
        formService.submitTaskForm(taskId, map);

        if(runtimeService.getVariable(processInstanceId,"validacija2").equals(false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //VRACA SVA POLJA ZA FORMU ZA POTVRDU CASOPISA
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/get/pregledcasopisaform/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForPregledCasopisa(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA POTVRDU CASOPISA
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/post/pregledcasopisaform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postPregledCasopisa(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            formService.submitTaskForm(taskId, map);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //VRACA SVA POLJA ZA FORMU ZA DODAVANJE CASOPISA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/dodavanjecasopisa/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskDodavanjeCasopisa(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACA SVA POLJA ZA FORMU ZA POTVRDU MIKROSERVISA
    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/get/potvrdamikroservisa/{taskId}", produces = "application/json")
    public @ResponseBody List<FormSubmissionDto> getTaskPotvrdaMikroservisa(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processId = task.getProcessInstanceId();
        FormFieldsDto ffd = processHelperService.createFormFieldsDto(task);
        return processHelperService.getMicroservices(processId);
    }

    /*
    //OVO SLUZI DA SE POTVRDI MIKROSERVIS
    @GetMapping(path = "/{nacinplacanja}/confirm/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> confirmNacinPlacanja(@PathVariable String nacinplacanja, @PathVariable String processInstanceId) {
        runtimeService.setVariable(processInstanceId,nacinplacanja,true);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/

    //VRACAJU SE PODACI SA FORME ZA DODAVANJE UREDNIKA I RECENZENATA
    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/post/potvrdamikroservisa/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postFormDataPotvrdaMikroservisa(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA POTVRDU MIKROSERVISA JE DODATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        if(processHelperService.getMicroservices(processInstanceId).size() > 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TEST CERT
    @GetMapping(path = "/getjson", produces = "application/json")
    public ResponseEntity<?> GetJson(){
        String s = "\"res\" : \"ok\"";
        return new ResponseEntity<>(s,HttpStatus.OK);
    }


}
