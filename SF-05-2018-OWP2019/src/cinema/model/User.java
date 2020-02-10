package cinema.model;

import java.time.LocalDate;

public class User {
	
	public enum UserRole{USER, ADMIN}
	
	private String username;
	private String password;
	private LocalDate registrationDate;
	private UserRole userRole;
	private boolean active;
	private boolean loggedIn;
	
	public User () {}

	public User(String username, String password, LocalDate registrationDate, 
					UserRole userRole, boolean active, boolean loggedIn) {
		this.username = username;
		this.password = password;
		this.registrationDate = registrationDate;
		this.userRole = userRole;
		this.active = active;
		this.loggedIn = loggedIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

}
