package com.nurkiewicz.jdbcrepository.mysql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.nurkiewicz.jdbcrepository.mysql.JdbcRepositoryTestMysqlConfig.MYSQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMysqlConfig.class)
public class JdbcRepositoryGeneratedKeyMysqlTest extends JdbcRepositoryGeneratedKeyTest {

	public JdbcRepositoryGeneratedKeyMysqlTest() {
		super(MYSQL_PORT);
	}

}
