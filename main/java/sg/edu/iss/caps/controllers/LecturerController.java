package sg.edu.iss.caps.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.iss.caps.model.AppPage;
import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.CourseStatus;
import sg.edu.iss.caps.model.ErrorMessage;
import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.LoginBag;
import sg.edu.iss.caps.model.Student;
import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.repositories.StudentCourseRepository;
import sg.edu.iss.caps.services.CourseService;
import sg.edu.iss.caps.services.StudentCourseService;
import sg.edu.iss.caps.services.StudentService;
import sg.edu.iss.caps.utilities.CalculateGPA;
import sg.edu.iss.caps.utilities.SortByCourseName;
import sg.edu.iss.caps.utilities.SortByStudentName;


@Controller
@RequestMapping("/lecturer")
@SessionAttributes("loggeduser")
public class LecturerController {

	@Autowired
	private CourseService corserv;
	
	@Autowired
	private StudentService stuserv;
	
	@Autowired
	private CalculateGPA cgpa;
	
	@Autowired
	private LoginController loginCon;

	@Autowired
	private StudentCourseService studCorServ;
	
	@Autowired
	private StudentCourseRepository studCorRepo;

	//View all courses that a lecturer teach
	@RequestMapping("/view-courses")
	public String viewLecCourses(
			Model model, HttpSession session) {
		
		String userId  = getCurrentUserId(session);
		
		//To get the courses of currently logged in lecturer
		List<Course> courses = corserv.getCoursesByLecturerId(userId);
		Collections.sort(courses, new SortByCourseName());
		
		//check if coming from other account
		if (session.getAttribute("errorMsg") != null) {
			ErrorMessage errorMsg = (ErrorMessage) session.getAttribute("errorMsg");
			model.addAttribute("LoginError", errorMsg);
			session.removeAttribute("errorMsg");
		}
		
		//Addcourses to listCourses for display
		model.addAttribute("listCourses",  courses);
		
		loginCon.checkCurrentPage(model, AppPage.LECTURER_VIEW_COURSES);
																							
		return "leccourses";		
	}	
	
	//Search a course from the list courses a lecturer teach
	@PostMapping("/search-courses")
	public String searchCourses(@Param("name") String name, Model model,
			HttpSession session) {
		
		String userid  = getCurrentUserId(session);
		//find courses by Course name and lecturer if searched with non empty string
		if (name != "") {
			List<Course> courses = corserv.findByLecturerAndCourse(userid, name);		
			Collections.sort(courses, new SortByCourseName());
		model.addAttribute("listCourses", courses);
		loginCon.checkCurrentPage(model, AppPage.LECTURER_VIEW_COURSES);
		
		return "leccourses";
		}
		//list all courses for the lecturer if searched with empty string
		else 
			return "redirect:/lecturer/view-courses";
	}
	
	//Returns the userId of the currently logged in user
	public String getCurrentUserId (HttpSession session){
		
		LoginBag lb =(LoginBag) session.getAttribute("loggeduser");
		String userId = lb.getLoggeduser().getUserId();
		
		return userId;
		
	}
	
	//View students currently taking a course
	@RequestMapping("/view-course-student-list/{id}")
	public String viewCourseStudentView(@PathVariable(value = "id") String courseId, Model model) {
		
		List<Student> students = stuserv.getCurrentStudentsByCourse(courseId);
		Collections.sort(students, new SortByStudentName());
		Course curCourse = corserv.getCourseById(courseId);
		model.addAttribute("curCourse", curCourse);		
		model.addAttribute("listStudents", students);	
		
		loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
			
		return "viewcoursestudent"; 
	}
	
	//Searching students currently taking a course
	@PostMapping("/search-students/{id}")
	public String searchStudents(@PathVariable(value = "id") String courseId, @Param("name") String name, Model model,
			HttpSession session) {
		
		String userid  = getCurrentUserId(session);
		//if searched with non empty string
		if (name != "") {
						
			List<Student> students = stuserv.searchCourseStudentByName(courseId, name);		
			Course curCourse = corserv.getCourseById(courseId);			
			model.addAttribute("curCourse", curCourse);	
			Collections.sort(students, new SortByStudentName());
		    model.addAttribute("listStudents", students);
		    
			loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
		
		return "viewcoursestudent";
		}
		else 
			return "redirect:/lecturer/view-course-student-list/"+courseId;
	}
	
	//View student perfomance 
	@RequestMapping("/view-perfomance/{id}")
	public String viewStudentPerfomance(@PathVariable(value = "id") String studentId, Model model) {
				
		List<StudentCourse> studCourseList = stuserv.findStudCoursesByStudId(studentId);
		
		if (!studCourseList.isEmpty()) {
		double gpa = cgpa.calculateGpa(studentId, studCourseList);		
		model.addAttribute("studentCourses", studCourseList);
		 model.addAttribute("gpa", gpa);		  
		 
			loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
		 
		return "viewstudentprofile";
		}
		else {			
			Student student = stuserv.getStudentById(studentId);
			model.addAttribute("student", student);
		}
		
		loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
		
		return "nostudentcourses";	
		
	}
	
	//Search a student to view perfomance
	@RequestMapping("/search-students")	
	public String searchAllStudents(Model model) {				  
		
		loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
		
		return "searchstudents"; 		
	}
	
	//Search results
	@RequestMapping("/search-results")		
	public String showStudentSearchResults(@Param(value= "name") String name, Model model) {		
		List<Student> students = stuserv.returnStudentByName(name);		
		Collections.sort(students, new SortByStudentName());
	    model.addAttribute("listStudents", students);		
	    
		loginCon.checkCurrentPage(model, AppPage.LECTURER_STUDENT_PERF);
	    
		return "searchstudentresults"; 		
		
	}		
	
		@GetMapping("/assign-grade/{courseId}")
		public String assignGrades(@PathVariable(value = "courseId") String courseId, Model model, HttpSession session) {			
		
		Course course = corserv.getCourseById(courseId);
		List<Student> currStudents = stuserv.getCurrentStudentsByCourse(courseId);		
		
		model.addAttribute("course", course);
		model.addAttribute("listStudents", currStudents);
	
		return "assigngrade";
	}
		
		//saving the assigned grades
		@PostMapping("/save-grades/{courseId}")		
		public String assignGrades(@PathVariable(value = "courseId") String courseId, 
				@Param("selectedGrade") String selectedGrade,	@Param("selectedStudent")  String selectedStudent, 
				Model model) {
			
			StudentCourse studentCourse = studCorServ.getStudentCourseByStudentAndCourse(selectedStudent, courseId);			
			Grade grade = Grade.valueOf(selectedGrade);			
			studentCourse.setGrade((grade));			
			//if the student passed
			if (grade !=Grade.F) {
			studentCourse.setCourseStatus(CourseStatus.COMPLETED);
			}
			//if the student failed
			else {
				studentCourse.setCourseStatus(CourseStatus.FAILED);
			}			
			studCorRepo.save(studentCourse);			
		return "redirect:/lecturer/assign-grade/"+courseId;
	}

}
