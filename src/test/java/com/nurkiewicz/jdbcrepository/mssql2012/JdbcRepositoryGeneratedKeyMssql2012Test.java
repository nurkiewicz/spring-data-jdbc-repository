package com.nurkiewicz.jdbcrepository.mssql2012;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssql2012Config.class)
public class JdbcRepositoryGeneratedKeyMssql2012Test extends JdbcRepositoryGeneratedKeyTest {

	public JdbcRepositoryGeneratedKeyMssql2012Test() {
		super(JdbcRepositoryTestMssql2012Config.MSSQL_PORT);
	}

}
