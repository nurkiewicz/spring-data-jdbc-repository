package com.nurkiewicz.jdbcrepository.postgresql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.nurkiewicz.jdbcrepository.postgresql.JdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestPostgresqlConfig.class)
public class JdbcRepositoryManualKeyPostgresqlTest extends JdbcRepositoryManualKeyTest {

	public JdbcRepositoryManualKeyPostgresqlTest() {
		super(POSTGRESQL_PORT);
	}

}
