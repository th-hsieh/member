package team3.meowie.member.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team3.meowie.member.model.Provider;
import team3.meowie.member.model.Role;
import team3.meowie.member.model.User;
import team3.meowie.member.repository.RoleRepository;
import team3.meowie.member.repository.UserRepository;

@Service
public class OAuthUserService implements IOAuthUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public User findOAuthUserByEmail(String email) {
		List<User> userList = userRepository.findByEmail(email);
		
		for (User user : userList) {
			if(user.getProvider() == Provider.GOOGLE)
				return user;
		}
		
		return null;
	}
	
	@Override
	public User saveOAuthLoginUser(String email, String name) {
		Collection<Role> role = new ArrayList<>();
		role.add(roleRepository.findByName("ROLE_USER"));
		
		User user = new User();
		user.setName(name);
		user.setUsername(name);
		user.setEmail(email);
		user.setRoles(role);
		user.setEnabled(true);
		user.setProvider(Provider.GOOGLE);
		
		return userRepository.save(user);
	}

}
