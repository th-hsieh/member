package team3.meowie.member.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import team3.meowie.member.security.MeowieUserDetails;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		MeowieUserDetails meowieUserDetails = (MeowieUserDetails) authentication.getPrincipal();
		
		String redirectUrl = request.getContextPath();
		
		if(meowieUserDetails.hasRole("ROLE_ADMIN")) {
			redirectUrl = "backIndex";
			response.sendRedirect(redirectUrl);
		}
		else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
		
	}

}
