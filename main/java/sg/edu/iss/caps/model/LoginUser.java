package sg.edu.iss.caps.model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class LoginUser {
	
	private String userId;
	private String username;
	private String password;
	private String name;
	private Role role;
	
	public LoginUser(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public LoginUser(Role role) {
		super();
		this.role = role;
	}
	
	
}
