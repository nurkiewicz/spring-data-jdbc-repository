package com.nurkiewicz.jdbcrepository.h2;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestH2Config extends JdbcRepositoryTestConfig {

	@Bean
	@Override
	public DataSource dataSource() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:mem:DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema_h2.sql'");
		return ds;
	}

}
