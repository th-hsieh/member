package team3.meowie.member.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserProfileDto {
	
	private Integer id;
	
	private String phone;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	private String introduction;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
