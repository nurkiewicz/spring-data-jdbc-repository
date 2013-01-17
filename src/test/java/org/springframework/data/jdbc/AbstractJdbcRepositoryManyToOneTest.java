package org.springframework.data.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.repositories.CommentWithUser;
import org.springframework.data.jdbc.repositories.CommentWithUserRepository;
import org.springframework.data.jdbc.repositories.User;
import org.springframework.data.jdbc.repositories.UserRepository;

import javax.annotation.Resource;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

public abstract class AbstractJdbcRepositoryManyToOneTest extends AbstractIntegrationTest {

	@Resource
	private CommentWithUserRepository repository;

	@Resource
	private UserRepository userRepository;
	private User someUser;
	private static final Date SOME_DATE = new Date();


	protected AbstractJdbcRepositoryManyToOneTest(int databasePort) {
		super(databasePort);
	}

	@Before
	public void setup() {
		someUser = userRepository.save(new User("some_user", new Date(), -1, false));
	}

	@Test
	public void shouldGenerateKey() throws Exception {
		//given
		final CommentWithUser comment = new CommentWithUser(someUser, "Some content", SOME_DATE, 0);

		//when
		repository.save(comment);

		//then
		assertThat(comment.getId()).isNotNull();
	}

	@Test
	public void shouldReturnCommentWithUserAttached() throws Exception {
		//given
		final CommentWithUser comment = new CommentWithUser(someUser, "Some content", SOME_DATE, 0);

		//when
		repository.save(comment);

		//then
		final CommentWithUser foundComment = repository.findOne(comment.getId());
		assertThat(foundComment).isEqualTo(new CommentWithUser(someUser, "Some content", SOME_DATE, 0));
	}

}
