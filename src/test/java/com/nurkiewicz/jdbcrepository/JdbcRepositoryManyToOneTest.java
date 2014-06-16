package com.nurkiewicz.jdbcrepository;

import com.nurkiewicz.jdbcrepository.repositories.CommentWithUser;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUserRepository;
import com.nurkiewicz.jdbcrepository.repositories.User;
import com.nurkiewicz.jdbcrepository.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.JANUARY;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

public abstract class JdbcRepositoryManyToOneTest extends AbstractIntegrationTest {

	public static final String SOME_USER = "some_user";
	@Resource
	private CommentWithUserRepository repository;

	@Resource
	private UserRepository userRepository;
	private User someUser;
	private static final java.sql.Date SOME_DATE = new java.sql.Date(new GregorianCalendar(2013, JANUARY, 19).getTimeInMillis());
	private static final Timestamp SOME_TIMESTAMP = new Timestamp(new GregorianCalendar(2013, JANUARY, 20).getTimeInMillis());

	protected JdbcRepositoryManyToOneTest() {
	}

	protected JdbcRepositoryManyToOneTest(int databasePort) {
		super(databasePort);
	}

	@Before
	public void setup() {
		someUser = userRepository.save(new User(SOME_USER, SOME_DATE, -1, false));
	}

	@Test
	public void shouldGenerateKey() throws Exception {
		//given
		final CommentWithUser comment = new CommentWithUser(someUser, "Some content", SOME_TIMESTAMP, 0);

		//when
		repository.save(comment);

		//then
		assertThat(comment.getId()).isNotNull();
	}

	@Test
	public void shouldReturnCommentWithUserAttached() throws Exception {
		//given
		final CommentWithUser comment = new CommentWithUser(someUser, "Some content", SOME_TIMESTAMP, 0);

		//when
		repository.save(comment);

		//then
		final CommentWithUser foundComment = repository.findOne(comment.getId());
		assertThat(foundComment).isEqualTo(new CommentWithUser(comment.getId(), someUser, "Some content", SOME_TIMESTAMP, 0));
	}

	@Test
	public void shouldReturnMultipleCommentsAttachedToTheSameUser() throws Exception {
		//given
		final CommentWithUser first = repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));
		final CommentWithUser second = repository.save(new CommentWithUser(someUser, "Second comment", SOME_TIMESTAMP, 2));
		final CommentWithUser third = repository.save(new CommentWithUser(someUser, "Third comment", SOME_TIMESTAMP, 1));

		//when
		final List<CommentWithUser> all = repository.findAll(new Sort("favourite_count"));

		//then
		assertThat(all).containsExactly(third, second, first);
	}

	@Test
	public void shouldReturnMultipleCommentsAttachedToDifferentUsers() throws Exception {
		//given
		final User firstUser = userRepository.save(new User("First user", SOME_DATE, 10, false));
		final User secondUser = userRepository.save(new User("Second user", SOME_DATE, 20, false));
		final User thirdUser = userRepository.save(new User("Third user", SOME_DATE, 30, false));

		final CommentWithUser first = repository.save(new CommentWithUser(firstUser, "First comment", SOME_TIMESTAMP, 3));
		final CommentWithUser second = repository.save(new CommentWithUser(secondUser, "Second comment", SOME_TIMESTAMP, 2));
		final CommentWithUser third = repository.save(new CommentWithUser(thirdUser, "Third comment", SOME_TIMESTAMP, 1));

		//when
		final List<CommentWithUser> all = repository.findAll(new Sort(DESC, "favourite_count"));

		//then
		assertThat(all).containsExactly(first, second, third);
	}

	@Test
	public void shouldReturnOnlyFirstPageWithUsers() throws Exception {
		//given
		final CommentWithUser first = repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));
		final CommentWithUser second = repository.save(new CommentWithUser(someUser, "Second comment", SOME_TIMESTAMP, 2));
		repository.save(new CommentWithUser(someUser, "Third comment", SOME_TIMESTAMP, 1));

		//when
		final Page<CommentWithUser> page = repository.findAll(new PageRequest(0, 2, ASC, "contents"));

		//then
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getContent()).containsExactly(first, second);
	}

	@Test
	public void shouldReturnOnlySecondPageWithUsers() throws Exception {
		//given
		repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));
		repository.save(new CommentWithUser(someUser, "Second comment", SOME_TIMESTAMP, 2));
		final CommentWithUser third = repository.save(new CommentWithUser(someUser, "Third comment", SOME_TIMESTAMP, 1));

		//when
		final Page<CommentWithUser> page = repository.findAll(new PageRequest(1, 2, ASC, "contents"));

		//then
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getContent()).containsExactly(third);
	}

	@Test
	public void shouldDeleteCommentWithoutDeletingUser() throws Exception {
		//given
		final CommentWithUser comment = repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));

		//when
		repository.delete(comment);

		//then
		assertThat(repository.count()).isZero();
		assertThat(userRepository.exists(SOME_USER)).isTrue();
	}

	@Test
	public void shouldUpdateCommentByAttachingDifferentUser() throws Exception {
		//given
		final User firstUser = userRepository.save(new User("First user", SOME_DATE, 10, false));
		final CommentWithUser comment = repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));

		//when
		comment.setUser(firstUser);
		repository.save(comment);

		//then
		assertThat(repository.count()).isEqualTo(1);
		final CommentWithUser foundComment = repository.findOne(comment.getId());
		assertThat(foundComment.getUser()).isEqualTo(firstUser);
	}

	@Test
	public void shouldDeleteAllCommentsWithoutDeletingUsers() throws Exception {
		//given
		final CommentWithUser first = repository.save(new CommentWithUser(someUser, "First comment", SOME_TIMESTAMP, 3));
		final CommentWithUser second = repository.save(new CommentWithUser(someUser, "Second comment", SOME_TIMESTAMP, 2));
		final CommentWithUser third = repository.save(new CommentWithUser(someUser, "Third comment", SOME_TIMESTAMP, 1));

		//when
		repository.deleteAll();

		//then
		assertThat(repository.count()).isZero();
		assertThat(userRepository.exists(SOME_USER)).isTrue();
	}

}
