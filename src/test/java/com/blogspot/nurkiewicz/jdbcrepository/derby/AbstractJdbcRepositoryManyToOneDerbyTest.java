package com.blogspot.nurkiewicz.jdbcrepository.derby;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestDerbyConfig.class)
public class AbstractJdbcRepositoryManyToOneDerbyTest extends AbstractJdbcRepositoryManyToOneTest {
	public AbstractJdbcRepositoryManyToOneDerbyTest() {
		super(-1);
	}
}
