package org.springframework.data.jdbc.mysql;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.AbstractJdbcRepositoryTestConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
public class AbstractJdbcRepositoryTestMysqlConfig extends AbstractJdbcRepositoryTestConfig {

	@Bean
	@Override
	public DataSource dataSource() {
		MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
		ds.setUser("root");
		ds.setPassword("");
		ds.setDatabaseName("spring_data_jdbc_repository_test");
		return ds;
	}

}
