package team3.meowie.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import team3.meowie.member.validation.ValidPassword;

public class NewPasswordDto {
	
	private String username;
	
	@NotNull
	@NotBlank(message = "This field shouldn't be blank")
	@ValidPassword
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
