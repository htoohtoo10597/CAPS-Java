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

import sg.edu.iss.caps.exceptions.DuplicateException;
import sg.edu.iss.caps.model.AppPage;
import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.CourseStatus;
import sg.edu.iss.caps.model.ErrorMessage;
import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.LoginBag;
import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.services.CourseService;
import sg.edu.iss.caps.services.StudentCourseService;
import sg.edu.iss.caps.services.StudentService;
import sg.edu.iss.caps.utilities.CalculateGPA;
import sg.edu.iss.caps.utilities.RegistrationUtil;
import sg.edu.iss.caps.utilities.SortByCourseName;
import sg.edu.iss.caps.utilities.SortByStudCourseName;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private CourseService courseServ;

	@Autowired
	private StudentCourseService studCourseServ;

	@Autowired
	private StudentService studServ;
	
	@Autowired
	private CalculateGPA cgpa;
	
	@Autowired
	private LoginController loginCon;

	@Autowired
	private RegistrationUtil regUtil;

	@GetMapping("/courseList")
	public String showCourseList(Model model, HttpSession session) {
		LoginBag user = (LoginBag) session.getAttribute("loggeduser");
		List<Course> courseList = courseServ.getAllCourse();
		Collections.sort(courseList, new SortByCourseName());
		model.addAttribute("listCourses", courseList);
		model.addAttribute("student", studServ.getStudentById(user.getLoggeduser().getUserId()));
		model.addAttribute("studRegisteredCourses", courseServ.findCoursesByStudId(user.getLoggeduser().getUserId()));
		
		loginCon.checkCurrentPage(model, AppPage.STUDENT_ENROLL);
		
		return "studentcourselist";
	}

	@PostMapping("/searchCourseList")
	public String searchCourse(Model model, @Param("search") String search, HttpSession session) {
		LoginBag user = (LoginBag) session.getAttribute("loggeduser");
		model.addAttribute("listCourses", courseServ.getAllCourse());
		List<Course> courseList = courseServ.findCoursesByName(search);
		Collections.sort(courseList, new SortByCourseName());
		model.addAttribute("listCourses", courseList);
		model.addAttribute("student", studServ.getStudentById(user.getLoggeduser().getUserId()));
		model.addAttribute("studRegisteredCourses", courseServ.findCoursesByStudId(user.getLoggeduser().getUserId()));
		loginCon.checkCurrentPage(model, AppPage.STUDENT_ENROLL);
		
		return "studentcourselist";
	}

	@GetMapping("/registerCourse/{studentId}/{courseId}")
	public String regCourse(Model model, @PathVariable("courseId") String courseId, @PathVariable("studentId") String studentId) throws DuplicateException {
		List<Course> studentRegCourses = courseServ.findCoursesByStudId(studentId);
		if (!studentRegCourses.contains(courseServ.getCourseById(courseId)) && courseServ.isCapacityOk(courseId) && !regUtil.overlaps(studentRegCourses, courseServ.getCourseById(courseId))) {
			StudentCourse sc = new StudentCourse(studServ.getStudentById(studentId), courseServ.getCourseById(courseId), Grade.NA, CourseStatus.ENROLLED);
			studCourseServ.newStudentCourse(sc);
		} else if (studentRegCourses.contains(courseServ.getCourseById(courseId)) && courseServ.isCapacityOk(courseId)) {
			throw new DuplicateException(String.format("\n\n\n ErrorRegistrationFailed: Student is already enrolled in \"%s\"\n\n", courseServ.getCourseById(courseId).getCourseName()));
		} else if (!studentRegCourses.contains(courseServ.getCourseById(courseId)) && !courseServ.isCapacityOk(courseId)) {
			throw new DuplicateException(String.format("\n\n\n ErrorRegistrationFailed: Course \"%s\" is fully booked\n\n", courseServ.getCourseById(courseId).getCourseName()));
		} 
		// if the course that the student is trying to register overlaps with another course, the controller will redirect to the error page
		else if (regUtil.overlaps(studentRegCourses, courseServ.getCourseById(courseId))) {
			model.addAttribute("courseName", courseServ.getCourseById(courseId).getCourseName());
			return "erroroverlap";
		}
		return "redirect:/student/courseList";
	}

	@GetMapping("/myCourses")
	public String myCourses(HttpSession session, Model model) {
		LoginBag user = (LoginBag) session.getAttribute("loggeduser");
		List<StudentCourse> studCourseList = studServ.findStudCoursesByStudId(user.getLoggeduser().getUserId());
		Collections.sort(studCourseList, new SortByStudCourseName());
		model.addAttribute("studentCourses", studCourseList);
		
		
		//check if coming from other account
		if (session.getAttribute("errorMsg") != null) {
			ErrorMessage errorMsg = (ErrorMessage) session.getAttribute("errorMsg");
			model.addAttribute("LoginError", errorMsg);
			session.removeAttribute("errorMsg");
		}
		
		
		if (studCourseList.isEmpty()) {
			model.addAttribute("gpa", "Not available");
		}else {
			model.addAttribute("gpa", cgpa.calculateGpa(user.getLoggeduser().getUserId(), studCourseList));
		}
		
		loginCon.checkCurrentPage(model, AppPage.STUDENT_MY_COURSES);
		return "studentcourses";
	}
}
