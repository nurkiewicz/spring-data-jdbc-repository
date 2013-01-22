package com.blogspot.nurkiewicz.jdbcrepository.sql;

import com.blogspot.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
public class MssqlSql2012Generator extends AbstractMssqlSqlGenerator {

	/**
	 * SQLServer Pagination feature for SQLServer 2012+ -> extension of order by clause
	 *
	 * @see http://msdn.microsoft.com/en-us/library/ms188385.aspx
	 */
	public static final String MSSQL_PAGINATION_CLAUSE = " OFFSET %s ROWS FETCH NEXT %s ROW ONLY";

	/**
	 * Sort by first column
	 */
	public static final String MSSQL_DEFAULT_SORT_CLAUSE = " ORDER BY 1 ASC";


	@Override
	public String selectAll(TableDescription table, Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize() + 1;
		String selectAll = super.selectAll(table);
		String sortingClause = super.sortingClauseIfRequired(page.getSort());


		if (sortingClause.trim().length() == 0) {
			//The Pagination feature requires a sort clause, if none is given we sort by the first column
			sortingClause = MSSQL_DEFAULT_SORT_CLAUSE;
		}

		String pagination = String.format(MSSQL_PAGINATION_CLAUSE, offset - 1, page.getPageSize());
		String sql = selectAll + sortingClause + pagination;
		return sql;
	}
}
