package sg.edu.iss.caps.appInitialization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.CourseStatus;
import sg.edu.iss.caps.model.Grade;
import sg.edu.iss.caps.model.Lecturer;
import sg.edu.iss.caps.model.Student;
import sg.edu.iss.caps.model.StudentCourse;
import sg.edu.iss.caps.repositories.AdminRepository;
import sg.edu.iss.caps.repositories.CourseRepository;
import sg.edu.iss.caps.repositories.LecturerRepository;
import sg.edu.iss.caps.repositories.StudentCourseRepository;
import sg.edu.iss.caps.repositories.StudentRepository;
import sg.edu.iss.caps.utilities.StringToDateConversion;

@Component
public class AppInitializator {
			
	
	    @Autowired
	    private CourseRepository crepo;
	    
	    @Autowired
	    private LecturerRepository lrepo;
	    
	    @Autowired
	    private AdminRepository arepo;
	    
	    @Autowired
	    private StudentCourseRepository screpo;
	    
	    @Autowired
	    private StudentRepository srepo;
	    
	    @Autowired
	    StringToDateConversion dateConv;
	    
	    private static final Logger LOGGER = Logger.getLogger(AppInitializator.class.getName());
	   	        	    
	    @PostConstruct
	    private void init() {
	    	
	    	LOGGER.info(String.format("\n---------------------Clearing All data from database --------------------------"));
	    	crepo.deleteAll();
	    	srepo.deleteAll();
	        lrepo.deleteAll();
	    	arepo.deleteAll();
	 	    	
	    	/*creating 3 courses and lecturer to pre-load to DB during initialization for joined table
	    	data into the independent tables will be loaded from data.sql file*/
	    	ArrayList<Course> courseList  = new ArrayList<Course>(); 
	    	ArrayList<Lecturer> lecturerList  = new ArrayList<Lecturer>();
	    	ArrayList<Student> studentList  = new ArrayList<Student>();
	    	
	    	//start date and end date for courses
	    	Date startDate = dateConv.StringToDate("2022-08-30");
	    	Date endDate = dateConv.StringToDate("2022-12-30");
	    	
	    	Lecturer lecturer1 = new Lecturer ("achong", "password", "Alan", "Chong", "alan.chong@everbright.edu.sg");		   	
	    	Course course1 = new Course ("Neural Networks and Deep Learning", "A neural network is a network or circuit of biological neurons, or, "
	    			+ "in a modern sense, an artificial neural network, composed of artificial neurons or nodes."
	    			+ " Thus, a neural network is either a biological neural network, made up of biological neurons, or an artificial neural network, "
	    			+ "used for solving artificial intelligence (AI) problems.", 15, 5, startDate, endDate);
	  
	    	Lecturer lecturer2 = new Lecturer ("clong", "password", "TianHai", "Long", "tianhai.long@everbright.edu.sg");		   	
	    	Course course2 = new Course ("Internet of Things", "The Internet of Things (IoT) describes physical objects (or groups of such objects) with sensors, "
	    			+ "processing ability, software, and other technologies that connect and exchange data with other devices "
	    			+ "and systems over the Internet or other communications networks. Internet of things has been "
	    			+ "considered a misnomer because devices do not need to be connected to the public internet, they only "
	    			+ "need to be connected to a network and be individually addressable.", 15, 5, startDate, endDate);
	    	
	    		
	    	Lecturer lecturer3 = new Lecturer ("tsbeng", "password", "SweeBeng", "Toh", "sweebeng.toh@everbright.edu.sg");	       	    	
	    	Course course3 = new Course ("Data Mining", "Data mining is the process of extracting and discovering patterns in large data sets"
	    			+ " involving methods at the intersection of machine learning, statistics, and database systems. Data mining is "
	    			+ "an interdisciplinary subfield of computer science and statistics with an overall goal of extracting information "
	    			+ "(with intelligent methods) from a data set and transforming the information into a comprehensible structure"
	    			+ " for further use.", 15, 5, startDate, endDate);
	    	
	    	courseList.add(course1); courseList.add(course2); courseList.add(course3);
	    	lecturerList.add(lecturer1); lecturerList.add(lecturer2); 	lecturerList.add(lecturer3);
	    	
	    	LOGGER.info(String.format("\n-----------updating cousre table-----------"));
	    	crepo.saveAll(courseList);
	    	LOGGER.info(String.format("\n-----------updating lecturer table-----------"));
	    	lrepo.saveAll(lecturerList);
		    
	    	LOGGER.info(String.format("\n-----------Updatating lecturer_course table-----------"));
	    	for (Lecturer lec : lecturerList) {	
	    		lec.setCourses(courseList);	
		     	lrepo.saveAndFlush(lec);	    		
	    	}
	    	//Updating Student-Course Table by creating some student objects and assigning courses to them
	    		    	
	    	Student student1 = new Student ("bjohnson", "password", "Ben", "Johnson", "ben.johnson@gmail.com");		
	    	Student student2 = new Student ("moen", "password", "Mathew", "Pen", "mathew.pen@everbright.edu.sg");
	    	Student student3 = new Student ("jhtan", "password", "Johnson", "Tan", "johnson.tan@gmail.com");		
	    	Student student4 = new Student ("twong", "password", "Tracy", "Wong", "tracy.wong@gmail.com");		
	    	Student student5 = new Student ("haung", "password", "Helen", "Ang", "helen.ang@gmail.com");		
	    	Student student6 = new Student ("nktan", "password", "Nicole", "Tan", "nikole.tan@gmail.com");		
	    	Student student7 = new Student ("mchma", "password", "Michelle", "Ma", "michelle.ma@gmail.com");		
	    	Student student8 = new Student ("cssim", "password", "ChoonSiong", "Sim", "choosiong.Sim@gmail.com");		
	    	Student student9 = new Student ("dchander", "password", "Deepak", "Chander", "deepak.chander@gmail.com");		
	    	Student student10 = new Student ("slng", "password", "Selena", "Ng", "selena.ng@gmail.com");		
	    	Student student11 = new Student ("nkvimal", "password", "Nikita", "Vimal", "nikita.vimal@gmail.com");	
	    	Student student12 = new Student ("ahahmd", "password", "Ayisha", "Ahammed", "ayisha.ahammed@gmail.com");	
	    	
	    	studentList.add(student1); studentList.add(student2); studentList.add(student3); studentList.add(student4);
	    	studentList.add(student5); studentList.add(student6); studentList.add(student7); studentList.add(student8);
	    	studentList.add(student9); studentList.add(student10); studentList.add(student11); studentList.add(student12);
	    	
	    	LOGGER.info(String.format("\n-----------updating student table-----------"));
	    	srepo.saveAll(studentList);
	    	
	    	LOGGER.info(String.format("\n-----------Updatating student_course table-----------"));
	    	for (Student student : studentList ) {	    		
	    		for (Course course : courseList ) {
	    			StudentCourse studCour = new StudentCourse (student, course, Grade.NA, CourseStatus.ENROLLED); 
	    			screpo.save(studCour);	  	    		
	    		}
	    	}	    	
	    	LOGGER.info(String.format("\n-----------More data for Independant tables will be loaded from data.sql file-----------"));
	    }
	}

