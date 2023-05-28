package team3.meowie.member.event;

import org.springframework.context.ApplicationEvent;

import team3.meowie.member.model.User;

@SuppressWarnings("serial")
public class OnPasswordResetEvent extends ApplicationEvent {
	
	private User user;

	public OnPasswordResetEvent(User user) {
		super(user);
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

}
