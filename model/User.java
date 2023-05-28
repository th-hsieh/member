package team3.meowie.member.model;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import team3.meowie.dic.model.Posts;
import team3.meowie.ticket.model.Ticket;

@Entity
@Table(name = "users")
//忽略掉user的某些屬性，否則json傳遞會有問題
@JsonIgnoreProperties({ "password", "email", "enabled", "registerDate", "roles", "ticket"})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer id;
	
	@Column(name = "name", columnDefinition = "nvarchar(20)")
	private String name;
	
	@Column(name = "username", columnDefinition = "nvarchar(20)")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	private boolean enabled;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "registerDate", columnDefinition = "datetime", nullable = false)
	private Date registerDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_USER")), 
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id", foreignKey = @ForeignKey(name = "FK_ROLE")))
	private Collection<Role> roles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Collection<Ticket> ticket;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Collection<Posts> posts;
	
	@PrePersist
	public void onRegister() {
		if(registerDate == null)
			registerDate = new Date();
	}
	
	public User() {
		this.enabled = false;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = this.roles.iterator();
		while(iterator.hasNext()) {
			Role role = iterator.next();
			if(role.getName().equals(roleName))
				return true;
		}
		return false;
	}
	
	public Collection<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(Collection<Ticket> ticket) {
		this.ticket = ticket;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

}
