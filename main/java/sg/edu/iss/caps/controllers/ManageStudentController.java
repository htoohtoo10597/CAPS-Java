package sg.edu.iss.caps.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.iss.caps.model.Student;
import sg.edu.iss.caps.repositories.StudentRepository;
import sg.edu.iss.caps.services.StudentService;

@RestController
@RequestMapping("/api/managestudents")
public class ManageStudentController {
	@Autowired
	private StudentService studserv;
	
	@Autowired
	private StudentRepository studrepo;
	
	@Transactional
	@GetMapping("/page/{pageNo}")
	public HashMap<String, Object> findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = 5;
		
		Page<Student> page = studserv.findPaginated(pageNo, pageSize);
		
		List<Student> listStudents = page.getContent();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("listStudents", listStudents);
		data.put("currentPage",pageNo);
		data.put("totalPages", page.getTotalPages());
		data.put("totalItems", page.getTotalElements());
		
		return data;
	}
	
	@RequestMapping("/update/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") String studId,
			@RequestBody Student studentDetails){
		
		Optional<Student> optional = studrepo.findById(studId);
		Student student = null;

		if (optional.isPresent()){
			student = optional.get();
		} else {
			throw new RuntimeException("Student not found.");
		}
		
		student.setFirstName(studentDetails.getFirstName());
		student.setLastName(studentDetails.getEmail());
		student.setEmail(studentDetails.getEmail());
		Student newStud = studrepo.save(student);
		
		return ResponseEntity.ok (newStud);
	}
	
}
