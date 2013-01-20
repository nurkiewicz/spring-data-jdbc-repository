package com.blogspot.nurkiewicz.jdbcrepository.derby;

import com.blogspot.nurkiewicz.jdbcrepository.JdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestDerbyConfig.class)
public class JdbcRepositoryCompoundPkDerbyTest extends JdbcRepositoryCompoundPkTest {
	public JdbcRepositoryCompoundPkDerbyTest() {
		super(-1);
	}
}
