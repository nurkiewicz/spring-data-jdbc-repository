package com.nurkiewicz.jdbcrepository.sql;

import com.google.common.collect.ImmutableMap;
import com.nurkiewicz.jdbcrepository.TableDescription;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Tomasz Nurkiewicz
 * @since 3/6/13, 8:32 PM
 */
public class SqlGeneratorTest {

	private final SqlGenerator sqlGenerator = new SqlGenerator();

	@Test
	public void buildSqlForSelectByIdsWhenSingleIdColumnAndNoId() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.selectByIds(table, 0);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table");
	}

	@Test
	public void buildSqlForSelectByIdsWhenSingleIdColumnAndOneId() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.selectByIds(table, 1);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE num = ?");
	}

	@Test
	public void buildSqlForSelectByIdsWhenSingleIdColumnAndTwoIds() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.selectByIds(table, 2);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE num IN (?, ?)");
	}

	@Test
	public void buildSqlForSelectByIdsWhenSingleIdColumnAndSeveralIds() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.selectByIds(table, 4);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE num IN (?, ?, ?, ?)");
	}

	@Test
	public void buildSqlForSelectByIdsWhenMultipleIdColumnsAndNoId() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2", "num3");

		//when
		final String sql = sqlGenerator.selectByIds(table, 0);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table");
	}

	@Test
	public void buildSqlForSelectByIdsWhenMultipleIdColumnsAndOneId() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2", "num3");

		//when
		final String sql = sqlGenerator.selectByIds(table, 1);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE num1 = ? AND num2 = ? AND num3 = ?");
	}

	@Test
	public void buildSqlForSelectByIdsWhenMultipleIdColumnsAndTwoIds() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2", "num3");

		//when
		final String sql = sqlGenerator.selectByIds(table, 2);

		//then
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE (num1 = ? AND num2 = ? AND num3 = ?) OR (num1 = ? AND num2 = ? AND num3 = ?)");
	}

	@Test
	public void buildSqlForSelectByIdsWhenMultipleIdColumnsAndSeveralIds() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2", "num3");

		//when
		final String sql = sqlGenerator.selectByIds(table, 4);

		//then
		final String idClause = "(num1 = ? AND num2 = ? AND num3 = ?)";
		assertThat(sql).isEqualTo("SELECT * FROM table WHERE " + idClause + " OR " + idClause + " OR " + idClause + " OR " + idClause);
	}

	@Test
	public void buildSqlForDeleteBySingleIdColumn() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.deleteById(table);

		//then
		assertThat(sql).isEqualTo("DELETE FROM table WHERE num = ?");
	}

	@Test
	public void buildSqlForDeleteByTwoIdColumns() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2");

		//when
		final String sql = sqlGenerator.deleteById(table);

		//then
		assertThat(sql).isEqualTo("DELETE FROM table WHERE num1 = ? AND num2 = ?");
	}

	@Test
	public void buildSqlForDeleteByMultipleIdColumns() throws Exception {
		//given
		final TableDescription table = new TableDescription("table", null, "num1", "num2", "num3");

		//when
		final String sql = sqlGenerator.deleteById(table);

		//then
		assertThat(sql).isEqualTo("DELETE FROM table WHERE num1 = ? AND num2 = ? AND num3 = ?");
	}

	@Test
	public void buildSqlForUpdateWithSingleIdColumn() throws Exception {
		//given
		final Object ANY = new Object();
		final TableDescription table = new TableDescription("table", "num");

		//when
		final String sql = sqlGenerator.update(table, ImmutableMap.of("x", ANY, "y", ANY, "z", ANY));

		//then
		assertThat(sql).isEqualTo("UPDATE table SET x = ?, y = ?, z = ? WHERE num = ?");
	}

	@Test
	public void buildSqlForUpdateWithMultipleIdColumns() throws Exception {
		//given
		final Object ANY = new Object();
		final TableDescription table = new TableDescription("table", null, "num1", "num2");

		//when
		final String sql = sqlGenerator.update(table, ImmutableMap.of("x", ANY, "y", ANY, "z", ANY));

		//then
		assertThat(sql).isEqualTo("UPDATE table SET x = ?, y = ?, z = ? WHERE num1 = ? AND num2 = ?");
	}

}
