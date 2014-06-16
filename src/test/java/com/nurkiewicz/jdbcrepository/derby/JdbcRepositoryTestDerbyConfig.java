package com.nurkiewicz.jdbcrepository.derby;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.repositories.Comment;
import com.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUser;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUserRepository;
import com.nurkiewicz.jdbcrepository.sql.DerbySqlGenerator;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestDerbyConfig extends JdbcRepositoryTestConfig {

	@Override
	public CommentRepository commentRepository() {
		return new CommentRepository(CommentRepository.MAPPER,
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

	@Override
	public CommentWithUserRepository commentWithUserRepository() {
		return new CommentWithUserRepository(CommentWithUserRepository.MAPPER,
				new RowUnmapper<CommentWithUser>() {
					@Override
					public Map<String, Object> mapColumns(CommentWithUser comment) {
						Map<String, Object> mapping = new LinkedHashMap<String, Object>();
						mapping.put("ID", comment.getId());
						mapping.put("USER_NAME", comment.getUser().getUserName());
						mapping.put("CONTENTS", comment.getContents());
						mapping.put("CREATED_TIME", new Timestamp(comment.getCreatedTime().getTime()));
						mapping.put("FAVOURITE_COUNT", comment.getFavouriteCount());
						return mapping;
					}
				},
				new CommentWithUserDerbySqlGenerator(),
				new TableDescription("COMMENTS", "COMMENTS c JOIN USERS u ON c.USER_NAME = u.USER_NAME", "ID")
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
