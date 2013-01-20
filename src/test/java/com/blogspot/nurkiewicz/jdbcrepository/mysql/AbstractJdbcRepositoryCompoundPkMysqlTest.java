package com.blogspot.nurkiewicz.jdbcrepository.mysql;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.blogspot.nurkiewicz.jdbcrepository.mysql.AbstractJdbcRepositoryTestMysqlConfig.MYSQL_PORT;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestMysqlConfig.class)
public class AbstractJdbcRepositoryCompoundPkMysqlTest extends AbstractJdbcRepositoryCompoundPkTest {
	public AbstractJdbcRepositoryCompoundPkMysqlTest() {
		super(MYSQL_PORT);
	}
}
