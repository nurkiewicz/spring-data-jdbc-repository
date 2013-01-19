package org.springframework.data.jdbc.mysql;

import org.springframework.data.jdbc.AbstractJdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.springframework.data.jdbc.mysql.AbstractJdbcRepositoryTestMysqlConfig.MYSQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestMysqlConfig.class)
public class AbstractJdbcRepositoryManualKeyMysqlTest extends AbstractJdbcRepositoryManualKeyTest {

	public AbstractJdbcRepositoryManualKeyMysqlTest() {
		super(MYSQL_PORT);
	}

}
