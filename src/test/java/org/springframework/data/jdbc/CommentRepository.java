package org.springframework.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/17/12, 11:15 PM
 */
@Repository
public class CommentRepository extends AbstractJdbcRepository<Comment, Integer> {

	public CommentRepository() {
		super(MAPPER, UPDATER, "COMMENT");
	}

	private static final RowMapper<Comment> MAPPER = new RowMapper<Comment>() {

		@Override
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Comment(
					rs.getInt("id"),
					rs.getString("user_name"),
					rs.getString("contents"),
					rs.getTimestamp("created_time"),
					rs.getInt("favourite_count")
			);
		}
	};

	private static final Updater<Comment> UPDATER = new Updater<Comment>() {
		@Override
		public void mapColumns(Comment comment, Map<String, Object> mapping) {
			mapping.put("id", comment.getId());
			mapping.put("user_name", comment.getUserName());
			mapping.put("contents", comment.getContents());
			mapping.put("created_time", comment.getCreatedTime());
			mapping.put("favourite_count", comment.getFavouriteCount());
		}
	};
}
