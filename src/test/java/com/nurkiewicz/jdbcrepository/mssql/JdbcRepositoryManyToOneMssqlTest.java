package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssqlConfig.class)
public class JdbcRepositoryManyToOneMssqlTest extends JdbcRepositoryManyToOneTest {

	public JdbcRepositoryManyToOneMssqlTest() {
		super(JdbcRepositoryTestMssqlConfig.MSSQL_PORT);
	}
}
