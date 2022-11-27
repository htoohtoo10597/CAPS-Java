package sg.edu.iss.caps.utilities;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.StudentCourse;

@Service
public class CalculateGPA {

	public Double calculateGpa(String studentId, List<StudentCourse> studCourseList) {
		double gpa = 0.0;
		double numerator = 0.0;
		double denominator = 0.0;
		double gradePointMax = 5;
		for (StudentCourse studCor : studCourseList) {
			double creditUnit = studCor.getCourse().getCreditUnit();
			double gradePoint = 0;
			switch (studCor.getGrade()) {
			case A: 
				gradePoint = 5;
				break;
			case B:
				gradePoint = 4;
				break;
			case C:
				gradePoint = 3;
				break;
			case D:
				gradePoint = 2;
				break;
			case F:
				gradePoint = 0;
				break;
			case NA:
				gradePoint = 0;
				break;
			}
			numerator += creditUnit * gradePoint;
			if (studCor.getGrade() != Grade.NA) {
				denominator += creditUnit * gradePointMax;
			} else {
				denominator += 0;
			}
		}
		if (denominator != 0) {
		gpa = (numerator / denominator) * gradePointMax;
		return gpa;
		}
		else {
			return 0.0;
		}
	}
}
