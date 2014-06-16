package com.nurkiewicz.jdbcrepository.repositories;

import org.springframework.data.domain.Persistable;

import java.util.Date;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/16/13, 10:51 PM
 */
public class CommentWithUser extends Comment implements Persistable<Integer> {

	private User user;

	public CommentWithUser(User user, String contents, Date createdTime, int favouriteCount) {
		super(user.getUserName(), contents, createdTime, favouriteCount);
		this.user = user;
	}

	public CommentWithUser(Integer id, User user, String contents, Date createdTime, int favouriteCount) {
		super(id, user.getUserName(), contents, createdTime, favouriteCount);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CommentWithUser)) return false;
		if (!super.equals(o)) return false;

		CommentWithUser that = (CommentWithUser) o;
		return user.equals(that.user);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + user.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "CommentWithUser(id=" + getId() + ", user=" + user +  ", contents='" + getContents() + '\'' + ", createdTime=" + getCreatedTime() + ", favouriteCount=" + getFavouriteCount() + ')';
	}

}
