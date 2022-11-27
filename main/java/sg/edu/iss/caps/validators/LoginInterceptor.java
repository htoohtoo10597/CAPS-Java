package sg.edu.iss.caps.validators;



import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.iss.caps.model.ErrorMessage;
import sg.edu.iss.caps.model.LoginBag;

public class LoginInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
      throws Exception {
    	LoginBag currentUser = (LoginBag) request.getSession().getAttribute("loggeduser");
    	String path = request.getRequestURI();
    	String[] splitPath = path.split("/");
    	
    	if (currentUser == null) {
    		logger.info("No user found");
    		return;
    	}
    	
    	if (splitPath.length > 1) {
        	List<String> roles = Arrays.asList("admin","student","lecturer");
        	 if (roles.contains(splitPath[1])) {
        		 String requestRole = splitPath[1];
        		 String currUserRole = currentUser.getLoggeduser().getRole().toString();
        		 
        		 if (!requestRole.equalsIgnoreCase(currUserRole)){
        			logger.info("we aint");
        			
        			
        			String[] errmsg = {"You tried accessing another page using an account with a different role. Logout before accessing that page."};
        			ErrorMessage errorMsg = new ErrorMessage(errmsg);
        			request.getSession().setAttribute("errorMsg", errorMsg);
        			
        				
        			 switch(currUserRole.toLowerCase()) {
        			 	case "admin":
        			 		response.sendRedirect("/admin/home");
        			 		break;
        			 		
        			 	case "student":
        			 		response.sendRedirect("/student/myCourses");
        			 		break;
        			 		
        			 	case "lecturer":
        			 		response.sendRedirect("/lecturer/view-courses");
        			 		break;
        			 
        			 }

        		 }
        		 
        	 }
        	
    	}


    }

}
