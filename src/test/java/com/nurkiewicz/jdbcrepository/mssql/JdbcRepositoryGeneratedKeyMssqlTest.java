package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssqlConfig.class)
public class JdbcRepositoryGeneratedKeyMssqlTest extends JdbcRepositoryGeneratedKeyTest {

	public JdbcRepositoryGeneratedKeyMssqlTest() {
		super(JdbcRepositoryTestMssqlConfig.MSSQL_PORT);
	}

}
