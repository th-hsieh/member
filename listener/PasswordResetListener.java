package team3.meowie.member.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import team3.meowie.member.event.OnPasswordResetEvent;
import team3.meowie.member.model.User;
import team3.meowie.member.service.IUserService;
import team3.meowie.member.tools.EmailMessage;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private EmailMessage emailMessage;
	
	@Autowired
    private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnPasswordResetEvent event) {
		this.resetPassword(event);
	}
	
	private void resetPassword(OnPasswordResetEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.createResetPasswordToken(user, token);
		
		String recipient = user.getEmail();
		String subject = "Password reset mail";
		String resetUrl = "/user/newPassword?token=" + token;
		String message = "This is your password reset mail,"
				+ "please check the link below to reset your password:";
		
		SimpleMailMessage email = emailMessage.emailFormat(recipient, subject, resetUrl, message);
		mailSender.send(email);
	}
	

}
