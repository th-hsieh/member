package team3.meowie.member.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import team3.meowie.member.dto.UserDto;
import team3.meowie.member.event.OnRegistrationFinishEvent;
import team3.meowie.member.model.User;
import team3.meowie.member.model.VerificationToken;
import team3.meowie.member.service.IUserService;
import team3.meowie.member.tools.EmailMessage;

@Controller
public class RegisterController {
	
	@Autowired
	IUserService userService;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private EmailMessage emailMessage;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@ResponseBody
	@PostMapping("/api/user/registration")
	public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			List<FieldError> errorList = result.getFieldErrors();
			
			FieldError error = null;
			for(int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}
		Map<String, String> success = new HashMap<>();
		success.put("message", "註冊成功!請去信箱進行驗證!");
		success.put("url", "http://localhost:8080/meow");
		User registered = userService.registerNewUser(userDto);
		eventPublisher.publishEvent(new OnRegistrationFinishEvent(registered));
		return ResponseEntity.ok(success);
	}
	
	@GetMapping("/resendRegistrationToken")
	public String resendRegistrationToken(@RequestParam String token) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(token);
		String newToken = verificationToken.getToken();
		User user = userService.findUserByToken(newToken);
		
		String recipient = user.getEmail();
		String subject = "Registration varify mail";
		String verificationUrl = "/registrationConfirm?token=" + newToken;
		String message = "Congratulation! You successfully registered,"
				+ "please check the link below to verify your mail:";
		
		SimpleMailMessage email = emailMessage.emailFormat(recipient, subject, verificationUrl, message);
		mailSender.send(email);
		
		return "redirect:/";
	}
	
	@GetMapping("/registrationConfirm")
	public String userRegistrationConfirm(@RequestParam String token, Model model) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
		
		if(verificationToken != null) {
			Calendar calendar = Calendar.getInstance();
			if(verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime() <= 0) {
				Map<String, String> message = new HashMap<>();
				message.put("message", "Expiration Token!");
				message.put("token", token);
				model.addAttribute("message", message);
				return "member/verifyfailed";
			}
			
			User user = verificationToken.getUser();
			
			if(user.isEnabled()) 
				return "redirect:/";
			
			userService.saveRegisteredUser(user);
			return "redirect:/";
			
		}
		return null;
	}
	
}
