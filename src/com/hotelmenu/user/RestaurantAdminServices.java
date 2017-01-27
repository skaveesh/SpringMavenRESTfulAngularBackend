package com.hotelmenu.user;

public class RestaurantAdminServices {
	private String restaurantId;
	private String restaurantUname;
	private String restaurantName;
	private String password;
	private String email;
	private String adminFname;
	private String adminLname;
	private String contatctDisplay;
	private String emailDisplay;
	
	
	public String getRestaurantUname() {
		return restaurantUname;
	}
	public void setRestaurantUname(String restaurantUname) {
		this.restaurantUname = restaurantUname;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdminFname() {
		return adminFname;
	}
	public void setAdminFname(String adminFname) {
		this.adminFname = adminFname;
	}
	public String getAdminLname() {
		return adminLname;
	}
	public void setAdminLname(String adminLname) {
		this.adminLname = adminLname;
	}
	public String getContatctDisplay() {
		return contatctDisplay;
	}
	public void setContatctDisplay(String contatctDisplay) {
		this.contatctDisplay = contatctDisplay;
	}
	public String getEmailDisplay() {
		return emailDisplay;
	}
	public void setEmailDisplay(String emailDisplay) {
		this.emailDisplay = emailDisplay;
	}
	public String getRestaurantId() {
		return restaurantId;
	}
	
	
}
