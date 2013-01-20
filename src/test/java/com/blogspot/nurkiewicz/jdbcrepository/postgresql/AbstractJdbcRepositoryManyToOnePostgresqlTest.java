package com.blogspot.nurkiewicz.jdbcrepository.postgresql;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.blogspot.nurkiewicz.jdbcrepository.postgresql.AbstractJdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

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
