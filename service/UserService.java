package team3.meowie.member.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import team3.meowie.member.dto.UserDto;
import team3.meowie.member.model.PasswordResetToken;
import team3.meowie.member.model.Provider;
import team3.meowie.member.model.Role;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.model.VerificationToken;
import team3.meowie.member.repository.ResetPasswordTokenRepository;
import team3.meowie.member.repository.RoleRepository;
import team3.meowie.member.repository.UserProfileRepository;
import team3.meowie.member.repository.UserRepository;
import team3.meowie.member.repository.VerificationTokenRepository;


@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User registerNewUser(UserDto userDto) {
		User user = new User();
		Collection<Role> role = new ArrayList<>();
		role.add(roleRepository.findByName("ROLE_USER"));
		
		user.setName(userDto.getName());
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());
		user.setRoles(role);
		user.setProvider(Provider.LOCAL);
		return userRepository.save(user);
	}

	@Override
	public List<User> findUserByEmail(String email) {
		List<User> user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findUserByToken(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		if(verificationToken != null)
			return verificationToken.getUser();
		
		return null;
	}

	@Override
	public void createUserVerificationToken(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
	
	@Override
	public VerificationToken generateNewVerificationToken(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.updateToken(UUID.randomUUID().toString());
		return verificationTokenRepository.save(verificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}

	@Override
	public UserProfile getUserProfileByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return userProfileRepository.findByUser(user);
	}

	@Override
	public boolean isLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return !(auth == null || auth instanceof AnonymousAuthenticationToken);
	}

	@Override
	public String getLoginUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Override
	public void createResetPasswordToken(User user, String token) {
		PasswordResetToken resetToken = new PasswordResetToken(user, token);
		resetPasswordTokenRepository.save(resetToken);
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return resetPasswordTokenRepository.findByToken(token);
	}

	@Override
	public void updateUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	public boolean checkOldPassword(User user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}

	@Override
	public Page<User> findByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 5, Sort.Direction.ASC, "id");
		Page<User> page = userRepository.findAll(pgb);
		return page;
	}
	
	@Override
	public void updateUserEnabled(String usernam, String enabled) {
		User user = userRepository.findByUsername(usernam);
		if(enabled.equals("true"))
			user.setEnabled(true);
		else
			user.setEnabled(false);
		userRepository.save(user);
	}
	
}
