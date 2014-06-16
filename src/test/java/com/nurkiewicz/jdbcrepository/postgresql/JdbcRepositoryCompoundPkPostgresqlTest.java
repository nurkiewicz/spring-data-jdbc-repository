package com.nurkiewicz.jdbcrepository.postgresql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.nurkiewicz.jdbcrepository.postgresql.JdbcRepositoryTestPostgresqlConfig.POSTGRESQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestPostgresqlConfig.class)
public class JdbcRepositoryCompoundPkPostgresqlTest extends JdbcRepositoryCompoundPkTest {
	public JdbcRepositoryCompoundPkPostgresqlTest() {
		super(POSTGRESQL_PORT);
	}
}
