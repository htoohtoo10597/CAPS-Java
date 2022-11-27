package sg.edu.iss.caps;

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
import sg.edu.iss.caps.model.Lecturer;
import sg.edu.iss.caps.services.CourseService;
import sg.edu.iss.caps.services.LecturerService;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CapsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminLecturerTests {
	
	@Autowired
	private LecturerService lecserv;
	
	@Autowired
	private CourseService couserv;
	
	@Test
	@Order(1)
	void AddLecturerToDatabase() {
		//adding lecturer to database code
		Lecturer lec = new Lecturer("username01", "password", "Sample", "Person", "sample@email.com");
		List<Lecturer> allLecturers = lecserv.getAllLecturers();
		
		int numOfLecturers = allLecturers.size();
		
		lecserv.saveLecturer(lec);
		
		Assertions.assertEquals(lecserv.getAllLecturers().size(), numOfLecturers + 1);
	}
	
	@Test
	@Order(2)
	void RemoveLecturerFromDatabase() {
		List<Lecturer> lecList = lecserv.getAllLecturers();
		
		Random rand = new Random();
		
		Lecturer randLec = lecList.get(rand.nextInt(lecList.size()));
		
		lecserv.deleteLecturerById(randLec.getId());
		
		List<Lecturer> newLecList = lecserv.getAllLecturers();
		
		Assertions.assertNotEquals(randLec, newLecList);
		
	}
	
	@Test
	@Order(3)
	void addCourseToLecturer() {
		List<Lecturer> lecList = lecserv.getAllLecturers();
		
		Random rand = new Random();
		
		Lecturer randLec = lecList.get(rand.nextInt(lecList.size()));
		
		int numOfCourses = randLec.getCourses().size();
		
		List<Course> courseList = couserv.getAllCourse();
		
		Course randCourse = courseList.get(rand.nextInt(courseList.size()));
		
		if (!randLec.getCourses().contains(randCourse)) {
			randLec.getCourses().add(randCourse);
		}
		
		Assertions.assertEquals(randLec.getCourses().size(), numOfCourses + 1);
	}
	
}
