package team3.meowie.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import team3.meowie.member.dto.UserProfileDto;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.service.IUserProfileService;
import team3.meowie.member.service.IUserService;

@Controller
public class UserProfileController {
	
	@Autowired
	private IUserService userService;

	@Autowired
	private IUserProfileService userProfileService;
	
	@GetMapping("/user/editProfile")
	public String showEditProfilePage(Model model) {
		String username = userService.getLoginUsername();
		User user = userService.findUserByUsername(username);
		UserProfile profile = userProfileService.findProfileByUser(user);
		model.addAttribute("profile", profile);
		return "member/editUserProfile";
	}
	
	@PutMapping("/user/editProfile")
	public String editProfile(@ModelAttribute("profile") UserProfileDto userProfileDto, @RequestParam("photo") MultipartFile multipartFile) {
		UserProfile profile = userProfileService.findProfileById(userProfileDto.getId());
		userProfileService.updateUserProfile(profile, userProfileDto, multipartFile);
		return "redirect:/user/center";
	}
}
