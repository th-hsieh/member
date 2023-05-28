package team3.meowie.member.service;

import java.util.List;

import org.springframework.data.domain.Page;

import team3.meowie.member.dto.UserDto;
import team3.meowie.member.model.PasswordResetToken;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.model.VerificationToken;

public interface IUserService {
	User registerNewUser(UserDto userdto);
	
	List<User> findUserByEmail(String email);
	
	User findUserByUsername(String username);
	
	User findUserByToken(String token);
	
	void createUserVerificationToken(User user, String token);
	
	VerificationToken getVerificationToken(String token);
	
	VerificationToken generateNewVerificationToken(String token);
	
	void saveRegisteredUser(User user);
	
	UserProfile getUserProfileByUsername(String username);
	
	boolean isLogin();
	
	String getLoginUsername();
	
	void createResetPasswordToken(User user, String token);
	
	PasswordResetToken getPasswordResetToken(String token);
	
	void updateUserPassword(User user, String password);
	
	boolean checkOldPassword(User user, String password);
	
	Page<User> findByPage(Integer pageNumber);

	void updateUserEnabled(String usernam, String enabled);
	
}
