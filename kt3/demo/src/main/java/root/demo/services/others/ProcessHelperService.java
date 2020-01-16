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
import root.demo.Dto.FormFieldsDto;
import root.demo.Dto.FormSubmissionDto;
import root.demo.Dto.TaskDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProcessHelperService {

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

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
}

