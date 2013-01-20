package com.blogspot.nurkiewicz.jdbcrepository.postgresql;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.blogspot.nurkiewicz.jdbcrepository.postgresql.AbstractJdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestPostgresqlConfig.class)
public class AbstractJdbcRepositoryManualKeyPostgresqlTest extends AbstractJdbcRepositoryManualKeyTest {

	public AbstractJdbcRepositoryManualKeyPostgresqlTest() {
		super(POSTGRESQL_PORT);
	}

}
