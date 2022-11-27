package sg.edu.iss.caps.utilities;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.iss.caps.model.Course;

@Service
public class RegistrationUtil {
	
	public boolean overlaps(List<Course> currentList, Course another) {

		boolean status = false;
		
		for (Course course : currentList) {
		
			if (course.getEndDate().after(another.getStartDate()) && course.getStartDate().before(another.getEndDate())) {
				status = true;
				break;
			}
		}
		return status;
	}
}
