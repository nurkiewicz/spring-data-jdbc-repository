package org.springframework.data.jdbc;

import org.springframework.data.domain.Persistable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/17/12, 11:12 PM
 */
public class Comment implements Persistable<Integer> {

	private Integer id;

	private String userName;

	private String contents;

	private Date createdTime;

	private int favouriteCount;

	public Comment(String userName, String contents, Date createdTime, int favouriteCount) {
		this.userName = userName;
		this.contents = contents;
		this.createdTime = createdTime;
		this.favouriteCount = favouriteCount;
	}

	public Comment(Integer id, String userName, String contents, Date createdTime, int favouriteCount) {
		this(userName, contents, createdTime, favouriteCount);
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Map<String, Object> toMap() {
		Map<String, Object> mapping = new LinkedHashMap<String, Object>();
		mapping.put("id", getId());
		mapping.put("user_name", getUserName());
		mapping.put("contents", getContents());
		mapping.put("created_time", getCreatedTime());
		mapping.put("favourite_count", getFavouriteCount());
		return mapping;
	}
}
