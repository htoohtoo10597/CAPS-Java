package sg.edu.iss.caps;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.CourseStatus;
import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.Lecturer;
import sg.edu.iss.caps.model.Student;
import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.repositories.CourseRepository;
import sg.edu.iss.caps.repositories.StudentCourseRepository;
import sg.edu.iss.caps.repositories.StudentRepository;
import sg.edu.iss.caps.services.CourseService;
import sg.edu.iss.caps.services.LecturerService;
import sg.edu.iss.caps.services.StudentService;
import sg.edu.iss.caps.utilities.StringToDateConversion;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CapsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentAssignGradeModuleTests {
	
	@Autowired
	private LecturerService lecserv;
	
	@Autowired
	private CourseService couserv;
	
	@Autowired
	private CourseRepository courrepo;
	
	@Autowired
	private StudentRepository studrepo;
	
	@Autowired
	private StudentService studserv;
	
	@Autowired
	private StudentCourseRepository studcorrepo;
	
	
	@Autowired
    StringToDateConversion dateConv;
	
	@Test
	@Order(1)
	void assignGradeToCourse() {
		
		
		//Creating a new Course and saving to DB		
		Date startDate = dateConv.StringToDate("2022-08-30");
    	Date endDate = dateConv.StringToDate("2022-12-30");
    			Course newCourse = new Course ("Neural Networks and Deep Learning2", "A neural network is a network or circuit of biological neurons, or, "
    			+ "in a modern sense, an artificial neural network, composed of artificial neurons or nodes."
    			+ " Thus, a neural network is either a biological neural network, made up of biological neurons, or an artificial neural network, "
    			+ "used for solving artificial intelligence (AI) problems.", 5, 5, startDate, endDate);
		courrepo.save(newCourse);
		
		//Creating a new Student and saving to DB
		Student newStudent = new Student ("bjjohnson", "password", "Benjamin", "Johnson", "benjamin.johnson@gmail.com");	
		studrepo.save(newStudent);
		
		//Assigning the course to student and saving to DB
		StudentCourse studCour = new StudentCourse (newStudent, newCourse, Grade.A, CourseStatus.COMPLETED); 
		studcorrepo.save(studCour);	  	
		
		
		//Retrieving back the saved CourseStudent checking if the assigned grade and courseStatus are equal
		Student studentFromDB = studserv.getStudentById(newStudent.getId());
		Course courseFromDB = couserv.getCourseById(newCourse.getId());
		
		StudentCourse courseStudentFromDB = studcorrepo
				.getStudentCourseByStudentAndCourse(studentFromDB.getId(), courseFromDB.getId());
	   Assertions.assertEquals(Grade.A, courseStudentFromDB.getGrade());
	   Assertions.assertEquals(CourseStatus.COMPLETED, courseStudentFromDB.getCourseStatus());		
	}
}
	