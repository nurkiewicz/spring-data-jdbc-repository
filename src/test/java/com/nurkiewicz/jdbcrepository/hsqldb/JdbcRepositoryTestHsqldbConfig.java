package com.nurkiewicz.jdbcrepository.hsqldb;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestHsqldbConfig extends JdbcRepositoryTestConfig {

	@Bean
	@Override
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().
				addScript("schema_hsqldb.sql").
				setType(EmbeddedDatabaseType.H2).
				build();
	}

}
