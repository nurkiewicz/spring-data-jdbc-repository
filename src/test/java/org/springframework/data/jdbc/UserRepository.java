package org.springframework.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractJdbcRepository<User, String> {

	public UserRepository() {
		super(MAPPER, UPDATER, "USER", "user_name");
	}

	private static final RowMapper<User> MAPPER = new RowMapper<User>() {
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

	private static final Updater<User> UPDATER = new Updater<User>() {
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
