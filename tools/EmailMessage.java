package team3.meowie.member.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import team3.meowie.ticket.service.TicketService;

@Component
public class EmailMessage {

	public SimpleMailMessage emailFormat(String recipient, String subject, String url, String message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText(message + "\n" + "http://localhost:8080/meow" + url);
		return email;
	}
@Autowired
	TicketService ticketService;
	public SimpleMailMessage qrCodeEmailFormat(String recipient, String subject, Long ticketId) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText( "\n" + "http://localhost:8080/meow");
		return email;
	} 
}
