package sg.edu.iss.caps.utilities;

import java.beans.PropertyEditorSupport;

public class EmailEditor extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text.contains("@")) {
			setValue(text);
		} else {
			throw new IllegalArgumentException("Email should be in a proper format.");
		}
	}
}
