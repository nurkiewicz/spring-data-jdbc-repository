
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service("userRepository")
public class UserRepositoryJdbc extends AbstractJdbcRepository<User, String> implements UserRepository {

	OfficeRepository officeRepository;
	
	@Autowired
	public UserRepositoryJdbc(OfficeRepository officeRepository,JdbcTemplate template) {
		super(
			userRowMapper,
			userUpdaterMapper,
			"USER",
			"id",
			template);
		this.officeRepository = officeRepository; 
		
	}

	@Override
	public User findByUsername(String username) {
		return this.findOneByColumn("username",username);
	}

	static RowMapper<User> userRowMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(
				rs.getString("id"),
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("fullName"),
				rs.getString("role"),
				officeRepository.findOne(rs.getString("office_id"));				
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
			mapping.put("office_id",t.getOffice().getId());
		}
	};

}
