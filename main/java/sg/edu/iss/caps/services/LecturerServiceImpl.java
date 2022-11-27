package sg.edu.iss.caps.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.caps.model.Lecturer;
import sg.edu.iss.caps.repositories.LecturerRepository;


@Service
public class LecturerServiceImpl implements LecturerService {

	@Autowired
	private LecturerRepository lecrepo;

	@Override
	public List<Lecturer> getAllLecturers() {
		// TODO Auto-generated method stub
		return lecrepo.findAll();
	}

	@Override
	public void saveLecturer(Lecturer lec) {
		// TODO Auto-generated method stub
		lecrepo.save(lec);
		
	}

	@Override
	public Lecturer getLecturerById(String id) {
		// TODO Auto-generated method stub
		Optional<Lecturer> optional = lecrepo.findById(id);
		Lecturer lec = null;
		
		if(optional.isPresent()){
			lec = optional.get();
		} else {
			throw new RuntimeException("Lecturer " + " not found.");
		}
			
		return lec;
	}
	@Override
	public void deleteLecturerById(String id) {
		// TODO Auto-generated method stub
		lecrepo.deleteById(id);
	}

	@Override
	public List<Lecturer> returnLecturerByName(String name) {
		// TODO Auto-generated method stub
		return lecrepo.searchLecturerByName(name);
	}
//	@Override
//	public void assignLecturerToCourse(Lecturer lec,String courseid) {
//		// TODO Auto-generated method stub
//		lecrepo.save(lec);
//		
//	}
//	@Override
//	public void unassignLecturerFromCourse(String courseid, String lecturerid) {
//		// TODO Auto-generated method stub
//		lecrepo.deleteById(lecturerid);
//	}

	
	
	
	@Override
	public List<Lecturer> returnLecturerByCredentials(String username, String password) {
		return lecrepo.searchLecturerByCredentials(username, password);
	}
	
	/*
	 * @Override public Page<Lecturer> findPaginated(int pageNo, int pageSize) { //
	 * TODO Auto-generated method stub
	 * 
	 * Pageable pageable = PageRequest.of(pageNo - 1, pageSize); return
	 * this.lecrepo.findAll(pageable);
	 * 
	 * }
	 */


}
