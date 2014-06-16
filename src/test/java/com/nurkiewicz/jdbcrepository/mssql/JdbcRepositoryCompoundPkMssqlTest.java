package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssqlConfig.class)
public class JdbcRepositoryCompoundPkMssqlTest extends JdbcRepositoryCompoundPkTest {

	public JdbcRepositoryCompoundPkMssqlTest() {
		super(JdbcRepositoryTestMssqlConfig.MSSQL_PORT);
	}
}
