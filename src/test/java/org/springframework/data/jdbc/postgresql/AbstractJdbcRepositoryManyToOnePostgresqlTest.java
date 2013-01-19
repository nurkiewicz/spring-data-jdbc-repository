package org.springframework.data.jdbc.postgresql;

import org.springframework.data.jdbc.AbstractJdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.springframework.data.jdbc.postgresql.AbstractJdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestPostgresqlConfig.class)
public class AbstractJdbcRepositoryManyToOnePostgresqlTest extends AbstractJdbcRepositoryManyToOneTest {
	public AbstractJdbcRepositoryManyToOnePostgresqlTest() {
		super(POSTGRESQL_PORT);
	}
}
