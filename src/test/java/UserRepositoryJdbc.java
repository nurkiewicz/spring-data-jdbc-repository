
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service("userRepository")
public class UserRepositoryJdbc extends AbstractJdbcRepository<User, String> {

	@Autowired
	public UserRepositoryJdbc(JdbcTemplate template) {
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
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("fullName"),
				rs.getString("role"));				
		}
	};
	
	static Updater<User> userUpdaterMapper = new Updater<User>() {
		@Override
		public void mapColumns(User t, Map<String, Object> mapping) {
			mapping.put("id",t.getId());
			mapping.put("username",t.getUsername());
			mapping.put("password",t.getPassword());
			mapping.put("fullName",t.getFullname());
			mapping.put("role",t.getRole());
		}
	};

}
