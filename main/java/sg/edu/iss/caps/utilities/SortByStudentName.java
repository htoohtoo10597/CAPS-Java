package sg.edu.iss.caps.utilities;

import java.util.Comparator;

import sg.edu.iss.caps.model.Student;

public class SortByStudentName implements Comparator<Student>{

	@Override
	public int compare(Student s1, Student s2) {
		return s1.getFirstName().toLowerCase().compareTo(s2.getFirstName().toLowerCase());
	}
}
