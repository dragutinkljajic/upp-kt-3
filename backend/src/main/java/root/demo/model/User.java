package root.demo.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	
	@Column(nullable = false)
	private String password;

	@Column
	private boolean isReviewer;
	
	@Column
	private boolean isVerified;
	
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column
	private String city;
	
	@Column
	private String state;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<ScientificArea> scientificAreas;

	public User() {}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isReviewer() {
		return isReviewer;
	}


	public void setReviewer(boolean isReviewer) {
		this.isReviewer = isReviewer;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public Collection<ScientificArea> getScientificAreas() {
		return scientificAreas;
	}


	public void setScientificAreas(Collection<ScientificArea> scientificAreas) {
		this.scientificAreas = scientificAreas;
	}


	public boolean isVerified() {
		return isVerified;
	}


	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
}