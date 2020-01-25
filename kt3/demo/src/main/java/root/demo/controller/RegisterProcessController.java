package root.demo.controller;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
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
import root.demo.model.*;
import root.demo.services.others.ProcessHelperService;
import root.demo.services.others.UserDbService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Kontroler za proces registracije

@Controller
@RequestMapping("/registration")
public class RegisterProcessController {

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

    private String processName = "reg_process";

    //POCINJE PROCES REGISTRACIJE, VRACA SVA POLJA ZA FORMU ZA REGISTRACIJU
    @GetMapping(path = "/start", produces = "application/json")
    public @ResponseBody FormFieldsDto startReg() {
        System.out.println("PROCES REGISTRACIJE JE POKRENUT");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processName);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA REGISTRACIJU
    @PostMapping(path = "/post/registerform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        System.out.println("FORMA ZA REGISTROVANJE JE POSLATA");
        HashMap<String, Object> map = processHelperService.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", dto);
        formService.submitTaskForm(taskId, map);

        if(runtimeService.getVariable(processInstanceId,"validacija").equals(false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //OVAJ ENDPOINT SE POGADJA IZ EMAILA
    @GetMapping(path = "/confirm/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> confirmEmail(@PathVariable String processInstanceId) {
        //System.out.println("ProcessInstanceId: \t" + processInstanceId);

        try {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
            formService.submitTaskForm(task.getId(), null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<String>("Invalid link.", HttpStatus.BAD_REQUEST);
        }

        String redirectUrl = "https://localhost:5004/regresult/" + processInstanceId;
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
    }

    //VRACA STATUS REGISTRACIJE KORISNIKU
    @GetMapping(path = "/get/status/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> checkRegisterStatus(@PathVariable String processInstanceId) {
        //System.out.println("ProcessInstanceId: \t" + processInstanceId);

        boolean status = false;
        boolean recenzent = false;
        UserDb userDb = null;
        String message = "";
        String body = "";

        try {
            //provera da li je proces zavrsen
            List<HistoricProcessInstance> historyProc = historyService.createHistoricProcessInstanceQuery().completed().list();
            for (HistoricProcessInstance process : historyProc) {
                if(process.getId().equals(processInstanceId)){
                    message = "Registracija je uspesno izvrsena";
                    body = "{ \"res\" : \"" + message + "\" }";
                    return new ResponseEntity<String>(body, HttpStatus.OK);
                }
            }

            //ako nije uzimamo userDb-a
            String username = (String) runtimeService.getVariable(processInstanceId, "username");
            recenzent = (boolean) runtimeService.getVariable(processInstanceId, "recenzent");

            userDb = userDbService.findByUsername(username);
            status = userDb.isEnabled();
        }catch (Exception e){
            System.out.println(e.getMessage());
            body = "{ \"res\" : \"" + "Some error occurred" + "\" }";
            return new ResponseEntity<String>(body, HttpStatus.BAD_REQUEST);
        }

        if(status == false){
            message = "Morate da potvrdite registraciju. Link je poslat na " + userDb.getEmail();
        }else if(status == true && recenzent == true){
            message = "Registracija je uspesno izvrsena. ";
            message += "Da bi ste postali recenzent morate sacekati da admin odobri vas zahtev. ";
            message += "Ako admin odbije vas zahtev, imate ulogu obicnog korisnika";
        }

        body = "{ \"res\" : \"" + message + "\" }";
        return new ResponseEntity<String>(body, HttpStatus.OK);
    }

    //VRACA ADMINU SVE TASKOVE ZA POTVRDU RECENZENATA
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/get/tasks/active/{username}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> getActiveProcessTasksForUser(@PathVariable String username) {
        return processHelperService.getActiveTasks(username, processName);
    }

    //VRACA SVA POLJA ZA FORMU ZA POTVRDU RECENZENTA
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/get/recenzentiform/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getTaskForRecenzenti(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processHelperService.createFormFieldsDto(task);
    }

    //VRACAJU SE PODACI SA FORME ZA POTVRDU RECENZENTA
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/post/recenzentiform/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postRecenzenti(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
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

}