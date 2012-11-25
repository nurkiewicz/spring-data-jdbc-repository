package org.springframework.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRepository extends AbstractJdbcRepository<User, String> {

	@Autowired
	public UserRepository(JdbcTemplate template) {
		super(
			userRowMapper,
			userUpdaterMapper,
			"USER",
			"id",
			template);
	}

	static RowMapper<User> userRowMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(
				rs.getString("id"),
				rs.getString("userName"),
				rs.getString("password"),
				rs.getString("fullName"),
				rs.getString("role"));				
		}
	};
	
	static Updater<User> userUpdaterMapper = new Updater<User>() {
		@Override
		public void mapColumns(User t, Map<String, Object> mapping) {
			mapping.put("id",t.getId());
			mapping.put("userName",t.getUserName());
			mapping.put("password",t.getPassword());
			mapping.put("fullName",t.getFullName());
			mapping.put("role",t.getRole());
		}
	};

}
