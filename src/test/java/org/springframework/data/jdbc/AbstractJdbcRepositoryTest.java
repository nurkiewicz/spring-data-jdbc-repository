package org.springframework.data.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestConfig.class)
@Transactional
public class AbstractJdbcRepositoryTest {

	public static final int SOME_REPUTATION = 42;
	@Resource
	private UserRepository repository;

	@Resource
	private DataSource dataSource;

	private JdbcOperations jdbc;

	private static final Date someDateOfBirth = new Date();

	@Before
	public void setup() {
		jdbc = new JdbcTemplate(dataSource);
	}


	@Test
	public void shouldReturnNullWhenDatabaseEmptyAndSearchingById() {
		//given
		String NOT_EXISTING_ID = "Foo";

		//when
		User user = repository.findOne(NOT_EXISTING_ID);

		//then
		assertThat(user).isNull();
	}

	@Test
	public void shouldReturnEmptyListWhenDatabaseEmpty() {
		//given

		//when
		Iterable<User> all = repository.findAll();

		//then
		assertThat(all).isEmpty();
	}

	@Test
	public void shouldReturnEmptyPageWhenNoEntitiesInDatabase() {
		//given

		//when
		Page<User> firstPage = repository.findAll(new PageRequest(0, 20));

		//then
		assertThat(firstPage).isEmpty();
		assertThat(firstPage.getTotalElements()).isZero();
		assertThat(firstPage.getSize()).isEqualTo(20);
		assertThat(firstPage.getNumber()).isZero();
	}

	@Test
	public void shouldSaveOneRecord() {
		//given
		User john = new User("john", someDateOfBirth, 42, true);

		//when
		repository.save(john);
		Iterable<User> all = repository.findAll();

		//then
		assertThat(all).hasSize(1);
		User record = all.iterator().next();
		assertThat(record).isEqualTo(new User("john", someDateOfBirth, 42, true));
	}

	@Test
	public void shouldUpdatePreviouslySavedRecord() throws Exception {
		//given
		User john = repository.save(new User("john", someDateOfBirth, 42, true));
		john.setEnabled(false);
		john.setReputation(45);

		//when
		repository.save(john);

		//then
		User updated = repository.findOne("john");
		assertThat(updated).isEqualTo(new User("john", someDateOfBirth, 45, false));
	}

	@Test
	public void shouldReturnOneRecordById() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "james", someDateOfBirth, 43, false);

		//when
		User user = repository.findOne("james");

		//then
		assertThat(user).isEqualTo(new User("james", someDateOfBirth, 43, false));
	}

	@Test
	public void shouldReturnNullWhenEntityForGivenIdDoesNotExist() throws Exception {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "james", someDateOfBirth, 43, false);

		//when
		User user = repository.findOne("john");

		//then
		assertThat(user).isNull();
	}

	@Test
	public void shouldReturnListWithOneItemWhenOneRecordInDatabase() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john2", someDateOfBirth, 42, true);

		//when
		Iterable<User> all = repository.findAll();

		//then
		assertThat(all).hasSize(1);
		User record = all.iterator().next();
		assertThat(record.getId()).isEqualTo("john2");
	}

	@Test
	public void shouldReturnPageWithOneItemWhenOneRecordInDatabase() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john4", someDateOfBirth, 42, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 5));

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
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john5", someDateOfBirth, 42, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(1, 5));

		//then
		assertThat(page).hasSize(0);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(1);
	}

	@Test
	public void shouldReturnPageWithOneItemWithSortingApplied() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john6", someDateOfBirth, 42, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 5, Direction.ASC, "user_name"));

		//then
		assertThat(page).hasSize(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent().get(0).getId()).isEqualTo("john6");
	}

	@Test
	public void shouldReturnPageWithOneItemWithSortingAppliedOnTwoProperties() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john6", someDateOfBirth, 42, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 5, new Sort(new Order(Direction.DESC, "reputation"), new Order(Direction.ASC, "user_name"))));

		//then
		assertThat(page).hasSize(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent().get(0).getId()).isEqualTo("john6");
	}

	@Test
	public void shouldReturnFalseWhenExistsCalledOnEmptyDatabase() {
		//given

		//when
		boolean exists = repository.exists("john");

		//then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenEntityWithSuchIdDoesNotExist() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john7", someDateOfBirth, 42, true);

		//when
		boolean exists = repository.exists("john6");

		//then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnTrueWhenEntityForGivenIdExists() {
		//given
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", "john8", someDateOfBirth, 42, true);

		//when
		boolean exists = repository.exists("john8");

		//then
		assertThat(exists).isTrue();
	}

	@Test
	public void shouldDeleteEntityById() throws Exception {
		//given
		final String SOME_ID = "john9";
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", SOME_ID, someDateOfBirth, 42, true);

		//when
		repository.delete(SOME_ID);

		//then
		assertThat(jdbc.queryForInt("SELECT COUNT(user_name) FROM USER WHERE user_name = ?", SOME_ID)).isZero();
	}

	@Test
	public void shouldDoNothingWhenEntityForGivenIdDoesNotExist() throws Exception {
		//given
		final String SOME_ID = "john10";

		//when
		repository.delete(SOME_ID);

		//then
		//no exception
	}

	@Test
	public void shouldNotDeleteEntityWithDifferentId() throws Exception {
		//given
		final String SOME_ID = "john11";
		jdbc.update("INSERT INTO USER VALUES (?, ?, ?, ?)", SOME_ID, someDateOfBirth, SOME_REPUTATION, true);

		//when
		repository.delete(SOME_ID + "_");

		//then
		assertThat(jdbc.queryForList("SELECT user_name FROM USER WHERE user_name = ?", String.class, SOME_ID)).containsExactly(SOME_ID);
	}


}
