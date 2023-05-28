package team3.meowie.member.service;

import org.springframework.web.multipart.MultipartFile;

import team3.meowie.member.dto.UserProfileDto;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;

public interface IUserProfileService {
	UserProfile findProfileById(Integer id);
	
	UserProfile findProfileByUser(User user);
	
	void createUserProfile(User user);
	
	void updateUserProfile(UserProfile userProfile, UserProfileDto userProfileDto, MultipartFile multipartFile);
}
