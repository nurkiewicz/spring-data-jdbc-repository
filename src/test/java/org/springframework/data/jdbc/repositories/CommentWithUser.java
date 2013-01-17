package org.springframework.data.jdbc.repositories;

import org.springframework.data.domain.Persistable;

import java.util.Date;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/16/13, 10:51 PM
 */
public class CommentWithUser implements Persistable<Integer> {

	private Integer id;

	private User user;

	private String contents;

	private Date createdTime;

	private int favouriteCount;

	public CommentWithUser(User user, String contents, Date createdTime, int favouriteCount) {
		this.user = user;
		this.contents = contents;
		this.createdTime = createdTime;
		this.favouriteCount = favouriteCount;
	}

	public CommentWithUser(Integer id, User user, String contents, Date createdTime, int favouriteCount) {
		this(user, contents, createdTime, favouriteCount);
		this.id = id;
	}


	@Override
	public boolean isNew() {
		return id == null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getFavouriteCount() {
		return favouriteCount;
	}

	public void setFavouriteCount(int favouriteCount) {
		this.favouriteCount = favouriteCount;
	}
}
