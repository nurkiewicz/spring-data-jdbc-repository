package com.blogspot.nurkiewicz.jdbcrepository.hsqldb;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestHsqldbConfig.class)
public class AbstractJdbcRepositoryManualKeyHsqldbTest extends AbstractJdbcRepositoryManualKeyTest {
	public AbstractJdbcRepositoryManualKeyHsqldbTest() {
		super(-1);
	}
}
