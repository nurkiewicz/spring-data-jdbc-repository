package com.nurkiewicz.jdbcrepository.mysql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.nurkiewicz.jdbcrepository.mysql.JdbcRepositoryTestMysqlConfig.MYSQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestMysqlConfig.class)
public class JdbcRepositoryManualKeyMysqlTest extends JdbcRepositoryManualKeyTest {

	public JdbcRepositoryManualKeyMysqlTest() {
		super(MYSQL_PORT);
	}

}
