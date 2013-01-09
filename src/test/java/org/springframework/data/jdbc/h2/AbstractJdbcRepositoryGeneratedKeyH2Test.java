package org.springframework.data.jdbc.h2;

import org.springframework.data.jdbc.AbstractJdbcRepositoryGeneratedKeyTest;
import org.springframework.data.jdbc.AbstractJdbcRepositoryTestH2Config;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:20 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestH2Config.class)
public class AbstractJdbcRepositoryGeneratedKeyH2Test extends AbstractJdbcRepositoryGeneratedKeyTest {
}
