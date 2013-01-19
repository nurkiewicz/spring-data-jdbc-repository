package org.springframework.data.jdbc.derby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.AbstractJdbcRepositoryTestConfig;
import org.springframework.data.jdbc.RowUnmapper;
import org.springframework.data.jdbc.TableDescription;
import org.springframework.data.jdbc.repositories.Comment;
import org.springframework.data.jdbc.repositories.CommentRepository;
import org.springframework.data.jdbc.repositories.CommentWithUser;
import org.springframework.data.jdbc.repositories.CommentWithUserRepository;
import org.springframework.data.jdbc.sql.DerbySqlGenerator;
import org.springframework.data.jdbc.sql.SqlGenerator;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestDerbyConfig extends AbstractJdbcRepositoryTestConfig {

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
