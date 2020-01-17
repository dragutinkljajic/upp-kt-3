package root.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import root.demo.model.User;
import root.demo.model.Verification;
import root.demo.repositories.UserRepository;
import root.demo.repositories.VerificationRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	VerificationRepository verificationRepository;

	@PostMapping(path="/login", produces = "application/json")
    public @ResponseBody ResponseEntity<User> login(@Context HttpServletRequest request, @RequestBody User user) {
		
		User existingUser = repository.findByUsername(user.getUsername());
		
		if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		
		request.getSession().setAttribute("user", existingUser);
		
		return new ResponseEntity(existingUser, HttpStatus.OK);
	}
	
	@GetMapping(path = "/logout",
		produces = "application/json")
	public ResponseEntity<String> logout(@Context HttpServletRequest request) {
		
		User user = (User)request.getSession().getAttribute("user");
		
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		request.getSession().invalidate();
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping(path = "/verify/{code}",
		produces = "application/json")
	public ResponseEntity<String> logout(@PathVariable String code) {
		
		Verification verification = verificationRepository.findByCode(code);
		User user = repository.findByUsername(verification.getUsername());
		
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		user.setVerified(true);
		repository.save(user);
		
		return new ResponseEntity("User successfully verified.", HttpStatus.OK);
	}
}