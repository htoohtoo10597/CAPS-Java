package sg.edu.iss.caps.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.iss.caps.model.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

	@Query("select s from Student s where s.firstName like %?1% or s.lastName like %?1%")
	List<Student> searchStudentByName(String name);
	
	@Query("SELECT c.course FROM Student s JOIN s.studentCourses c WHERE c.student.id = :id")
	List<Course> findCoursesByStudId(String id);
	
	@Query("select s from Student s where s.username = ?1 and s.password = ?2")
	List<Student> searchStudentByCredentials(String username, String password);
	
	//get all the students with all course status for a course
	@Query("select s from Student s join s.studentCourses sc WHERE sc.course.id = :courseId")
	List<Student> getStudentByCourse(String courseId);
	 
	//get the students currently taking the course (course status is ENROLLED)
	@Query("select s from Student s join s.studentCourses sc WHERE sc.course.id = :courseId and sc.courseStatus = 0")
	List<Student> getCurrentStudentsByCourse(@Param("courseId")  String courseId);

	//return student list that has fistName or lastName contains the search string for a particular course
	@Query("select s from Student s join s.studentCourses sc WHERE sc.course.id = :courseId and"
			+ " (s.lastName like :name or s.firstName like :name)")
	List<Student> searchCourseStudentByName (String courseId, String name);

	@Query("SELECT s.studentCourses FROM Student s WHERE s.id = :id")
	List<StudentCourse> findStudCoursesByStudId (String id);

}
