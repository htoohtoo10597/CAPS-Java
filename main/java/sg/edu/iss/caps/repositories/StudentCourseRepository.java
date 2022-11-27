package sg.edu.iss.caps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.iss.caps.model.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, String> {
	
	
	@Query("select sc from StudentCourse sc where sc.student.id = ?1 and sc.course.id = ?2 ")
	StudentCourse getStudentCourseByStudentAndCourse(String studentId, String courseId);

}
