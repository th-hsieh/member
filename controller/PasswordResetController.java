package team3.meowie.member.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import team3.meowie.member.dto.ChangePasswordDto;
import team3.meowie.member.dto.NewPasswordDto;
import team3.meowie.member.event.OnPasswordResetEvent;
import team3.meowie.member.model.PasswordResetToken;
import team3.meowie.member.model.Provider;
import team3.meowie.member.model.User;
import team3.meowie.member.service.IUserService;

@Controller
public class PasswordResetController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	
	@ResponseBody
	@PostMapping("/user/resetPassword")
	public ResponseEntity<String> resetUserPassword(@RequestBody Map<String, String> jsonObject) {
		String email = jsonObject.get("email");
		List<User> userList = userService.findUserByEmail(email);
		if(userList.isEmpty())
			return ResponseEntity.badRequest().body("User doesn't exist!");
		
		for (User user : userList) {
			if(user.getProvider() != Provider.GOOGLE) {
				User localUser = user;
				eventPublisher.publishEvent(new OnPasswordResetEvent(localUser));
			}
		}
		return ResponseEntity.ok("Send successful");
	}
	
	@GetMapping("/user/newPassword")
	public String showResetPasswordPage(@RequestParam String token, Model model) {
		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		
		if(passwordResetToken != null) {
			Calendar calendar = Calendar.getInstance();
			if(passwordResetToken.getExpirationDate().getTime() - calendar.getTime().getTime() <= 0) {
				String message = "Expiration Token!";
				model.addAttribute("message", message);
				return "member/passwordTokenVerifyfailed";
			}
			
			User user = passwordResetToken.getUser();
			model.addAttribute("user", user);
			return "member/updateNewPassword";
		}
		
		return null;
	}
	
	@ResponseBody
	@PutMapping("/user/newPassword")
	public ResponseEntity<Map<String, String>> saveNewPassword(@Valid @RequestBody NewPasswordDto passwordDto, BindingResult result) {
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			List<FieldError> errorList = result.getFieldErrors();
			
			FieldError error = null;
			for(int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}
		
		Map<String, String> success = new HashMap<>();
		success.put("url", "http://localhost:8080/meow/login");
		User user = userService.findUserByUsername(passwordDto.getUsername());
		String newPassword = passwordDto.getPassword();
		userService.updateUserPassword(user, newPassword);
		
		return ResponseEntity.ok(success);
	}
	
	@ResponseBody
	@PutMapping("/user/center/changePassword")
	public ResponseEntity<Map<String, String>> changeNewPassword(@Valid @RequestBody ChangePasswordDto passwordDto, BindingResult result) {
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			List<FieldError> errorList = result.getFieldErrors();
			
			FieldError error = null;
			for(int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}
		User user = userService.findUserByUsername(passwordDto.getUsername());
		
		if(!userService.checkOldPassword(user, passwordDto.getOldPassword())) {
			Map<String, String> fail = new HashMap<>();
			fail.put("fail", "Invalid old password!");
			return ResponseEntity.badRequest().body(fail);
		}
		
		String newPassword = passwordDto.getPassword();
		userService.updateUserPassword(user, newPassword);
		Map<String, String> success = new HashMap<>();
		success.put("success", "修改成功!");
		
		return ResponseEntity.ok(success);
	}
	
}
