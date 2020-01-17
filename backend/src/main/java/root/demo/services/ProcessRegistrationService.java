package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.FormSubmissionDto;
import root.demo.model.ScientificArea;
import root.demo.repositories.UserRepository;

@Service
public class ProcessRegistrationService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
//		 String var = "Pera";
//	      var = var.toUpperCase();
//	      execution.setVariable("input", var);
//	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
//	      System.out.println(registration);
//	      User user = identityService.newUser("");
//	      for (FormSubmissionDto formField : registration) {
//			if(formField.getFieldId().equals("username")) {
//				user.setId(formField.getFieldValue());
//			}
//			if(formField.getFieldId().equals("password")) {
//				user.setPassword(formField.getFieldValue());
//			}
//	      }
//	      identityService.saveUser(user);
		
		//set camunda user
		User user = identityService.newUser("");
		user.setId(getPropertyIfExists(execution, "username"));
		user.setPassword(getPropertyIfExists(execution, "password"));
		user.setFirstName(getPropertyIfExists(execution, "first_name"));
		user.setLastName(getPropertyIfExists(execution, "last_name"));
		user.setEmail(getPropertyIfExists(execution, "email"));
		identityService.saveUser(user);
		
		//set custom user
		root.demo.model.User customUser = new root.demo.model.User();
		customUser.setUsername(getPropertyIfExists(execution, "username"));
		customUser.setPassword(getPropertyIfExists(execution, "password"));
		customUser.setFirstName(getPropertyIfExists(execution, "first_name"));
		customUser.setLastName(getPropertyIfExists(execution, "last_name"));
		customUser.setCity(getPropertyIfExists(execution, "city"));
		customUser.setState(getPropertyIfExists(execution, "state"));
		customUser.setEmail(getPropertyIfExists(execution, "email"));
		
		List<ScientificArea> scientificAreas = (List<ScientificArea>)execution.getVariable("scientificAreas");
		for (ScientificArea area : scientificAreas) {
			area.setUser(customUser);
		}
		customUser.setScientificAreas(scientificAreas);
		userRepository.save(customUser);
	}
	
	private String getPropertyIfExists(DelegateExecution execution, String propertyName) {
		if (execution.hasVariable(propertyName)) {
			return (String)execution.getVariable(propertyName);
		}
		
		return null;
	}
}
