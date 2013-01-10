package org.springframework.data.jdbc.postgresql;

import org.postgresql.jdbc2.optional.PoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.AbstractJdbcRepositoryTestConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestPostgresqlConfig extends AbstractJdbcRepositoryTestConfig {

	@Bean
	@Override
	public DataSource dataSource() {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUser("postgres");
		ds.setDatabaseName("spring_data_jdbc_repository_test");
		return ds;
	}

}
