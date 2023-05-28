package team3.meowie.member.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import team3.meowie.member.model.Provider;
import team3.meowie.member.model.Role;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.repository.RoleRepository;
import team3.meowie.member.repository.UserProfileRepository;
import team3.meowie.member.repository.UserRepository;

@Component
public class DefaultLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;
	
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;

		final Role admin = createRole("ROLE_ADMIN");
		final Role user = createRole("ROLE_USER");
		final Role unverified = createRole("ROLE_UNVERIFIED");
		
		createDefaultUser("meowieAdmin", "meowie", "pAssw0rd", "default@gmail.com", new ArrayList<>(Arrays.asList(admin)), true);
		createDefaultUser("testuser", "testuser", "pAssw0rd", "defaulttestuser@gmail.com", new ArrayList<>(Arrays.asList(user)), true);
		createDefaultUser("unverifieduser", "unverified", "pAssw0rd", "defaultunverified@gmail.com", new ArrayList<>(Arrays.asList(unverified)), false);
		
		alreadySetup = true;
	}

	@Transactional
	private Role createRole(final String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role = roleRepository.save(role);
		}
		return role;
	}
	
	private User createDefaultUser(String username, String name, String password, String email, Collection<Role> roles, boolean enable) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			user = new User();
			user.setName(name);
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(email);
			user.setRoles(roles);
			user.setEnabled(enable);
			user.setProvider(Provider.LOCAL);
		}
		user = userRepository.save(user);
		UserProfile profile = userProfileRepository.findByUser(user);
		if(profile == null) {
			profile = new UserProfile(user);
		}
			userProfileRepository.save(profile);
		return user;
	}

}
