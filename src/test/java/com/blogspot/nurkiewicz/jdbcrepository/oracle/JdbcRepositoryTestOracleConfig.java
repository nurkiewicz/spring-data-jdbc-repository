package com.blogspot.nurkiewicz.jdbcrepository.oracle;

import com.blogspot.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.UserRepository;
import com.blogspot.nurkiewicz.jdbcrepository.sql.OracleSqlGenerator;
import com.blogspot.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestOracleConfig extends JdbcRepositoryTestConfig {

	public static final int ORACLE_PORT = Integer.parseInt(System.getProperty("oracle.port", "1521"));

	@Bean
	@Override
	public CommentRepository commentRepository() {
		return new CommentRepository("comments");
	}

	@Bean
	@Override
	public UserRepository userRepository() {
		return new UserRepository("users");
	}

	@Override
	public BoardingPassRepository boardingPassRepository() {
		return new BoardingPassRepository("boarding_pass");
	}

	@Bean
	public SqlGenerator sqlGenerator() {
		return new OracleSqlGenerator();
	}

	@Bean
	@Override
	public DataSource dataSource() {
		OracleDataSource ds = null;
		try {
			ds = new OracleDataSource();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		ds.setUser(System.getProperty("oracle.username"));
		ds.setPortNumber(ORACLE_PORT);
		ds.setPassword(System.getProperty("oracle.password"));
		ds.setServerName(System.getProperty("oracle.hostname", "localhost"));
		ds.setDatabaseName(System.getProperty("oracle.sid", "orcl"));
		ds.setDriverType(System.getProperty("oracle.driver_type", "thin"));
		return ds;
	}

}
