package com.blogspot.nurkiewicz.jdbcrepository.postgresql;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.blogspot.nurkiewicz.jdbcrepository.postgresql.AbstractJdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

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
