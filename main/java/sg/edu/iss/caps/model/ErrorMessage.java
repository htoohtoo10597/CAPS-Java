package sg.edu.iss.caps.model;

import lombok.*;

@Data
@NoArgsConstructor
public class ErrorMessage {
	
	private String[] message;

	public ErrorMessage(String[] message) {
		super();
		this.message = message;
	}
}
