package org.springframework.data.jdbc;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repositories.BoardingPass;
import org.springframework.data.jdbc.repositories.BoardingPassRepository;

import javax.annotation.Resource;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order;
import static org.springframework.data.jdbc.AbstractJdbcRepository.pk;

public abstract class AbstractJdbcRepositoryCompoundPkTest extends AbstractIntegrationTest {

	public AbstractJdbcRepositoryCompoundPkTest(int databasePort) {
		super(databasePort);
	}


	@Resource
	private BoardingPassRepository repository;

	@Test
	public void shouldStoreEntityWithCompoundPrimaryKey() throws Exception {
		//given
		final BoardingPass entity = new BoardingPass("FOO-100", 42, "Smith", "B1");

		//when
		repository.save(entity);

		//then
		final BoardingPass found = repository.findOne(pk("FOO-100", 42));
		assertThat(found).isEqualTo(new BoardingPass("FOO-100", 42, "Smith", "B1"));
	}

	@Test
	public void shouldAllowStoringMultipleEntitiesDifferingByOnePrimaryKeyColumn() throws Exception {
		//given
		final BoardingPass firstSeatInA = repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));
		final BoardingPass secondSeatInA = repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C2"));
		final BoardingPass firstSeatInB = repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D3"));
		final BoardingPass secondSeatInB = repository.save(new BoardingPass("BAR-100", 2, "Who", "E4"));

		//when
		final BoardingPass foundFlight = repository.findOne(pk("BAR-100", 1));

		//then
		assertThat(foundFlight).isEqualTo(new BoardingPass("BAR-100", 1, "Gordon", "D3"));
	}

	@Test
	public void shouldAllowUpdatingByPrimaryKey() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));
		final BoardingPass secondSeat = repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C2"));

		secondSeat.setPassenger("Jameson");
		secondSeat.setSeat("C3");

		//when
		repository.save(secondSeat);

		//then
		assertThat(repository.count()).isEqualTo(2);
		final BoardingPass foundUpdated = repository.findOne(pk("FOO-100", 2));
		assertThat(foundUpdated).isEqualTo(new BoardingPass("FOO-100", 2, "Jameson", "C3"));
	}

	@Test
	public void shouldDeleteByCompoundPrimaryKey() throws Exception {
		//given
		final BoardingPass pass = repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));

		//when
		repository.delete(pk("FOO-100", 1));

		//then
		assertThat(repository.exists(pk("FOO-100", 1))).isFalse();
	}

	@Test
	public void shouldAllowSortingByAllPrimaryKeyColumns() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C2"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E4"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D3"));

		//when
		final List<BoardingPass> all = repository.findAll(
				new Sort(
						new Order(ASC, "flight_no"),
						new Order(DESC, "seq_no")
				)
		);

		//then
		assertThat(all).containsExactly(
				new BoardingPass("BAR-100", 2, "Who", "E4"),
				new BoardingPass("BAR-100", 1, "Gordon", "D3"),
				new BoardingPass("FOO-100", 2, "Johnson", "C2"),
				new BoardingPass("FOO-100", 1, "Smith", "B1")
		);
	}

	@Test
	public void shouldReturnFirstPageOfSortedResults() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C2"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E4"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D3"));

		//when
		final Page<BoardingPass> page = repository.findAll(
				new PageRequest(0, 3,
						new Sort(
								new Order(ASC, "flight_no"),
								new Order(DESC, "seq_no")
						)
				));

		//then
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getContent()).containsExactly(
				new BoardingPass("BAR-100", 2, "Who", "E4"),
				new BoardingPass("BAR-100", 1, "Gordon", "D3"),
				new BoardingPass("FOO-100", 2, "Johnson", "C2")
		);
	}

	@Test
	public void shouldReturnLastPageOfSortedResults() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B1"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C2"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E4"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D3"));

		//when
		final Page<BoardingPass> page = repository.findAll(
				new PageRequest(1, 3,
						new Sort(
								new Order(ASC, "flight_no"),
								new Order(DESC, "seq_no")
						)
				));

		//then
		assertThat(page.getContent()).containsExactly(new BoardingPass("FOO-100", 1, "Smith", "B1"));
	}

}
