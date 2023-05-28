package team3.meowie.member.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "verificationToken")
public class VerificationToken {
	
	private static final int EXPIRATION = 60;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String token;
	
	@OneToOne
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
	private User user;
	
	@Column(columnDefinition = "datetime")
	private Date expirationDate;

	public VerificationToken() {
	}

	public VerificationToken(User user, String token) {
		this.user = user;
		this.token = token;
		this.expirationDate = calculateExpirationDate(EXPIRATION);
	}
	
	private Date calculateExpirationDate(int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, minutes);
		return new Date(cal.getTime().getTime());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public void updateToken(String token) {
		this.token = token;
		this.expirationDate = calculateExpirationDate(EXPIRATION);
	}

}
