package org.springframework.data.jdbc;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestConfig.class)
@Transactional
public class AbstractJdbcRepositoryTest {

	@Resource
	private UserRepository userRepository;

	@Test
	public void shouldReturnNullWhenDatabaseEmptyAndSearchingById() {
		//given
		String NOT_EXISTING_ID = "Foo";

		//when
		User user = userRepository.findOne(NOT_EXISTING_ID);

		//then
		assertThat(user).isNull();
	}

}
