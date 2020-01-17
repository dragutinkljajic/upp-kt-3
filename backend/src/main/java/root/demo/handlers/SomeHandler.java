package root.demo.handlers;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.ScientificArea;

@Service
public class SomeHandler implements TaskListener {
	
	@Autowired
	IdentityService identityService;
	
	  public void notify(DelegateTask delegateTask) {
//		  System.out.println("Kreiran prvi task");
//		  List<User> users = identityService.createUserQuery().userIdIn("pera", "mika", "zika").list();
//		
//		  users = identityService.createUserQuery().list();
//		  DelegateExecution execution = delegateTask.getExecution();
//		  execution.setVariable("users", users);
		  
		  DelegateExecution execution = delegateTask.getExecution();
		  List<ScientificArea> scientificAreas = new ArrayList<ScientificArea>();
		  if(execution.hasVariable("scientificAreas")) {
			  scientificAreas = (List<ScientificArea>)execution.getVariable("scientificAreas");
		  }
		  
		  ScientificArea scientificArea = new ScientificArea();
		  scientificArea.setName((String)execution.getVariable("area_name"));
		  scientificAreas.add(scientificArea);
		  execution.setVariable("scientificAreas", scientificAreas);
	  }
}