package team3.meowie.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import team3.meowie.member.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
	
	List<User> findByEmail(String email);
}
