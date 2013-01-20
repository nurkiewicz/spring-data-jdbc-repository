package com.blogspot.nurkiewicz.jdbcrepository.mysql;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryTestConfig;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestMysqlConfig extends AbstractJdbcRepositoryTestConfig {

	public static final int MYSQL_PORT = 3306;

	@Bean
	@Override
	public DataSource dataSource() {
		MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
		ds.setUser("root");
		ds.setPassword(System.getProperty("mysql.password", ""));
		ds.setDatabaseName("spring_data_jdbc_repository_test");
		return ds;
	}

}
