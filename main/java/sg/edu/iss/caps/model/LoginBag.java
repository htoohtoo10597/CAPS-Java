package sg.edu.iss.caps.model;

import lombok.*;

@Data
@NoArgsConstructor
public class LoginBag {
	
	private LoginUser loggeduser;

	public LoginBag(LoginUser loggeduser) {
		super();
		this.loggeduser = loggeduser;
	}
	
	
}
