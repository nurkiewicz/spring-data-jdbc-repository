package com.nurkiewicz.jdbcrepository.repositories;

import org.springframework.data.domain.Persistable;

import java.util.Date;

public class User implements Persistable<String> {

	private transient boolean persisted;

	private String userName;

	private Date dateOfBirth;

	private int reputation;

	private boolean enabled;

	public User(String userName, Date dateOfBirth, int reputation, boolean enabled) {
		this.userName = userName;
		this.dateOfBirth = dateOfBirth;
		this.reputation = reputation;
		this.enabled = enabled;
	}

	@Override
	public String getId() {
		return userName;
	}

	@Override
	public boolean isNew() {
		return !persisted;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public User withPersisted(boolean persisted) {
		this.persisted = persisted;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (enabled != user.enabled) return false;
		if (reputation != user.reputation) return false;
		if (dateOfBirth != null ? !dateOfBirth.equals(user.dateOfBirth) : user.dateOfBirth != null) return false;
		return !(userName != null ? !userName.equals(user.userName) : user.userName != null);

	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
		result = 31 * result + reputation;
		result = 31 * result + (enabled ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User(userName='" + userName + '\'' + ", dateOfBirth=" + dateOfBirth + ", reputation=" + reputation + ", enabled=" + enabled + ')';
	}
}
