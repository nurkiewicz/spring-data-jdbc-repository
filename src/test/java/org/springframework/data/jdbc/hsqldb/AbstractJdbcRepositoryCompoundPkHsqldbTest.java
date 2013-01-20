package org.springframework.data.jdbc.hsqldb;

import org.springframework.data.jdbc.AbstractJdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestHsqldbConfig.class)
public class AbstractJdbcRepositoryCompoundPkHsqldbTest extends AbstractJdbcRepositoryCompoundPkTest {
	public AbstractJdbcRepositoryCompoundPkHsqldbTest() {
		super(-1);
	}
}
