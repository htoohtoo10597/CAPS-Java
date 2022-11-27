package sg.edu.iss.caps.exceptions;

@SuppressWarnings("serial")
public class DuplicateException extends Exception {
	public DuplicateException() {
		super();
	}
	
	public DuplicateException(String message) {
		super(message);
	}
}
