package sg.edu.iss.caps.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.iss.caps.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
	
	@Query("select c from Course c where c.courseName like %:name%")
	List<Course> findCoursesByName(String name);

	@Query("select c from Course c where c.courseName like %?1%")
	List<Course> searchCourseByName(String name);
	
	@Query("select c from Course c join fetch c.lecturers l WHERE l.id = :lecId")
	List<Course> getCoursesByLecturerId(@Param("lecId") String id);
	 
	@Query("select c from Course c join fetch c.lecturers l WHERE l.id = :lecId and c.courseName like :courseName")
	List<Course> findByLecturerAndCourse(@Param("lecId") String id,  @Param("courseName") String name);
	
	@Query("select c.creditUnit from Course c WHERE c.id = :courseId")
	double getCreditUnit(String courseId);
	 
}
