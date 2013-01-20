package com.blogspot.nurkiewicz.jdbcrepository.h2;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryTestConfig;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestH2Config extends AbstractJdbcRepositoryTestConfig {

	@Bean
	@Override
	public DataSource dataSource() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:mem:DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema_h2.sql'");
		return ds;
	}

}
