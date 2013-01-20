package org.springframework.data.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repositories.BoardingPassRepository;
import org.springframework.data.jdbc.repositories.CommentRepository;
import org.springframework.data.jdbc.repositories.CommentWithUserRepository;
import org.springframework.data.jdbc.repositories.UserRepository;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public abstract class AbstractJdbcRepositoryTestConfig {

	@Bean
	public abstract DataSource dataSource();

	@Bean
	public CommentRepository commentRepository() {
		return new CommentRepository("COMMENTS");
	}

	@Bean
	public UserRepository userRepository() {
		return new UserRepository("USERS");
	}

	@Bean
	public BoardingPassRepository boardingPassRepository() {
		return new BoardingPassRepository();
	}

	@Bean
	public CommentWithUserRepository commentWithUserRepository() {
		return new CommentWithUserRepository(new TableDescription("COMMENTS", "COMMENTS JOIN USERS ON COMMENTS.user_name = USERS.user_name", "id"));
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}
