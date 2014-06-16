package com.nurkiewicz.jdbcrepository.derby;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestDerbyConfig.class)
public class JdbcRepositoryManualKeyDerbyTest extends JdbcRepositoryManualKeyTest {
	public JdbcRepositoryManualKeyDerbyTest() {
		super(-1);
	}
}
