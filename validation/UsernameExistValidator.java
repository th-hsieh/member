package team3.meowie.member.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import team3.meowie.member.service.UserService;

public class UsernameExistValidator implements ConstraintValidator<ExistUsername, String>{

	@Autowired
	private UserService userService;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return userService.findUserByUsername(username) == null;
	}

}
