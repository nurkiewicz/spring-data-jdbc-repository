package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssqlConfig.class)
public class JdbcRepositoryManualKeyMssqlTest extends JdbcRepositoryManualKeyTest {

	public JdbcRepositoryManualKeyMssqlTest() {
		super(JdbcRepositoryTestMssqlConfig.MSSQL_PORT);
	}

}
