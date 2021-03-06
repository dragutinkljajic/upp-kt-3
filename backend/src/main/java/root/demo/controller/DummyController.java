package root.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.cmd.GetDeploymentResourceNamesCmd;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import root.demo.model.FormFieldsDto;
import root.demo.model.FormSubmissionDto;
import root.demo.model.TaskDto;
import root.demo.model.User;

@Controller
@RequestMapping("/welcome")
@CrossOrigin(origins = "*")
public class DummyController {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_1");

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@GetMapping(path = "/get/tasks/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> get(@Context HttpServletRequest request, @PathVariable String processInstanceId) {
		
		User user = (User)request.getSession().getAttribute("user");
		
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			if (user == null && task.getAssignee() == null) {
				TaskFormData tfd = formService.getTaskFormData(task.getId());
				TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
				List<FormField> properties = tfd.getFormFields();
				t.setFormFields(properties);
				dtos.add(t);
			}
			else if (user != null && user.getUsername().equals(task.getAssignee())) {
				TaskFormData tfd = formService.getTaskFormData(task.getId());
				TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
				List<FormField> properties = tfd.getFormFields();
				t.setFormFields(properties);
				dtos.add(t);
			}
		}
		
        return new ResponseEntity(dtos,  HttpStatus.OK);
    }
	
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "registration", dto);
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/tasks/claim/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity claim(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String user = (String) runtimeService.getVariable(processInstanceId, "username");
		taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/tasks/complete/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> complete(@PathVariable String taskId) {
		Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.complete(taskId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskTemp.getProcessInstanceId()).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
        return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
    }
	
	@GetMapping(path = "/get/assigned", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> getAssigned(@Context HttpServletRequest request) {
		
		User user = (User)request.getSession().getAttribute("user");
		
		if (user == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(user.getUsername()).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
				TaskFormData tfd = formService.getTaskFormData(task.getId());
				TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
				List<FormField> properties = tfd.getFormFields();
				t.setFormFields(properties);
				dtos.add(t);
		}
		
        return new ResponseEntity(dtos,  HttpStatus.OK);
    }
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	@GetMapping(path = "/tasks/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<TaskDto> getTask(@PathVariable String taskId) {
		
		List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
		
		Task task = tasks.get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
		List<FormField> properties = tfd.getFormFields();
		t.setFormFields(properties);
		
        return new ResponseEntity(t,  HttpStatus.OK);
    }
}
