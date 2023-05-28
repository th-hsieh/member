package team3.meowie.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import team3.meowie.member.model.User;
import team3.meowie.member.service.IUserService;

@Controller
public class BackUserController {

	@Autowired
	private IUserService userService;
	
	@GetMapping("/backMember")
	public String getPageUser(@RequestParam(name="p",defaultValue = "1") Integer pageNumber, Model model) {
		Page<User> page = userService.findByPage(pageNumber);
		model.addAttribute("page", page);
		return "NewBack/member/userData";
	}
	
	@GetMapping("/editMember")
	public String eidtUserData(@RequestParam(name="p",defaultValue = "1") Integer pageNumber, Model model) {
		Page<User> page = userService.findByPage(pageNumber);
		model.addAttribute("page", page);
		return "NewBack/member/editUserData";
	}
	
	@ResponseBody
	@PutMapping("/editMember")
	public void disableUser(@RequestBody Map<String, String> jsonObject) {
		String username = jsonObject.get("username");
		String enabled = jsonObject.get("enabled");
		System.out.println(username + enabled);
		userService.updateUserEnabled(username, enabled);
	}
	
}
