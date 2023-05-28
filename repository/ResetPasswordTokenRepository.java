package team3.meowie.member.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import team3.meowie.member.model.PasswordResetToken;

public interface ResetPasswordTokenRepository extends JpaRepository<PasswordResetToken, Integer>{
	PasswordResetToken findByToken(String token);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM PasswordResetToken WHERE expirationDate <= :expirationDate")
	void deleteExpiredDate(Date expirationDate);
}
