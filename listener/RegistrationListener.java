package team3.meowie.member.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import team3.meowie.member.event.OnRegistrationFinishEvent;
import team3.meowie.member.model.User;
import team3.meowie.member.service.IUserProfileService;
import team3.meowie.member.service.IUserService;
import team3.meowie.member.tools.EmailMessage;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationFinishEvent> {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserProfileService userProfileService;
	
	@Autowired
	private EmailMessage emailMessage;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationFinishEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationFinishEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.createUserVerificationToken(user, token);
		userProfileService.createUserProfile(user);
		
		String recipient = user.getEmail();
		String subject = "Registration varify mail";
		String verificationUrl = "/registrationConfirm?token=" + token;
		String message = "Congratulation! You successfully registered,"
				+ "please check the link below to verify your mail:";
		
		SimpleMailMessage email = emailMessage.emailFormat(recipient, subject, verificationUrl, message);
		mailSender.send(email);
	}
	
}
