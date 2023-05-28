package team3.meowie.member.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import team3.meowie.member.model.Provider;
import team3.meowie.member.model.User;
import team3.meowie.member.service.UserService;

@Component
public class EmailExistValidator implements ConstraintValidator<ExistEmail, String>{

	@Autowired
	private UserService userService;
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		List<User> userList = userService.findUserByEmail(email);
		if(!userList.isEmpty()) {
			for (User user : userList) {
				if(user.getProvider() == Provider.LOCAL)
					return false;
			}
		}
		
		return true;
	}

}
