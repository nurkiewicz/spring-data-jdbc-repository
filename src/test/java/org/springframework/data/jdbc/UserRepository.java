package org.springframework.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

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
			userKeyGenerator,
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

	static KeyGenerator<User> userKeyGenerator = new KeyGenerator<User>() {
		@Override
		public User newKey(User t) {
			String id = UUID.randomUUID().toString();
			t.setId(id); // since the Persistable interface doesn't have a setId you must call setId within the key generator.
						 // another option, if your objects are read-only, is to return a new copy of the object with ID populated.
			return t;
		}
	};

}
