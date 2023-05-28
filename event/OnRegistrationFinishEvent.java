package team3.meowie.member.event;

import org.springframework.context.ApplicationEvent;

import team3.meowie.member.model.User;

@SuppressWarnings("serial")
public class OnRegistrationFinishEvent extends ApplicationEvent {
	
	private User user;

	public OnRegistrationFinishEvent(User user) {
		super(user);
		
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
