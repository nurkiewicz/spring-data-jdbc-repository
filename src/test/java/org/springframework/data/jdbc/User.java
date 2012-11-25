package org.springframework.data.jdbc;

import org.springframework.data.domain.Persistable;

public class User implements Persistable<String> {

	private String id;

	private String userName;
	private String password;
	
	private String fullName;
	
	private String role;

	protected void setUserName(String userName) {
		this.userName = userName;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected void setFullName(String fullName) {
		this.fullName = fullName;
	}

	protected void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	public String getRole() {
		return role;
	}

	public User(
			String id,
			String userName,
			String password, 
			String fullName,
			String role) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
