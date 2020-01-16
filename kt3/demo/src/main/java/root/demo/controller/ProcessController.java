package root.demo.controller;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import root.demo.Dto.TaskDto;
import root.demo.services.others.ProcessHelperService;

import java.util.List;


//Zajednicki kontroler za sve procese

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHelperService processHelperService;

    //VRACA SVE TRENUTNE TASKOVE U PROCESU
    @GetMapping(path = "/tasks/get/{processInstanceId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<TaskDto>> getTasks(@PathVariable String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return new ResponseEntity(processHelperService.createListOfTaskDtos(tasks),  HttpStatus.OK);
    }

    //OMOGUCAVA CLAIMOVANJE TASKA
    @PostMapping(path = "/tasks/claim/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity claim(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String user = (String) runtimeService.getVariable(processInstanceId, "username");
        //System.out.println(user);
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //OMOGUCAVA COMPLEETOVANJE TASKA
    @PostMapping(path = "/tasks/complete/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> complete(@PathVariable String taskId) {
        Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.complete(taskId);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskTemp.getProcessInstanceId()).list();
        return new ResponseEntity<List<TaskDto>>(processHelperService.createListOfTaskDtos(tasks), HttpStatus.OK);
    }

}