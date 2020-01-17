package root.demo.handlers;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.helpers.RandomStringGenerator;
import root.demo.model.Verification;
import root.demo.repositories.VerificationRepository;

@Service
public class EmailVerificationHandler implements ExecutionListener {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private VerificationRepository verificationRepository;
	
//	  public void notify(DelegateTask delegateTask) {
//		  DelegateExecution execution = delegateTask.getExecution();
//		  
//		  String message = ("Please verify your account at: \n + "
//		  		+ "http://localhost:8080/" + RandomStringGenerator.generateRandomString(13));
//		  
//		  execution.setVariable("email_message", message);
//	  }

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		String code = RandomStringGenerator.generateRandomString(13);
		
		  String message = ("Please verify your account at: \n"
				  + "http://localhost:8080/auth/verify/" + code);
		  
		  execution.setVariable("verification_code", code);
		  execution.setVariable("email_message", message);
		  

			Verification verification = new Verification();
			verification.setUsername((String)execution.getVariable("username"));
			verification.setCode(code);
			
			verificationRepository.save(verification);
	}
}
