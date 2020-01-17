package root.demo.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import root.demo.model.Verification;
import root.demo.repositories.UserRepository;
import root.demo.repositories.VerificationRepository;

@Service
public class EmailService implements JavaDelegate{

	@Autowired
    private JavaMailSender javaMailSender;
	
	void sendEmail(String email, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject(message);
        msg.setText(message);

        javaMailSender.send(msg);

    }
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if (execution.hasVariable("email") && execution.hasVariable("email_message")) {
			sendEmail((String)execution.getVariable("email"), (String)execution.getVariable("email_message"));
		}
	}
}
