package com.nurkiewicz.jdbcrepository.h2;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryCompoundPkTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JdbcRepositoryTestH2Config.class)
public class JdbcRepositoryCompoundPkH2Test extends JdbcRepositoryCompoundPkTest {
}
