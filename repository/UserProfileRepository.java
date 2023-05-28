package team3.meowie.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer>{
	UserProfile findByUser(User user);

}
