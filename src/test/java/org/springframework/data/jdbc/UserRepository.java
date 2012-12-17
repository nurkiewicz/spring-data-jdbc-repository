package org.springframework.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class UserRepository extends AbstractJdbcRepository<User, String> {

	@Autowired
	public UserRepository(JdbcOperations operations) {
		super(
				USER_ROW_MAPPER,
				USER_UPDATER,
				"USER",
				"user_name",
				operations);
	}

	private static RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(
					rs.getString("user_name"),
					rs.getTimestamp("date_of_birth"),
					rs.getInt("reputation"),
					rs.getBoolean("enabled")
			).withPersisted(true);
		}
	};

	private static Updater<User> USER_UPDATER = new Updater<User>() {
		@Override
		public void mapColumns(User t, Map<String, Object> mapping) {
			mapping.put("user_name", t.getUserName());
			mapping.put("date_of_birth", t.getDateOfBirth());
			mapping.put("reputation", t.getReputation());
			mapping.put("enabled", t.isEnabled());
		}
	};

	@Override
	protected User postUpdate(User entity) {
		return entity.withPersisted(true);
	}

	@Override
	protected User postCreate(User entity) {
		return entity.withPersisted(true);
	}
}
