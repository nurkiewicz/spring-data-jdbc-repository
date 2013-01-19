package org.springframework.data.jdbc.mysql;

import org.springframework.data.jdbc.AbstractJdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestMysqlConfig.class)
public class AbstractJdbcRepositoryManyToOneMysqlTest extends AbstractJdbcRepositoryManyToOneTest {
	public AbstractJdbcRepositoryManyToOneMysqlTest() {
		super(-1);
	}
}
