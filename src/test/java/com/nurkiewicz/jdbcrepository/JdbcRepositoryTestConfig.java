package com.nurkiewicz.jdbcrepository;

import com.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUserRepository;
import com.nurkiewicz.jdbcrepository.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public abstract class JdbcRepositoryTestConfig {

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
