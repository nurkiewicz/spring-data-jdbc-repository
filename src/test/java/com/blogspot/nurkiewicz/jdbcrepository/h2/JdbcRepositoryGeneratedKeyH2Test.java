package com.blogspot.nurkiewicz.jdbcrepository.h2;

import com.blogspot.nurkiewicz.jdbcrepository.JdbcRepositoryGeneratedKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestH2Config.class)
public class JdbcRepositoryGeneratedKeyH2Test extends JdbcRepositoryGeneratedKeyTest {
	public JdbcRepositoryGeneratedKeyH2Test() {
		super(-1);
	}
}
