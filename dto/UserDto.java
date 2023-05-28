package team3.meowie.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import team3.meowie.member.validation.ExistEmail;
import team3.meowie.member.validation.ExistUsername;
import team3.meowie.member.validation.ValidPassword;

public class UserDto {
	
	@NotNull
	@NotBlank(message = "This field shouldn't be blank")
	private String name;
	
	@NotNull
	@NotBlank(message = "This field shouldn't be blank")
	@ExistUsername
	private String username;
	
	@NotNull
	@NotBlank(message = "This field shouldn't be blank")
	@ValidPassword
	private String password;

	@NotNull
	@NotBlank(message = "This field shouldn't be blank")
	@Email(message = "Email format error")
	@ExistEmail
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
