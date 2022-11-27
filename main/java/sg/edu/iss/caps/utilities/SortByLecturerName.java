package sg.edu.iss.caps.utilities;

import java.util.Comparator;

import sg.edu.iss.caps.model.Lecturer;

public class SortByLecturerName implements Comparator <Lecturer> {
	

		@Override
		public int compare(Lecturer l1, Lecturer l2) {
			return l1.getFirstName().toLowerCase().compareTo(l2.getFirstName().toLowerCase());
		}

}
