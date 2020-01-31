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
import root.demo.model.Casopis;
import root.demo.model.NacinPlacanja;
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
    public @ResponseBody ResponseEntity<CasopisDto> getMagazine(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String name = (String) runtimeService.getVariable(processInstanceId, "naziv");
        CasopisDto casopisDto = casopisService.getByNaziv(name);
        if(casopisDto == null){
            return new ResponseEntity(new CasopisDto(),  HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(casopisDto,  HttpStatus.OK);
    }

    @GetMapping(path = "/get/all", produces = "application/json")
    public @ResponseBody ResponseEntity<List<CasopisDto>> getAllMagazines() {
        List<CasopisDto> casopisiDto = casopisService.getAllMagazines();
        return new ResponseEntity(casopisiDto,  HttpStatus.OK);
    }

    @GetMapping(path = "/kupi/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> kupiCasopis(@PathVariable Long id) {
        Casopis casopis = casopisService.getById(id);
        if(casopis == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String redirectUrl = "https://localhost:5000/magazine?id=" + casopis.getId();
        //return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();

        String body = "{ \"redirectUrl\" : \"" + redirectUrl + "\" }";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping(path = "/subscribe/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> subscribeCasopis(@PathVariable Long id) {
        Casopis casopis = casopisService.getById(id);
        if(casopis == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else{
            boolean indikator = false;
            List<NacinPlacanja> nacinPlacanja = casopis.getNaciniplacanja();
            for(NacinPlacanja nc : nacinPlacanja){
                if(nc.getName().toLowerCase().equals("paypal")){
                    indikator = true;
                }
            }

            if(indikator == false){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }

        //treba se obratiti KP da se dobije url od pp fronta
        String redirectUrl = "https://localhost:5003/subscription/" + casopis.getId();
        //return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();

        String body = "{ \"redirectUrl\" : \"" + redirectUrl + "\" }";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
