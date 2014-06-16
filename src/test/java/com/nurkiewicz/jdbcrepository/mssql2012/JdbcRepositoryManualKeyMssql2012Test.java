package com.nurkiewicz.jdbcrepository.mssql2012;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMssql2012Config.class)
public class JdbcRepositoryManualKeyMssql2012Test extends JdbcRepositoryManualKeyTest {

	public JdbcRepositoryManualKeyMssql2012Test() {
		super(JdbcRepositoryTestMssql2012Config.MSSQL_PORT);
	}

}
