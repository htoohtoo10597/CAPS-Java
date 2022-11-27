
package sg.edu.iss.caps.services;

import java.util.List;

import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.StudentCourse;

public interface CourseService {

	List<Course> getAllCourse();
	
	List<Course> getCoursesByLecturerId(String lecturerId);

	List<Course> findByLecturerAndCourse(String lecturerId, String name);

	void saveCourse(Course course);

	Course getCourseById(String id);

	void deleteCourseById(String id);

	List<Course> findCoursesByName(String name);

	List<Course> findCoursesByStudId(String id);

	int getEnrollCountByCourseId(String id);

	boolean isCapacityOk(String courseId);
	
	List<Course> returnCourseByName(String name);
	
	List<StudentCourse> getStudCoursesByCourseId(String id);
	
//	Llist<Course> testPagination
	
	double getCreditUnit(String courseId);
	
}
