package org.springframework.data.jdbc.h2;

import org.springframework.data.jdbc.AbstractJdbcRepositoryManualKeyTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/9/13, 10:19 PM
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestH2Config.class)
public class AbstractJdbcRepositoryManualKeyH2Test extends AbstractJdbcRepositoryManualKeyTest {
	public AbstractJdbcRepositoryManualKeyH2Test() {
		super(-1);
	}
}
