package sg.edu.iss.caps.utilities;

import java.util.Comparator;

import sg.edu.iss.caps.model.Course;

public class SortByCourseName implements Comparator<Course>{

	@Override
	public int compare(Course o1, Course o2) {
		return o1.getCourseName().toLowerCase().compareTo(o2.getCourseName().toLowerCase());
	}
	
}
