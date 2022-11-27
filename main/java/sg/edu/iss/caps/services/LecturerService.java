package sg.edu.iss.caps.services;

import java.util.List;

import sg.edu.iss.caps.model.Lecturer;



public interface LecturerService {
	
	List<Lecturer> getAllLecturers();
	
	//saving the employee object in the database
	void saveLecturer(Lecturer lec);
	
	//fetching one employee using the id
	Lecturer getLecturerById(String id);
	
	//deleting the employee in the database
	void deleteLecturerById(String id);
	
	//returns a list of employees that were searched by name
	List<Lecturer> returnLecturerByName(String name);
	
	List<Lecturer> returnLecturerByCredentials(String username, String password);
	
	//Page<Lecturer> findPaginated(int pageNo, int pageSize);
}
