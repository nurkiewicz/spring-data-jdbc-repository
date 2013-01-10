package org.springframework.data.jdbc.hsqldb;

import org.springframework.data.jdbc.AbstractJdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestHsqldbConfig.class)
public class AbstractJdbcRepositoryGeneratedKeyHsqldbTest extends AbstractJdbcRepositoryGeneratedKeyTest {
	public AbstractJdbcRepositoryGeneratedKeyHsqldbTest() {
		super(-1);
	}
}
