package com.nurkiewicz.jdbcrepository;

import com.google.common.collect.Lists;
import com.nurkiewicz.jdbcrepository.repositories.BoardingPass;
import com.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.nurkiewicz.jdbcrepository.JdbcRepository.pk;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order;

public abstract class JdbcRepositoryCompoundPkTest extends AbstractIntegrationTest {

	public JdbcRepositoryCompoundPkTest() {
	}

	public JdbcRepositoryCompoundPkTest(int databasePort) {
		super(databasePort);
	}

	@Resource
	private BoardingPassRepository repository;

	@Test
	public void shouldStoreEntityWithCompoundPrimaryKey() throws Exception {
		//given
		final BoardingPass entity = new BoardingPass("FOO-100", 42, "Smith", "B01");

		//when
		repository.save(entity);

		//then
		final BoardingPass found = repository.findOne(pk("FOO-100", 42));
		assertThat(found).isEqualTo(new BoardingPass("FOO-100", 42, "Smith", "B01"));
	}

	@Test
	public void shouldAllowStoringMultipleEntitiesDifferingByOnePrimaryKeyColumn() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C02"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D03"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E04"));

		//when
		final BoardingPass foundFlight = repository.findOne(pk("BAR-100", 1));

		//then
		assertThat(foundFlight).isEqualTo(new BoardingPass("BAR-100", 1, "Gordon", "D03"));
	}

	@Test
	public void shouldAllowUpdatingByPrimaryKey() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		final BoardingPass secondSeat = repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C02"));

		secondSeat.setPassenger("Jameson");
		secondSeat.setSeat("C03");

		//when
		repository.save(secondSeat);

		//then
		assertThat(repository.count()).isEqualTo(2);
		final BoardingPass foundUpdated = repository.findOne(pk("FOO-100", 2));
		assertThat(foundUpdated).isEqualTo(new BoardingPass("FOO-100", 2, "Jameson", "C03"));
	}

	@Test
	public void shouldDeleteByCompoundPrimaryKey() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));

		//when
		repository.delete(pk("FOO-100", 1));

		//then
		assertThat(repository.exists(pk("FOO-100", 1))).isFalse();
	}

	@Test
	public void shouldDeleteByEntityUsingCompoundPk() throws Exception {
		//given
		final BoardingPass bp = repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));

		//when
		repository.delete(bp);

		//then
		assertThat(repository.findAll()).isEmpty();
	}

	@Test
	public void shouldAllowSortingByAllPrimaryKeyColumns() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C02"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E04"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D03"));

		//when
		final List<BoardingPass> all = repository.findAll(
				new Sort(
						new Order(ASC, "flight_no"),
						new Order(DESC, "seq_no")
				)
		);

		//then
		assertThat(all).containsExactly(
				new BoardingPass("BAR-100", 2, "Who", "E04"),
				new BoardingPass("BAR-100", 1, "Gordon", "D03"),
				new BoardingPass("FOO-100", 2, "Johnson", "C02"),
				new BoardingPass("FOO-100", 1, "Smith", "B01")
		);
	}

	@Test
	public void shouldReturnFirstPageOfSortedResults() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C02"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E04"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D03"));

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
				new BoardingPass("BAR-100", 2, "Who", "E04"),
				new BoardingPass("BAR-100", 1, "Gordon", "D03"),
				new BoardingPass("FOO-100", 2, "Johnson", "C02")
		);
	}

	@Test
	public void shouldReturnLastPageOfSortedResults() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 2, "Johnson", "C02"));
		repository.save(new BoardingPass("BAR-100", 2, "Who", "E04"));
		repository.save(new BoardingPass("BAR-100", 1, "Gordon", "D03"));

		//when
		final Page<BoardingPass> page = repository.findAll(
				new PageRequest(1, 3,
						new Sort(
								new Order(ASC, "flight_no"),
								new Order(DESC, "seq_no")
						)
				));

		//then
		assertThat(page.getContent()).containsExactly(new BoardingPass("FOO-100", 1, "Smith", "B01"));
	}

	@Test
	public void shouldReturnNothingWhenFindingByListOfIdsButListEmpty() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 2, "Smith", "B01"));
		repository.save(new BoardingPass("FOO-100", 3, "Smith", "B01"));

		//when
		final Iterable<BoardingPass> none = repository.findAll(Collections.<Object[]>emptyList());

		//then
		assertThat(none).isEmpty();
	}

	@Test
	public void shouldSelectOneRecordById() throws Exception {
		//given
		repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01"));
		final Object[] idOfSecond = repository.save(new BoardingPass("FOO-100", 2, "Smith", "B01")).getId();
		repository.save(new BoardingPass("FOO-100", 3, "Smith", "B01"));

		//when
		final List<BoardingPass> oneRecord = Lists.newArrayList(repository.findAll(Arrays.<Object[]>asList(idOfSecond)));

		//then
		assertThat(oneRecord).hasSize(1);
		assertThat(oneRecord.get(0).getId()).isEqualTo(idOfSecond);
	}

	@Test
	public void shouldSelectMultipleRecordsById() throws Exception {
		//given
		final Object[] idOfFirst = repository.save(new BoardingPass("FOO-100", 1, "Smith", "B01")).getId();
		repository.save(new BoardingPass("FOO-100", 2, "Smith", "B01"));
		final Object[] idOfThird = repository.save(new BoardingPass("FOO-100", 3, "Smith", "B01")).getId();

		//when
		final List<BoardingPass> boardingPasses = Lists.newArrayList(repository.findAll(Arrays.asList(idOfFirst, idOfThird)));

		//then
		assertThat(boardingPasses).hasSize(2);
		Collections.sort(boardingPasses, new Comparator<BoardingPass>() {
			@Override
			public int compare(BoardingPass o1, BoardingPass o2) {
				return o1.getSeqNo() - o2.getSeqNo();
			}
		});
		assertThat(boardingPasses.get(0).getId()).isEqualTo(idOfFirst);
		assertThat(boardingPasses.get(1).getId()).isEqualTo(idOfThird);
	}


}
