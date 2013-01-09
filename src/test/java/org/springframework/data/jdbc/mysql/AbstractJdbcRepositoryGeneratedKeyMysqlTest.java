package org.springframework.data.jdbc.mysql;

import org.springframework.data.jdbc.AbstractJdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestMysqlConfig.class)
public class AbstractJdbcRepositoryGeneratedKeyMysqlTest extends AbstractJdbcRepositoryGeneratedKeyTest {

	public AbstractJdbcRepositoryGeneratedKeyMysqlTest() {
		super(3306);
	}

}
