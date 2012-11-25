package org.springframework.data.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Arrays;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestConfig.class)
@Transactional
public class AbstractJdbcRepositoryTest {

	@Resource
	private UserRepository userRepository;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Test
	public void shouldReturnNullWhenDatabaseEmptyAndSearchingById() {
		//given
		String NOT_EXISTING_ID = "Foo";

		//when
		User user = userRepository.findOne(NOT_EXISTING_ID);

		//then
		assertThat(user).isNull();
	}

	@Test
	public void shouldReturnEmptyListWhenDatabaseEmpty() {
		//given

		//when
		Iterable<User> all = userRepository.findAll();

		//then
		assertThat(all).isEmpty();
	}

	@Test
	public void shouldReturnEmptyPageWhenNoEntitiesInDatabase() {
		//given

		//when
		Page<User> firstPage = userRepository.findAll(new PageRequest(0, 20));

		//then
		assertThat(firstPage).isEmpty();
		assertThat(firstPage.getTotalElements()).isZero();
		assertThat(firstPage.getSize()).isEqualTo(20);
		assertThat(firstPage.getNumber()).isZero();
	}

	@Test
	public void shouldReturnOneRecordById() {
		//given
		jdbcTemplate.update("INSERT INTO USER VALUES (?, ?, ?, ?, ?)", "john", "johnsmith", "John Smith", "secret", "USER");

		//when
		User john = userRepository.findOne("john");

		//then
		assertThat(john).isNotNull();
		assertThat(john.getId()).isEqualTo("john");
		assertThat(john.getUserName()).isEqualTo("johnsmith");
		assertThat(john.getFullName()).isEqualTo("John Smith");
		assertThat(john.getPassword()).isEqualTo("secret");
		assertThat(john.getRole()).isEqualTo("USER");
	}

	@Test
	public void shouldReturnListWithOneItemWhenOneRecordInDatabase() {
		//given
		jdbcTemplate.update("INSERT INTO USER VALUES (?, ?, ?, ?, ?)", "john2", "johnsmith", "John Smith", "secret", "USER");

		//when
		Iterable<User> all = userRepository.findAll();

		//then
		assertThat(all).hasSize(1);
		User record = all.iterator().next();
		assertThat(record.getId()).isEqualTo("john2");
	}

	@Test
	public void shouldReturnPageWithOneItemWhenOneRecordInDatabase() {
		//given
		jdbcTemplate.update("INSERT INTO USER VALUES (?, ?, ?, ?, ?)", "john4", "johnsmith", "John Smith", "secret", "USER");

		//when
		Page<User> page = userRepository.findAll(new PageRequest(0, 5));

		//then
		assertThat(page).hasSize(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent().get(0).getId()).isEqualTo("john4");
	}

	@Test
	public void shouldReturnNothingWhenOnlyOneRecordInDatabaseButSecondPageRequested() {
		//given
		jdbcTemplate.update("INSERT INTO USER VALUES (?, ?, ?, ?, ?)", "john5", "johnsmith", "John Smith", "secret", "USER");

		//when
		Page<User> page = userRepository.findAll(new PageRequest(1, 5));

		//then
		assertThat(page).hasSize(0);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(1);
	}

	@Test
	public void shouldReturnPageWithOneItemWithSortingApplied() {
		//given
		jdbcTemplate.update("INSERT INTO USER VALUES (?, ?, ?, ?, ?)", "john6", "johnsmith", "John Smith", "secret", "USER");

		//when
		Page<User> page = userRepository.findAll(new PageRequest(0, 5, Sort.Direction.ASC, "userName"));

		//then
		assertThat(page).hasSize(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent().get(0).getId()).isEqualTo("john6");
	}

}
