package org.springframework.data.jdbc;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
		public Map<String, Object> mapColumns(User t) {
			return ImmutableMap.<String, Object>of(
					"user_name", t.getUserName(),
					"date_of_birth", t.getDateOfBirth(),
					"reputation", t.getReputation(),
					"enabled", t.isEnabled());
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
