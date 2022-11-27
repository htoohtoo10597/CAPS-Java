package sg.edu.iss.caps.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;


@Service
public class StringToDateConversion {

	public Date StringToDate(String s) {

	    Date result = null;
	    try{
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        result  = dateFormat.parse(s);
	    }
	    catch(ParseException e){
	        e.printStackTrace();
	    }
	    return result ;
	}
	
}
