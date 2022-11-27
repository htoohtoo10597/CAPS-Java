package sg.edu.iss.caps.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Lecturer {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	
	@NotNull
	@NotEmpty(message = "Username cannot be empty")
	@Column(nullable = false, length = 50, unique=true)
	private String username;
	
	@NotNull
	@NotEmpty(message = "Password cannot be empty")
	@Column(nullable = false, length = 50)
	private String password;
	
	@NotNull
	@NotEmpty(message = "First Name cannot be empty")
	@Column(nullable = false, length = 50)
	private String firstName;
	
	@NotNull
	@NotEmpty(message = "Last name cannot be empty")
	@Column(nullable = false, length = 50)
	private String lastName;
	
	@NotNull
	@Column(nullable = false, length = 50, unique=true)
	private String email;
	
	@ManyToMany (cascade = CascadeType.ALL, fetch= FetchType.EAGER)
	@JoinTable(name="lecturer_course",
		joinColumns= @JoinColumn(name= "lecturer_Id"),
		inverseJoinColumns= @JoinColumn(name="course_Id"))
	private List<Course> courses;

	public Lecturer(String username, String password, String firstName, String lastName, String email) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	
}