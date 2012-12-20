package org.springframework.data.jdbc;

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
		super(MAPPER, ROW_UNMAPPER, "COMMENT");
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

	private static final RowUnmapper<Comment> ROW_UNMAPPER = new RowUnmapper<Comment>() {
		@Override
		public Map<String, Object> mapColumns(Comment comment) {
			return comment.toMap();
		}
	};

	@Override
	protected Comment postCreate(Comment entity, Number generatedId) {
		entity.setId(generatedId.intValue());
		return entity;
	}
}
