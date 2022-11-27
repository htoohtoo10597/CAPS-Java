package sg.edu.iss.caps;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.CourseStatus;
import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.Student;
import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.repositories.CourseRepository;
import sg.edu.iss.caps.repositories.StudentCourseRepository;
import sg.edu.iss.caps.repositories.StudentRepository;
import sg.edu.iss.caps.services.StudentCourseService;
import sg.edu.iss.caps.services.StudentService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CapsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentCourseunitTests {
    @Autowired
    private StudentRepository studrepo;

    @Autowired
    private CourseRepository courserepo;

    @Autowired
    private StudentCourseRepository studcorepo;

    @Autowired
    private StudentService studserv;

    @Autowired
    private StudentCourseService studcorserv;

    @Test
    @Order(1)
    void EnrollACourse() {
        Student stud1 = new Student("peterp", "password", "peter", "parker", "spiderman@avengers.com");
        studrepo.saveAndFlush(stud1);
        Course course1 = new Course("Chemistry 101", "How to chemistry", 2, 4);
        courserepo.saveAndFlush(course1);
        
        int before = studcorepo.findAll().size();
        
        StudentCourse sc = new StudentCourse(stud1, course1, Grade.NA, CourseStatus.ENROLLED);
        studcorserv.newStudentCourse(sc);
       
        int after = studcorepo.findAll().size();
        
        Assertions.assertEquals(after, before + 1);
    }

    @Test
    @Order(2)
    void UnEnrollACourse() {
        int before = studcorepo.findAll().size();
        
        List<StudentCourse> sclist = studcorepo.findAll();
        studcorserv.removeStudentCourseById(sclist.get(0).getId());
        
        int after = studcorepo.findAll().size();

        Assertions.assertEquals(after, before - 1);
    }

    @Test
    @Order(3)
    void ViewAllEnrolledCourseOfStudent(){
        Student stud1 = new Student("tony", "password", "tony", "stark", "iamironman@avengers.com");
        studrepo.saveAndFlush(stud1);
        
        Course course1 = new Course("Physics 101", "How to physics", 2, 4);
        courserepo.saveAndFlush(course1);
        Course course2 = new Course("Entrepreneur 101", "How to entrepreneur", 2, 4);
        courserepo.saveAndFlush(course2);
        Course course3 = new Course("Avengers 101", "How to be an avenger", 2, 4);
        courserepo.saveAndFlush(course3);
        Course course4 = new Course("Philantropy 101", "How to philantropy", 2, 4);
        courserepo.saveAndFlush(course4);

        StudentCourse sc1 = new StudentCourse(stud1, course1, Grade.NA, CourseStatus.ENROLLED);
        studcorserv.newStudentCourse(sc1);
        StudentCourse sc2 = new StudentCourse(stud1, course2, Grade.NA, CourseStatus.ENROLLED);
        studcorserv.newStudentCourse(sc2);
        StudentCourse sc3 = new StudentCourse(stud1, course3, Grade.NA, CourseStatus.ENROLLED);
        studcorserv.newStudentCourse(sc3);
        StudentCourse sc4 = new StudentCourse(stud1, course3, Grade.NA, CourseStatus.ENROLLED);
        studcorserv.newStudentCourse(sc4);

        Student stud = studserv.returnStudentByCredentials("tony", "password").get(0);
        List<StudentCourse> listStudCourse = studserv.findStudCoursesByStudId(stud.getId());

        Assertions.assertEquals(listStudCourse.size(), 4);
    }
}
