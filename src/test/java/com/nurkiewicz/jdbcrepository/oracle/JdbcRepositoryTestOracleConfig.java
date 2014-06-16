package com.nurkiewicz.jdbcrepository.oracle;

import com.jolbox.bonecp.BoneCPDataSource;
import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.nurkiewicz.jdbcrepository.repositories.UserRepository;
import com.nurkiewicz.jdbcrepository.sql.OracleSqlGenerator;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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
		BoneCPDataSource ds = new BoneCPDataSource();
		ds.setDriverClass("oracle.jdbc.OracleDriver");
		final String host = System.getProperty("oracle.hostname", "localhost");
		final String service = System.getProperty("oracle.sid", "orcl");
		final String url = " jdbc:oracle:thin:@//" + host + ":" + ORACLE_PORT + "/" + service;
		ds.setJdbcUrl(url);
		ds.setUsername(System.getProperty("oracle.username"));
		ds.setPassword(System.getProperty("oracle.password"));
		return ds;
	}

}
