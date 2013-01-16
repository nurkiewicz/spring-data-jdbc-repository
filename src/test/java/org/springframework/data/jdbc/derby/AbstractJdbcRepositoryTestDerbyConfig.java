package org.springframework.data.jdbc.derby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.AbstractJdbcRepositoryTestConfig;
import org.springframework.data.jdbc.RowUnmapper;
import org.springframework.data.jdbc.repositories.Comment;
import org.springframework.data.jdbc.repositories.CommentRepository;
import org.springframework.data.jdbc.sql.DerbySqlGenerator;
import org.springframework.data.jdbc.sql.SqlGenerator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestDerbyConfig extends AbstractJdbcRepositoryTestConfig {

	@Override
	public CommentRepository commentRepository() {
		return new CommentRepository(
				new RowMapper<Comment>() {
					@Override
					public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Comment(
								rs.getInt("ID"),
								rs.getString("USER_NAME"),
								rs.getString("CONTENTS"),
								rs.getTimestamp("CREATED_TIME"),
								rs.getInt("FAVOURITE_COUNT")
						);
					}
				},
				new RowUnmapper<Comment>() {
					@Override
					public Map<String, Object> mapColumns(Comment comment) {
						Map<String, Object> mapping = new LinkedHashMap<String, Object>();
						mapping.put("ID", comment.getId());
						mapping.put("USER_NAME", comment.getUserName());
						mapping.put("CONTENTS", comment.getContents());
						mapping.put("CREATED_TIME", new java.sql.Timestamp(comment.getCreatedTime().getTime()));
						mapping.put("FAVOURITE_COUNT", comment.getFavouriteCount());
						return mapping;
					}
				},
				"COMMENTS",
				"ID"
		);
	}

	@Bean
	public SqlGenerator sqlGenerator() {
		return new DerbySqlGenerator();
	}

	@Bean
	@Override
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().
				addScript("schema_derby.sql").
				setType(EmbeddedDatabaseType.DERBY).
				build();
	}

}
