package sg.edu.iss.caps.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.iss.caps.model.*;
import sg.edu.iss.caps.services.*;

@Controller
@RequestMapping("/log")
public class LoginController {

	
	@Autowired
	private LecturerService lecserv;
	
	@Autowired
	private StudentService studserv;
	
	@Autowired
	private AdminService adminserv;
	
	
	@GetMapping("/login")
	public String loginUser(@ModelAttribute("user") LoginUser user,
			BindingResult bindingResult, 
			Model model,
			HttpSession session) 
	{
		if (bindingResult.hasErrors()) {
			
			String[] errmsg = {"Please enter a valid username or password."};
			ErrorMessage errorMsg = new ErrorMessage(errmsg);
			System.out.println(errorMsg);
			model.addAttribute("LoginError", errorMsg);
			return "home";
		}
				
		System.out.println("Search Admin");
		//check where the user is from (admin/student/lecturer)
		List<Admin> adminList = adminserv.returnAdminByCredentials(user.getUsername(),
				user.getPassword());
				
		if (adminList.size() == 1) {
			Admin admin = adminList.get(0);
			System.out.println(admin.getFirstName());
			user.setRole(Role.ADMIN);
			user.setUserId(admin.getId());
			user.setName(admin.getFirstName());
			LoginBag lb = new LoginBag(user);
			session.setAttribute("loggeduser", lb);

			return "redirect:/admin/home";
		}
		System.out.println("No Admin");
		
		System.out.println("Search Student");
		//if user is student
		List<Student> studentList = studserv.returnStudentByCredentials(user.getUsername(),
				user.getPassword());
				
		if (studentList.size() == 1) {

			Student student = studentList.get(0);
			System.out.println(student.getFirstName());
			user.setRole(Role.STUDENT);
			user.setUserId(student.getId());
			user.setName(student.getFirstName());
			LoginBag lb = new LoginBag(user);
			session.setAttribute("loggeduser", lb);

			return "redirect:/student/myCourses";
		}
		System.out.println("No Student");
		
		System.out.println("Search Lecturer");
		//if user is lecturer
		List<Lecturer> lecList = lecserv.returnLecturerByCredentials(user.getUsername(),
				user.getPassword());

	
		if (lecList.size() == 1) {

			Lecturer lec = lecList.get(0);
			System.out.println(lec.getFirstName());
			user.setRole(Role.LECTURER);
			user.setUserId(lec.getId());
			user.setName(lec.getFirstName());
			LoginBag lb = new LoginBag(user);
			session.setAttribute("loggeduser", lb);

			return "redirect:/lecturer/view-courses";
		}
		System.out.println("No Lecturer");
		
		String[] errmsg = {"Please enter a valid username or password."};
		ErrorMessage errorMsg = new ErrorMessage(errmsg);
		System.out.println(errorMsg);
		model.addAttribute("LoginError", errorMsg);
		return "home";
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	public void setAdminRole(Model model, LoginUser user) {
		user.setRole(Role.ADMIN);
		model.addAttribute("user", user);
	}
	
	public void setStudentRole(Model model, LoginUser user) {
		user.setRole(Role.STUDENT);
		model.addAttribute("user", user);
	}
	
	public void setLecturerRole(Model model, LoginUser user) {
		user.setRole(Role.LECTURER);
		model.addAttribute("user", user);
	}
	
	public void setLogoutRole(Model model) {
		model.addAttribute("user", new LoginUser(Role.TO_LOGIN));
	}
	
	public void checkCurrentPage(Model model, AppPage currModule) {
		model.addAttribute("currentMod", currModule);
	}
}
