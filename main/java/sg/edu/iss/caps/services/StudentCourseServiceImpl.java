package sg.edu.iss.caps.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.repositories.StudentCourseRepository;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

	@Autowired
	private StudentCourseRepository studCourseRepo;
	
	@Override
	public void newStudentCourse(StudentCourse studentCourse) {
		studCourseRepo.saveAndFlush(studentCourse);
	}

	@Override
	public void removeStudentCourseById(String id) {
		studCourseRepo.deleteById(id);
	}
	
	public void removeallStudentCourse() {
		studCourseRepo.deleteAll();
	}

	@Override
	public StudentCourse getStudentCourseById(String id) {
		Optional<StudentCourse> optional = studCourseRepo.findById(id);
		StudentCourse studentCourse = null;
		if(optional.isPresent()){
			studentCourse = optional.get();
		} else {
			throw new RuntimeException("StudentCourse not found.");
		}
		return studentCourse;
	}
	
	@Override
	public StudentCourse getStudentCourseByStudentAndCourse(String studentId, String courseId) {
		
		StudentCourse studentCourse = studCourseRepo.getStudentCourseByStudentAndCourse (studentId, courseId);
				
		return studentCourse;
	}
		
}
