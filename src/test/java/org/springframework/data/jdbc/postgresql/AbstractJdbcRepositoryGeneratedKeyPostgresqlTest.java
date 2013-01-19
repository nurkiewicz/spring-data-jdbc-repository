package org.springframework.data.jdbc.postgresql;

import org.springframework.data.jdbc.AbstractJdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.springframework.data.jdbc.postgresql.AbstractJdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestPostgresqlConfig.class)
public class AbstractJdbcRepositoryGeneratedKeyPostgresqlTest extends AbstractJdbcRepositoryGeneratedKeyTest {

	public AbstractJdbcRepositoryGeneratedKeyPostgresqlTest() {
		super(POSTGRESQL_PORT);
	}

}
