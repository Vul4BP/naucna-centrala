package root.demo.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import root.demo.Dto.CasopisDto;
import root.demo.model.NaucnaOblast;
import root.demo.repository.NaucnaOblastRepository;
import root.demo.services.others.CasopisService;

import java.util.List;

@Controller
@RequestMapping("/casopis")
public class CasopisController {

    @Autowired
    CasopisService casopisService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping(path = "/get/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<CasopisDto> getByTaskId(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String name = (String) runtimeService.getVariable(processInstanceId, "naziv");
        CasopisDto casopisDto = casopisService.getByNaziv(name);
        if(casopisDto == null){
            return new ResponseEntity(new CasopisDto(),  HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(casopisDto,  HttpStatus.OK);
    }

}
