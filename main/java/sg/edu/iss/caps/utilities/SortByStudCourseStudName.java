package sg.edu.iss.caps.utilities;

import java.util.Comparator;

import sg.edu.iss.caps.model.StudentCourse;

public class SortByStudCourseStudName implements Comparator<StudentCourse>{

	@Override
	public int compare(StudentCourse o1, StudentCourse o2) {
		return o1.getStudent().getFirstName().toLowerCase().compareTo(o2.getStudent().getFirstName().toLowerCase());
	}

	
}
