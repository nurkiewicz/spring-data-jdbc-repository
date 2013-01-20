package com.blogspot.nurkiewicz.jdbcrepository.h2;

import com.blogspot.nurkiewicz.jdbcrepository.AbstractJdbcRepositoryManyToOneTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestH2Config.class)
public class AbstractJdbcRepositoryManyToOneH2Test extends AbstractJdbcRepositoryManyToOneTest {
	public AbstractJdbcRepositoryManyToOneH2Test() {
		super(-1);
	}
}
