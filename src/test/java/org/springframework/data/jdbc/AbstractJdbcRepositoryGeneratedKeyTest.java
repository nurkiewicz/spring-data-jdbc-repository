package org.springframework.data.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.repositories.Comment;
import org.springframework.data.jdbc.repositories.CommentRepository;
import org.springframework.data.jdbc.repositories.User;
import org.springframework.data.jdbc.repositories.UserRepository;

import javax.annotation.Resource;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/20/12, 10:55 PM
 */
public abstract class AbstractJdbcRepositoryGeneratedKeyTest extends AbstractIntegrationTest {

	@Resource
	private CommentRepository repository;

	@Resource
	private UserRepository userRepository;
	private String someUser = "some_user";

	public AbstractJdbcRepositoryGeneratedKeyTest(int databasePort) {
		super(databasePort);
	}

	@Before
	public void setup() {
		userRepository.save(new User(someUser, new Date(), -1, false));
	}

	@Test
	public void shouldGenerateKey() throws Exception {
		//given
		final Comment comment = new Comment(someUser, "Some content", new Date(), 0);

		//when
		repository.save(comment);

		//then
		assertThat(comment.getId()).isNotNull();
	}

	@Test
	public void shouldGenerateSubsequentIds() throws Exception {
		//given
		final Comment firstComment = new Comment(someUser, "Some content", new Date(), 0);
		final Comment secondComment = new Comment(someUser, "Some content", new Date(), 0);

		//when
		repository.save(firstComment);
		repository.save(secondComment);

		//then

		assertThat(firstComment.getId()).isLessThan(secondComment.getId());
	}

	@Test
	public void shouldUpdateCommentByGeneratedId() throws Exception {
		//given
		final Comment comment = repository.save(new Comment(someUser, "Some content", new Date(0), 0));
		final int id = comment.getId();

		//when
		final Comment updatedComment = repository.save(new Comment(id, someUser, "New content", new Date(1), 1));

		//then
		assertThat(repository.count()).isEqualTo(1);
		assertThat(updatedComment).isEqualTo(new Comment(id, someUser, "New content", new Date(1), 1));
	}

}
