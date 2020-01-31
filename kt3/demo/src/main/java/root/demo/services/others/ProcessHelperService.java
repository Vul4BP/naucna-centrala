package root.demo.services.others;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import root.demo.Dto.FormFieldsDto;
import root.demo.Dto.FormSubmissionDto;
import root.demo.Dto.TaskDto;
import root.demo.model.Casopis;
import root.demo.model.NacinPlacanja;
import root.demo.repository.CasopisRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProcessHelperService {

    @Autowired
    private CasopisRepository casopisRepository;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

    public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    public FormFieldsDto createFormFieldsDto(Task task){
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println("ID: " + fp.getId() + "\tTYPE: " + fp.getType());
        }

        String processInstanceId = task.getProcessInstanceId();
        return new FormFieldsDto(task.getId(), processInstanceId, properties);
    }

    public List<TaskDto> createListOfTaskDtos(List<Task> tasks){
        List<TaskDto> dtos = new ArrayList<TaskDto>();
        for (Task task : tasks) {
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return dtos;
    }

    public ResponseEntity<List<TaskDto>> getActiveTasks(String username, String processName){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processName)
                .active()
                .list();

        List<Task> tasks = new ArrayList<Task>();

        for(ProcessInstance pi : processInstances){
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(username).singleResult();
            if(task!=null) {
                tasks.add(task);
            }
        }

        return new ResponseEntity(this.createListOfTaskDtos(tasks),  HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDto>> getActiveTasksByName(String username, String processName, String taskName){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processName)
                .active()
                .list();

        List<Task> tasks = new ArrayList<Task>();

        for(ProcessInstance pi : processInstances){
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(username).taskName(taskName).singleResult();
            if(task!=null) {
                tasks.add(task);
            }
        }

        return new ResponseEntity(this.createListOfTaskDtos(tasks),  HttpStatus.OK);
    }

    public List<FormSubmissionDto> getMicroservices(String processId){
        List<FormSubmissionDto> retVal = new ArrayList<>();
        String naziv = (String)runtimeService.getVariable(processId, "naziv");
        Casopis casopis = casopisRepository.findByNaziv(naziv);
        checkMicroservices(casopis,processId);
        for(NacinPlacanja np : casopis.getNaciniplacanja()){
            Boolean potvrdjen = (Boolean) runtimeService.getVariable(processId,np.getName());
            String generatedId = (String) runtimeService.getVariable(processId,"generatedId");

            if(potvrdjen == false) {
                FormSubmissionDto fsd = new FormSubmissionDto();
                fsd.setFieldId(np.getName());
                if (np.getName().equals("Paypal")) {
                    fsd.setFieldValue("https://localhost:5003/seller/" + generatedId);
                } else if (np.getName().equals("Bitcoin")) {
                    fsd.setFieldValue("https://localhost:5001/seller/" + generatedId);
                } else if (np.getName().equals("Banka")) {
                    fsd.setFieldValue("https://localhost:5002/seller/" + generatedId);
                }
                retVal.add(fsd);
            }
        }
        return retVal;
    }

    public void checkMicroservices(Casopis casopis,String processId){
        Long id = casopis.getId();
        List<NacinPlacanja> naciniPlacanja = casopis.getNaciniplacanja();
        for(NacinPlacanja np : naciniPlacanja){
            String name = np.getName();
            RestTemplate rt = restTemplate;
            boolean result = false;
            String email = casopis.getUrednici().get(0).getEmail();
            try {
                if (name.toLowerCase().equals("paypal")) {
                    result = rt.getForObject("https://localhost:8443/paypalservice/seller/get/" + email, boolean.class);
                } else if (name.toLowerCase().equals("bitcoin")) {
                    result = rt.getForObject("https://localhost:8443/bitcoinservice/seller/get/" + email, boolean.class);
                } else if (name.toLowerCase().equals("banka")) {
                    result = rt.getForObject("https://localhost:8443/bankservice/seller/get/" + email, boolean.class);
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            if(result == true){
                runtimeService.setVariable(processId,name,true);
            }
        }

    }
}

