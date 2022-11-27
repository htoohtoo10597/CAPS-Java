package sg.edu.iss.caps.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class StudentCourse {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JsonIgnore
	private Student student;
	
	@ManyToOne
	private Course course;
	
	private Grade grade;
	//private DateTime enrollmentDate;
	private CourseStatus courseStatus;
	
	public StudentCourse(Student student, Course course, CourseStatus courseStatus) {
		super();
		this.student = student;
		this.course = course;
		this.courseStatus = courseStatus;
	}

	public StudentCourse(Student student, Course course, Grade grade, CourseStatus courseStatus) {
		super();
		this.student = student;
		this.course = course;
		this.grade = grade;
		this.courseStatus = courseStatus;
	}
	
	
	
	
}
