package team3.meowie.member.service;

import team3.meowie.member.model.User;

public interface IOAuthUserService {
	User findOAuthUserByEmail(String email);
	
	User saveOAuthLoginUser(String email, String name);
	
}
