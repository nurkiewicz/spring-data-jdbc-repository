package org.springframework.data.jdbc.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.TableDescription;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/16/13, 10:25 PM
 */
public class DerbySqlGenerator extends SqlGenerator {

	public static final String ROW_NUM_COLUMN = "ROW_NUM";
	public static final String ROW_NUM_COLUMN_CLAUSE = "ROW_NUMBER() OVER () AS " + ROW_NUM_COLUMN + ", ";

	public DerbySqlGenerator(String allColumnsClause) {
		super(ROW_NUM_COLUMN_CLAUSE + allColumnsClause);
	}

	public DerbySqlGenerator() {
		super(ROW_NUM_COLUMN_CLAUSE + "e.*");
	}

	@Override
	public String selectAll(TableDescription table) {
		return "SELECT " + getAllColumnsClause() + " FROM " + table.getName() + " e";
	}

	@Override
	public String selectAll(TableDescription table, Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize() + 1;
		return "SELECT * FROM (" + super.selectAll(table, page) + ") AS t WHERE " + ROW_NUM_COLUMN + " BETWEEN " + offset + " AND " + (offset + page.getPageSize() - 1);
	}

	@Override
	protected String limitClause(Pageable page) {
		return "";
	}
}
