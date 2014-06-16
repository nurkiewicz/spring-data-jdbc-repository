package com.nurkiewicz.jdbcrepository.oracle;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestOracleConfig.class)
public class JdbcRepositoryManualKeyOracleTest extends JdbcRepositoryManualKeyTest {

	public JdbcRepositoryManualKeyOracleTest() {
		super(JdbcRepositoryTestOracleConfig.ORACLE_PORT);
	}

}
