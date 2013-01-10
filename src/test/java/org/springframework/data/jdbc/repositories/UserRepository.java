package org.springframework.data.jdbc.repositories;

import org.springframework.data.jdbc.AbstractJdbcRepository;
import org.springframework.data.jdbc.RowUnmapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class UserRepository extends AbstractJdbcRepository<User, String> {

	public UserRepository() {
		super(MAPPER, ROW_UNMAPPER, "USERS", "user_name");
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

	private static final RowUnmapper<User> ROW_UNMAPPER = new RowUnmapper<User>() {
		@Override
		public Map<String, Object> mapColumns(User t) {
			final LinkedHashMap<String, Object> columns = new LinkedHashMap<String, Object>();
			columns.put("user_name", t.getUserName());
			columns.put("date_of_birth", new Date(t.getDateOfBirth().getTime()));
			columns.put("reputation", t.getReputation());
			columns.put("enabled", t.isEnabled());
			return columns;
		}
	};

	@Override
	protected User postUpdate(User entity) {
		return entity.withPersisted(true);
	}

	@Override
	protected User postCreate(User entity, Number generatedId) {
		return entity.withPersisted(true);
	}
}
