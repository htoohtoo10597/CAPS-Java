package sg.edu.iss.caps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sg.edu.iss.caps.exceptions.DuplicateException;
import sg.edu.iss.caps.model.*;
import sg.edu.iss.caps.repositories.StudentCourseRepository;
import sg.edu.iss.caps.services.CourseService;
import sg.edu.iss.caps.services.LecturerService;
import sg.edu.iss.caps.services.StudentCourseService;
import sg.edu.iss.caps.services.StudentService;
import sg.edu.iss.caps.utilities.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.registerCustomEditor(Lecturer.class, "email", new EmailEditor());
		binder.registerCustomEditor(Student.class, "email", new EmailEditor());
	}
	
	@Autowired
	private LoginController loginCon;
	
	/*
	 * REPOSITORIES TO INJECT STARTS HERE
	 */

	@Autowired
	private LecturerService lecserv;
	
	@Autowired
	private CourseService couserv;
	
	@Autowired
	private StudentService studserv;
	
	@Autowired
	private StudentCourseService studCourseServ;

	@Autowired
	private RegistrationUtil regUtil;

	@Autowired
	private StudentCourseRepository screpo;
	
	
	/*
	 * REPOSITORIES TO INJECT ENDS HERE
	 */
	
	@RequestMapping("/home")
	public String openHomePage (Model model, HttpSession sess) {
		model.addAttribute("lecCount", lecserv.getAllLecturers().size());
		model.addAttribute("courseCount", couserv.getAllCourse().size());
		model.addAttribute("studCount", studserv.getAllStudents().size());
		
		if (sess.getAttribute("errorMsg") != null) {
			ErrorMessage errorMsg = (ErrorMessage) sess.getAttribute("errorMsg");
			model.addAttribute("LoginError", errorMsg);
			sess.removeAttribute("errorMsg");
		}
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_HOME);
		return "adminhome";
	}
	
	/*
	 * LECTURER METHODS START HERE
	 */
	
	@RequestMapping("/manage-lecturers")
	public String viewManageLecturers(Model model) {
		List<Lecturer> listLecturers = lecserv.getAllLecturers();
		//Collections.sort(listLecturers, new SortByLecturerName());
		model.addAttribute("listLecturers", listLecturers);
	
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "managelecturers";
	}
	
	@GetMapping("/add-new-lecturer")
	public String newLecturer(Model model) {
		Lecturer lecturer = new Lecturer();
		
		model.addAttribute("lecturer", lecturer);
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "newLecturer";
	}
	
	@PostMapping("/save-lecturer")
	public String saveLecturer(@ModelAttribute("lecturer") @Valid Lecturer lecturer, BindingResult result, 
			Model model, HttpServletRequest request){
		
		if (result.hasErrors()) {
			String referer = request.getHeader("Referer");
			System.out.println(referer);
		    return "redirect:" + referer;
		}
		
		lecserv.saveLecturer(lecturer);
		return "redirect:/admin/manage-lecturers";
	}
	

	@GetMapping("/update-lecturer/{id}")
	public String updateLecturer(@PathVariable(value = "id") String id, Model model) {
		
		Lecturer lecturer = lecserv.getLecturerById(id);
		
		System.out.println("This code works");
		
		model.addAttribute("lecturer", lecturer);
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "updateLecturer";
	}
	
	@GetMapping("/delete-lecturer/{id}")
	public String deleteLecturer(@PathVariable(value = "id") String id, Model model) {
		
		lecserv.deleteLecturerById(id);
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		return "redirect:/admin/manage-lecturers";
	}
	
	@PostMapping("/search-lecturers")
	public String searchLecturers(@Param("name") String name, Model model) {
		List<Lecturer> listLecturers = lecserv.returnLecturerByName(name);
		
		model.addAttribute("listLecturers", listLecturers);
		
		//loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "managelecturers";
	}
	
	@RequestMapping("/view-lecturer-courses/{id}")
	public String viewLecturerCourses(@PathVariable(value = "id") String id, Model model) {
		
		Lecturer lecturer = lecserv.getLecturerById(id);
		
		List<Course> listCourse = lecturer.getCourses();
		
		model.addAttribute("listCourse", listCourse);
		model.addAttribute("lecturer", lecturer);
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "viewLecturerCourses";
	}
	
	
	@GetMapping("/save-lecturer-to-course/{lec-id}/{course-id}")
	public String saveLecturerToCourse(@PathVariable(value = "lec-id") String lecId, 
			@PathVariable(value = "course-id") String courseId) {
		
		Lecturer lec = lecserv.getLecturerById(lecId);
		Course course = couserv.getCourseById(courseId);
		
		lec.getCourses().add(course);
		lecserv.saveLecturer(lec);
		
		return "redirect:/admin/view-lecturer-courses/" + lec.getId();
	}
	
	@GetMapping("/delete-course-from-lecturer/{lec-id}/{course-id}")
	public String removeCourseFromLecturer(@PathVariable(value = "lec-id") String lecId, 
			@PathVariable(value = "course-id") String courseId) {
		
		Lecturer lec = lecserv.getLecturerById(lecId);
		Course course = couserv.getCourseById(courseId);
		
		lec.getCourses().remove(course);
		lecserv.saveLecturer(lec);
		
		return "redirect:/admin/view-lecturer-courses/" + lec.getId();
	}
	
	
	
	@GetMapping("/add-course-to-lecturer/{id}")
	public String addCourseToLecturer(Model model, 
			@PathVariable(value = "id") String lecId) {
		
		Lecturer lecturer = lecserv.getLecturerById(lecId);
		
		List<Course> allCourses = couserv.getAllCourse();
		
		List<Course> listLecCourse = allCourses
			.stream()
			.filter(x -> x.getLecturers().contains(lecturer))
			.collect(Collectors.toList());
		
		
		model.addAttribute("allCourses", allCourses);
		model.addAttribute("listLecCourse", listLecCourse);
		model.addAttribute("lecturer", lecturer);
		
		loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		return "addCourseToLecturer";
	}
	
	/*
	 * @GetMapping("/manage-lecturers/page/{pageNo}") public String
	 * findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) { int
	 * pageSize = 10;
	 * 
	 * Page<Lecturer> page = lecserv.findPaginated(pageNo, pageSize);
	 * 
	 * List<Lecturer> listLecturers = page.getContent();
	 * 
	 * model.addAttribute("currentPage",pageNo); model.addAttribute("totalPages",
	 * page.getTotalPages()); model.addAttribute("totalItems",
	 * page.getTotalElements()); model.addAttribute("listLecturers", listLecturers);
	 * loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
	 * 
	 * return "managelecturers"; }
	 * 
	 * @GetMapping("/search-lecturers/page/{pageNo}") public String
	 * findPaginatedSearch(@PathVariable(value = "pageNo") int pageNo, Model model)
	 * { int pageSize = 10;
	 * 
	 * Page<Lecturer> page = lecserv.findPaginated(pageNo, pageSize);
	 * 
	 * List<Lecturer> listLecturers = page.getContent();
	 * 
	 * model.addAttribute("currentPage",pageNo); model.addAttribute("totalPages",
	 * page.getTotalPages()); model.addAttribute("totalItems",
	 * page.getTotalElements()); model.addAttribute("listLecturers", listLecturers);
	 * loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
	 * 
	 * return "managelecturers"; }
	 */
	
	/*
	 * LECTURER METHODS END HERE
	 */
	
	
	//COURSE METHODS STARTS HERE
	@RequestMapping("/manage-course")
	public String viewCoursePage(Model model, HttpSession session) {
		List<Course> courseList = couserv.getAllCourse();
		Collections.sort(courseList, new SortByCourseName());
		model.addAttribute("listCourse", courseList);
		
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
		return "manageCourse";
	}
	
	@GetMapping("/add-new-course")
	public String newCourse(Model model, HttpSession session) {
		Course course = new Course();
		model.addAttribute("course", course);
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
		return "newupdatecourse";
	}
	
	@PostMapping("/search-courses")
	public String searchCourses(@Param("name") String name, Model model) {
		List<Course> listCourse = couserv.findCoursesByName(name);
		Collections.sort(listCourse, new SortByCourseName());
		model.addAttribute("listCourse", listCourse);
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
		return "manageCourse";
	}
	
	@PostMapping("/save-course")
	public String saveCourse(@ModelAttribute("course") @Valid Course course, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
			return "newupdatecourse";
		}
		
		couserv.saveCourse(course);
		return "redirect:/admin/manage-course";
	}
	
	@GetMapping("/edit-course/{id}")
	public String editCourseById(@PathVariable(value = "id") String id, Model model) {
		Course course = couserv.getCourseById(id);
		model.addAttribute("course", course);
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
		return "newupdatecourse";
	}
	
	@GetMapping("/delete-course/{id}")
	public String removeCourse(@PathVariable(value = "id") String id, Model model) {
		couserv.deleteCourseById(id);
		return "redirect:/admin/manage-course";
	}
	//COURSE METHODS ENDS HERE
	
	//ENROLLMENT METHODS STARTS HERE
	@GetMapping("/view-student-courses/{courseId}")
	public String viewStudCourses(@PathVariable("courseId") String courseId, Model model) {
		List<Student> listStudent = studserv.getAllStudents();
		Collections.sort(listStudent, new SortByStudentName());
		model.addAttribute("listStudent", listStudent);
		model.addAttribute("listExistStudent", studserv.getStudentByCourse(courseId));
		List<StudentCourse> listStudCourse = couserv.getStudCoursesByCourseId(courseId);
		listStudCourse.sort(new SortByStudCourseStudName());
		model.addAttribute("listStudCourse", listStudCourse);
		model.addAttribute("course", couserv.getCourseById(courseId));
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_COURSES);
		return "manageenrollment";
	}
	
	@PostMapping("/enroll-student")
	public String enrollStudent(@Param("selectedStudentId") String selectedStudentId, @Param("courseId") String courseId, Model model) throws DuplicateException {
		List<Course> studentRegCourses = couserv.findCoursesByStudId(selectedStudentId);
		if (couserv.isCapacityOk(courseId) && !regUtil.overlaps(studentRegCourses, couserv.getCourseById(courseId))) {
			StudentCourse sc = new StudentCourse(studserv.getStudentById(selectedStudentId), couserv.getCourseById(courseId), Grade.NA, CourseStatus.ENROLLED);
			studCourseServ.newStudentCourse(sc);
		} else {
			model.addAttribute("courseName", couserv.getCourseById(courseId).getCourseName());
			return "erroroverlap";
		}
		return "redirect:/admin/view-student-courses/"+courseId;
	}

	@PostMapping("/update-status")
	public String UpdateStatus(@Param("courseStatus") String courseStatus, @Param("courseId") String courseId) {
		if (courseStatus.equals("ENROLLED")){
			List<StudentCourse> list = couserv.getStudCoursesByCourseId(courseId);
			for (StudentCourse sc : list){
				sc.setCourseStatus(CourseStatus.ENROLLED);
				screpo.saveAndFlush(sc);
			}
		}else if (courseStatus.equals("COMPLETED")){
			List<StudentCourse> list = couserv.getStudCoursesByCourseId(courseId);
			for (StudentCourse sc : list){
				sc.setCourseStatus(CourseStatus.COMPLETED);
				screpo.saveAndFlush(sc);
			}
		}else if (courseStatus.equals("FAILED")){
			List<StudentCourse> list = couserv.getStudCoursesByCourseId(courseId);
			for (StudentCourse sc : list){
				sc.setCourseStatus(CourseStatus.FAILED);
				screpo.saveAndFlush(sc);
			}
		}
		return "redirect:/admin/view-student-courses/"+courseId;
	}

	@GetMapping("/status-enrolled/{courseId}/{studCourId}")
	public String ChangeStatusEnrolled(@PathVariable("studCourId") String studCourId, @PathVariable("courseId") String courseId){
		StudentCourse sc = studCourseServ.getStudentCourseById(studCourId);
		sc.setCourseStatus(CourseStatus.ENROLLED);
		screpo.saveAndFlush(sc);
		return "redirect:/admin/view-student-courses/"+courseId;
	}

	@GetMapping("/status-completed/{courseId}/{studCourId}")
	public String ChangeStatusCompleted(@PathVariable("studCourId") String studCourId, @PathVariable("courseId") String courseId){
		StudentCourse sc = studCourseServ.getStudentCourseById(studCourId);
		sc.setCourseStatus(CourseStatus.COMPLETED);
		screpo.saveAndFlush(sc);
		return "redirect:/admin/view-student-courses/"+courseId;
	}

	@GetMapping("/status-failed/{courseId}/{studCourId}")
	public String ChangeStatusFailed(@PathVariable("studCourId") String studCourId, @PathVariable("courseId") String courseId){
		StudentCourse sc = studCourseServ.getStudentCourseById(studCourId);
		sc.setCourseStatus(CourseStatus.FAILED);
		screpo.saveAndFlush(sc);
		return "redirect:/admin/view-student-courses/"+courseId;
	}

	
//	@PostMapping("/search-enrollstudent")
//	public String searchEnrollStudent(@Param("courseId") String courseId, @Param("name") String name, Model model) {
//		model.addAttribute("listStudent", studServ.getAllStudents());
//		model.addAttribute("listExistStudent", studServ.getStudentByCourse(courseId));
//		model.addAttribute("listStudCourse", listStudCourse);
//		model.addAttribute("course", couserv.getCourseById(courseId));
//		return "manageenrollment";
//	}
	
	@GetMapping("/deleteEnrollment/{courseId}/{studCourseId}")
	public String deleteEnrollment(@PathVariable("studCourseId") String studCourseId, @PathVariable("courseId") String courseId) {
		studCourseServ.removeStudentCourseById(studCourseId);
		return "redirect:/admin/view-student-courses/"+courseId;
	}
	
	//ENROLLMENT METHODS ENDS HERE
	
	
	
	
	
//	@PostMapping("/assign-lecturer")
//	public String assignLecturer(@ModelAttribute("Course") Course course, Lecturer lecturer) {
//		lecserv.assignLecturerToCourse(lecturer, course.getId());
//		
//		return "redirect:/admin/manage-course";
//	}
//	
//	@GetMapping("/unassign-lecturer")
//	public String unassignLecturerFromCourse(@ModelAttribute("Course") Course course, Lecturer lecturer) {
//		
//		lecserv.unassignLecturerFromCourse(course.getId(),lecturer.getId());
//		
//		return "redirect:/admin/manage-course";
//	}
	
	
	//
	// MANAGE STUDENTS STARTS HERE
	//


	//ok
	@RequestMapping("/manage-students")
	public String viewStudentPage(Model model) {
		
		List<Student> listStudents = studserv.getAllStudents();
		Collections.sort(listStudents, new SortByStudentName());
		model.addAttribute("listStudents", listStudents);
	
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_STUDENTS);
		return "managestudents";
	}
	
	//ok
	@GetMapping("/add-new-student")
	public String newStudent(Model model) {
		Student student = new Student();
		
		model.addAttribute("student", student);
		
		//loginCon.setAdminRole(model, new LoginUser(Role.ADMIN));
		//loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_LECTURERS);
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_STUDENTS);
		return "newstudent";
	}

	//ok
	@PostMapping("/save-student")
	public String saveStudent(@ModelAttribute("student") Student student) {
		studserv.saveStudent(student);
		
	  
		//redirect: -> sends user to another page (in this case, the home page)
		return "redirect:/admin/manage-students"; 
	}
	  

	@GetMapping("/update-student/{id}")
	public String updateStudent(@PathVariable(value = "id") String id, Model model) {
		Student student = studserv.getStudentById(id);  
		model.addAttribute("student", student);
		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_STUDENTS);
		return "updatestudent";
	}
	  
	  
	  
	//ok
	@GetMapping("/delete-student/{id}") 
	public String deleteStudent (@PathVariable(value = "id") String id, Model model) {
	  
		studserv.deleteStudentById(id);
	  
		return "redirect:/admin/manage-students"; 
	}
	  
	
	/*
	 * public String searchStudent(@Param("name") String name, Model model) {
	 * List<Student> listStudents = studserv.returnStudentByName(name);
	 * model.addAttribute("listStudents", listStudents); return "index"; }
	 */
	  
	  
	@PostMapping("/search-students")
	public String searchStudents(@Param("name") String name, Model model) {
		List<Student> listStudents = studserv.returnStudentByName(name);
			
		model.addAttribute("listStudents", listStudents);
			

		loginCon.checkCurrentPage(model, AppPage.ADMIN_MANAGE_STUDENTS);
		return "managestudents";

	}
	  
	  
	  
	 
	 
}
