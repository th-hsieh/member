package team3.meowie.member.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import team3.meowie.member.dto.UserProfileDto;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.repository.UserProfileRepository;

@Service
public class UserProfileService implements IUserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Override
	public UserProfile findProfileById(Integer id) {
		Optional<UserProfile> option = userProfileRepository.findById(id);
		
		if(option.isPresent()) {
			UserProfile profile = option.get();
			return profile;
		}
		
		return null;
	}
	
	@Override
	public UserProfile findProfileByUser(User user) {
		return userProfileRepository.findByUser(user);
	}
	
	@Override
	public void createUserProfile(User user) {
		UserProfile profile = new UserProfile(user);
		userProfileRepository.save(profile);
	}

	@Override
	public void updateUserProfile(UserProfile profile, UserProfileDto userProfileDto, MultipartFile multipartFile) {
		String photo = null;
		try {
			photo = Base64.getEncoder().encodeToString(multipartFile.getBytes());
			profile.setPhone(userProfileDto.getPhone());
			profile.setBirthday(userProfileDto.getBirthday());
			profile.setIntroduction(userProfileDto.getIntroduction());
			if(!photo.isEmpty()) {
				profile.setPhoto(photo);
			}
			userProfileRepository.save(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
