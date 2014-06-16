package com.nurkiewicz.jdbcrepository.repositories;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/16/13, 10:52 PM
 */
@Repository
public class CommentWithUserRepository extends JdbcRepository<CommentWithUser, Integer> {

	public CommentWithUserRepository(TableDescription table) {
		this(MAPPER, ROW_UNMAPPER, table);
	}

	public CommentWithUserRepository(RowMapper<CommentWithUser> rowMapper, RowUnmapper<CommentWithUser> rowUnmapper, TableDescription table) {
		this(rowMapper, rowUnmapper, null, table);
	}

	public CommentWithUserRepository(RowMapper<CommentWithUser> rowMapper, RowUnmapper<CommentWithUser> rowUnmapper, SqlGenerator sqlGenerator, TableDescription table) {
		super(rowMapper, rowUnmapper, sqlGenerator, table);
	}

	public static final RowMapper<CommentWithUser> MAPPER = new RowMapper<CommentWithUser>() {

		@Override
		public CommentWithUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			final User user = UserRepository.MAPPER.mapRow(rs, rowNum);
			return new CommentWithUser(
					rs.getInt("id"),
					user,
					rs.getString("contents"),
					rs.getTimestamp("created_time"),
					rs.getInt("favourite_count")
			);
		}
	};

	private static final RowUnmapper<CommentWithUser> ROW_UNMAPPER = new RowUnmapper<CommentWithUser>() {
		@Override
		public Map<String, Object> mapColumns(CommentWithUser comment) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", comment.getId());
			mapping.put("user_name", comment.getUser().getUserName());
			mapping.put("contents", comment.getContents());
			mapping.put("created_time", new java.sql.Timestamp(comment.getCreatedTime().getTime()));
			mapping.put("favourite_count", comment.getFavouriteCount());
			return mapping;
		}
	};

	@Override
	protected <S extends CommentWithUser> S postCreate(S entity, Number generatedId) {
		entity.setId(generatedId.intValue());
		return entity;
	}
}
