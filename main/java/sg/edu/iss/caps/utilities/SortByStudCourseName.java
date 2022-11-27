package sg.edu.iss.caps.utilities;

import java.util.Comparator;

import sg.edu.iss.caps.model.StudentCourse;

public class SortByStudCourseName implements Comparator<StudentCourse>{

	@Override
	public int compare(StudentCourse o1, StudentCourse o2) {
		return o1.getCourse().getCourseName().toLowerCase().compareTo(o2.getCourse().getCourseName().toLowerCase());
	}

}
