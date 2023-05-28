package team3.meowie.member.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.oauth.MeowieOAtuh2User;
import team3.meowie.member.service.IOAuthUserService;
import team3.meowie.member.service.IUserProfileService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Autowired
	private IOAuthUserService oAuthUserService;
	
	@Autowired
    private IUserProfileService userProfileService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		MeowieOAtuh2User meowieOAtuh2User = (MeowieOAtuh2User) authentication.getPrincipal();
		String email = meowieOAtuh2User.getEmail();
		String name = meowieOAtuh2User.getName();
		
		User user = oAuthUserService.findOAuthUserByEmail(email);
		if(user == null) {
			User googleUser = oAuthUserService.saveOAuthLoginUser(email, name);
			UserProfile userProfile = userProfileService.findProfileByUser(googleUser);
			if(userProfile == null)
				userProfileService.createUserProfile(googleUser);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
