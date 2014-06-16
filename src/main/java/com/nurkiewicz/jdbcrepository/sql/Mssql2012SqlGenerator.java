package com.nurkiewicz.jdbcrepository.sql;

import com.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

/**
 * SQLServer Pagination feature for SQLServer 2012+ -> extension of order by clause
 *
 * @see http://msdn.microsoft.com/en-us/library/ms188385.aspx
 *      Author: tom
 */
public class Mssql2012SqlGenerator extends AbstractMssqlSqlGenerator {

	/**
	 * Sort by first column
	 */
	private static final String MSSQL_DEFAULT_SORT_CLAUSE = " ORDER BY 1 ASC";

	@Override
	public String selectAll(TableDescription table, Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize() + 1;
		String sortingClause = super.sortingClauseIfRequired(page.getSort());

		if (!StringUtils.hasText(sortingClause)) {
			//The Pagination feature requires a sort clause, if none is given we sort by the first column
			sortingClause = MSSQL_DEFAULT_SORT_CLAUSE;
		}

		final String paginationClause = " OFFSET " + (offset - 1) + " ROWS FETCH NEXT " + page.getPageSize() + " ROW ONLY";
		return super.selectAll(table) + sortingClause + paginationClause;
	}
}
