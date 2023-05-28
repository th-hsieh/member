package team3.meowie.member.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import team3.meowie.member.model.Role;
import team3.meowie.member.model.User;

public class MeowieUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public MeowieUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<Role> roles = user.getRoles();
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) 
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		
		return authorities;
	}
	
	public boolean hasRole(String roleName) {
		return this.user.hasRole(roleName);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

}
