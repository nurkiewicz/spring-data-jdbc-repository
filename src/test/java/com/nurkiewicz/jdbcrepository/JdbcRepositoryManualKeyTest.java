package com.nurkiewicz.jdbcrepository;

import com.google.common.collect.Lists;
import com.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.nurkiewicz.jdbcrepository.repositories.User;
import com.nurkiewicz.jdbcrepository.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order;

public abstract class JdbcRepositoryManualKeyTest extends AbstractIntegrationTest {

	public static final int SOME_REPUTATION = 42;

	@Resource
	private UserRepository repository;

	@Resource
	private CommentRepository commentRepository;

	@Resource
	private DataSource dataSource;

	private JdbcOperations jdbc;

	private static final Date SOME_DATE_OF_BIRTH = new Date(new GregorianCalendar(2013, Calendar.JANUARY, 9).getTimeInMillis());

	public JdbcRepositoryManualKeyTest() {
	}

	public JdbcRepositoryManualKeyTest(int databasePort) {
		super(databasePort);
	}

	@Before
	public void setup() {
		jdbc = new JdbcTemplate(dataSource);
	}


	@Test
	public void shouldReturnNullWhenDatabaseEmptyAndSearchingById() {
		//given
		String notExistingId = "Foo";

		//when
		User user = repository.findOne(notExistingId);

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

	private User user(String userName) {
		return new User(userName, SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);
	}

	@Test
	public void shouldSaveOneRecord() {
		//given
		User john = user("john");

		//when
		repository.save(john);
		Iterable<User> all = repository.findAll();

		//then
		assertThat(all).hasSize(1);
		User record = all.iterator().next();
		assertThat(record).isEqualTo(user("john"));
	}

	@Test
	public void shouldUpdatePreviouslySavedRecord() throws Exception {
		//given
		User john = repository.save(user("john"));
		john.setEnabled(false);
		john.setReputation(45);

		//when
		repository.save(john);

		//then
		User updated = repository.findOne("john");
		assertThat(updated).isEqualTo(new User("john", SOME_DATE_OF_BIRTH, 45, false));
	}

	@Test
	public void shouldReturnOneRecordById() {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "james", SOME_DATE_OF_BIRTH, 43, false);

		//when
		User user = repository.findOne("james");

		//then
		assertThat(user).isEqualTo(new User("james", SOME_DATE_OF_BIRTH, 43, false));
	}

	@Test
	public void shouldReturnNullWhenEntityForGivenIdDoesNotExist() throws Exception {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "james", SOME_DATE_OF_BIRTH, 43, false);

		//when
		User user = repository.findOne("john");

		//then
		assertThat(user).isNull();
	}

	@Test
	public void shouldReturnListWithOneItemWhenOneRecordInDatabase() {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john2", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

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
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john4", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

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
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john5", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

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
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john6", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 5, ASC, "user_name"));

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
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john6", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 5, new Sort(new Order(DESC, "reputation"), new Order(ASC, "user_name"))));

		//then
		assertThat(page).hasSize(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(5);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent().get(0).getId()).isEqualTo("john6");
	}

	@Test
	public void shouldReturnFirstPageSortedByReputation() {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john11", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john12", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john13", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john14", SOME_DATE_OF_BIRTH, SOME_REPUTATION    , true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john15", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(0, 3, new Sort(new Order(DESC, "reputation"), new Order(ASC, "user_name"))));

		//then
		assertThat(page).hasSize(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getSize()).isEqualTo(3);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getContent()).containsExactly(
				new User("john11", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true),
				new User("john13", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true),
				new User("john12", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true)
		);
	}

	@Test
	public void shouldReturnSecondPageSortedByReputation() {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john11", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john12", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john13", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 2, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john14", SOME_DATE_OF_BIRTH, SOME_REPUTATION    , true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john15", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);

		//when
		Page<User> page = repository.findAll(new PageRequest(1, 3, new Sort(new Order(DESC, "reputation"), new Order(ASC, "user_name"))));

		//then
		assertThat(page).hasSize(2);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getSize()).isEqualTo(3);
		assertThat(page.getNumber()).isEqualTo(1);
		assertThat(page.getContent()).containsExactly(
				new User("john15", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true),
				new User("john14", SOME_DATE_OF_BIRTH, SOME_REPUTATION    , true)
		);
	}

	@Test
	public void shouldReturnEmptyListWhenFindAllCalledWithoutPaging() throws Exception {
		//given
		final Sort sort = new Sort("reputation");

		//when
		final Iterable<User> reputation = repository.findAll(sort);

		//then
		assertThat(reputation).isEmpty();
	}

	@Test
	public void shouldReturnEmptyListWhenFindAllCalledWithoutPagingButWithSortingOnMultipleProperties() throws Exception {
		//given
		final Sort sort = new Sort(new Order(DESC, "reputation"), new Order(ASC, "date_of_birth"));

		//when
		final Iterable<User> reputation = repository.findAll(sort);

		//then
		assertThat(reputation).isEmpty();
	}

	@Test
	public void shouldReturnSingleRecordWhenFindAllWithoutPagingButWithSorting() throws Exception {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john7", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);
		final Sort sort = new Sort(new Order(DESC, "reputation"), new Order(ASC, "date_of_birth"));

		//when
		final Iterable<User> all = repository.findAll(sort);

		//then
		assertThat(all).hasSize(1);
		assertThat(all.iterator().next()).isEqualTo(new User("john7", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true));
	}

	@Test
	public void shouldSortMultipleRecordsByTwoDifferentOrderingProperties() throws Exception {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john3", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john5", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john4", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true);
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john6", SOME_DATE_OF_BIRTH, SOME_REPUTATION - 1, true);
		final Sort sort = new Sort(new Order(DESC, "reputation"), new Order(ASC, "user_name"));

		//when
		final List<User> all = repository.findAll(sort);

		//then
		assertThat(all).containsExactly(
				new User("john4", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true),
				new User("john5", SOME_DATE_OF_BIRTH, SOME_REPUTATION + 1, true),
				new User("john3", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true),
				new User("john6", SOME_DATE_OF_BIRTH, SOME_REPUTATION - 1, true)
		);
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
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john7", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		boolean exists = repository.exists("john6");

		//then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnTrueWhenEntityForGivenIdExists() {
		//given
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", "john8", SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		boolean exists = repository.exists("john8");

		//then
		assertThat(exists).isTrue();
	}

	@Test
	public void shouldDeleteEntityById() throws Exception {
		//given
		final String someId = "john9";
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", someId, SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		repository.delete(someId);

		//then
		assertThat(jdbc.queryForObject("SELECT COUNT(user_name) FROM USERS WHERE user_name = ?",				Integer.class, someId)).isZero();
	}

	@Test
	public void shouldDoNothingWhenEntityForGivenIdDoesNotExist() throws Exception {
		//given
		final String someId = "john10";

		//when
		repository.delete(someId);

		//then
		//no exception
	}

	@Test
	public void shouldNotDeleteEntityWithDifferentId() throws Exception {
		//given
		final String someId = "john11";
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", someId, SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		repository.delete(someId + "_");

		//then
		assertThat(jdbc.queryForList("SELECT user_name FROM USERS WHERE user_name = ?", String.class, someId)).containsExactly(someId);
	}

	@Test
	public void shouldDeleteByEntity() throws Exception {
		//given
		final String someId = "john12";
		final User user = repository.save(user(someId));

		//when
		repository.delete(user);

		//then
		assertThat(jdbc.queryForObject("SELECT COUNT(user_name) FROM USERS WHERE user_name = ?",
				Integer.class, someId)).isZero();
	}

	@Test
	public void shouldReturnZeroForCountWhenEmptyTable() throws Exception {
		//given

		//when
		final long count = repository.count();

		//then
		assertThat(count).isZero();
	}

	@Test
	public void shouldReturnOneWhenSingleElementInTable() throws Exception {
		//given
		final String someId = "john12";
		jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", someId, SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);

		//when
		final long count = repository.count();

		//then
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void shouldReturnCountOfRecordsInTable() throws Exception {
		//given
		insertRecordsForIds("1", "2", "3");

		//when
		final long count = repository.count();

		//then
		assertThat(count).isEqualTo(3);
	}

	@Test
	public void shouldSaveMultipleEntities() throws Exception {
		//given
		User john = user("john");
		User alice = user("alice");

		//when
		repository.save(Arrays.asList(john, alice));

		//then
		assertThat(jdbc.queryForList("SELECT user_name FROM USERS ORDER BY user_name", String.class)).containsExactly("alice", "john");
	}

	@Test
	public void shouldDeleteMultipleEntities() throws Exception {
		//given
		User john = user("john");
		User alice = user("alice");
		repository.save(Arrays.asList(john, alice));

		//when
		repository.delete(Arrays.asList(john, alice));

		//then
		assertThat(jdbc.queryForObject("SELECT COUNT(user_name) FROM USERS", Integer.class)).isZero();
	}

	@Test
	public void shouldSkipNonListedEntitiesWhenDeletingInBatch() throws Exception {
		//given
		User john = user("john");
		User alice = user("alice");
		final User bobby = user("bobby");
		repository.save(Arrays.asList(john, alice, bobby));

		//when
		repository.delete(Arrays.asList(john, alice));

		//then
		assertThat(jdbc.queryForList("SELECT user_name FROM USERS", String.class)).containsExactly("bobby");
	}

	@Test
	public void shouldSkipNonExistingEntitiesWhenDeletingInBatch() throws Exception {
		//given
		User john = user("john");
		User alice = user("alice");
		final User bobby = user("bobby");
		repository.save(Arrays.asList(john, alice, bobby));

		//when
		repository.delete(Arrays.asList(john, alice, user("bogus")));

		//then
		assertThat(jdbc.queryForList("SELECT user_name FROM USERS", String.class)).containsExactly("bobby");
	}

	@Test
	public void shouldDoNothingWhenDeletingAllButEmptyTable() throws Exception {
		//given

		//when
		repository.deleteAll();

		//then
		assertThat(jdbc.queryForObject("SELECT COUNT(user_name) FROM USERS", Integer.class)).isZero();
	}

	@Test
	public void shouldDeleteAllRecordsInTable() throws Exception {
		//given
		insertRecordsForIds("1", "2", "3");

		//when
		repository.deleteAll();

		//then
		assertThat(jdbc.queryForObject("SELECT COUNT(user_name) FROM USERS", Integer.class)).isZero();
	}

	private void insertRecordsForIds(String... ids) {
		for(String id: ids) {
			jdbc.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", id, SOME_DATE_OF_BIRTH, SOME_REPUTATION, true);
		}
	}

	@Test
	public void shouldReturnNothingWhenFindingByListOfIdsButListEmpty() throws Exception {
		//given
		insertRecordsForIds("1", "2", "3");

		//when
		final Iterable<User> none = repository.findAll(Collections.<String>emptyList());

		//then
		assertThat(none).isEmpty();
	}

	@Test
	public void shouldSelectOneRecordById() throws Exception {
		//given
		insertRecordsForIds("1", "2", "3");

		//when
		final List<User> oneRecord = Lists.newArrayList(repository.findAll(Arrays.asList("2")));

		//then
		assertThat(oneRecord).hasSize(1);
		assertThat(oneRecord.get(0).getId()).isEqualTo("2");
	}

	@Test
	public void shouldSelectMultipleRecordsById() throws Exception {
		//given
		insertRecordsForIds("1", "2", "3");

		//when
		final List<User> users = Lists.newArrayList(repository.findAll(Arrays.asList("1", "3")));

		//then
		assertThat(users).hasSize(2);
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		assertThat(users.get(0).getId()).isEqualTo("1");
		assertThat(users.get(1).getId()).isEqualTo("3");
	}

}
