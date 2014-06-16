package com.nurkiewicz.jdbcrepository.postgresql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.nurkiewicz.jdbcrepository.postgresql.JdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestPostgresqlConfig.class)
public class JdbcRepositoryManyToOnePostgresqlTest extends JdbcRepositoryManyToOneTest {
	public JdbcRepositoryManyToOnePostgresqlTest() {
		super(POSTGRESQL_PORT);
	}
}
