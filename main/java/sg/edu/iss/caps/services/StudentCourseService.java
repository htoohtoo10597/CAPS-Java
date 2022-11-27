package sg.edu.iss.caps.services;

import sg.edu.iss.caps.model.StudentCourse;

public interface StudentCourseService {

	void newStudentCourse(StudentCourse studentCourse);
	
	void removeStudentCourseById(String id);
	
	StudentCourse getStudentCourseById(String id);
	
	StudentCourse getStudentCourseByStudentAndCourse(String studentId, String CourseId);

}
