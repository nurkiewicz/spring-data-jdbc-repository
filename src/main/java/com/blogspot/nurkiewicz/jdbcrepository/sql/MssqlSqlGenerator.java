package com.blogspot.nurkiewicz.jdbcrepository.sql;

import com.blogspot.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
public class MssqlSqlGenerator extends AbstractMssqlSqlGenerator {
	public static String ROW_NUM_WRAPPER = "SELECT * FROM (SELECT Row_number() OVER (ORDER BY %s) AS ROW_NUM,  __t.*  FROM   (%n %s %n) AS __t) AS a WHERE  row_num BETWEEN %s AND %s ";

	public MssqlSqlGenerator() {
	}

	public MssqlSqlGenerator(String allColumnsClause) {
		super(allColumnsClause);
	}


	@Override
	public String selectAll(TableDescription table, Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize() + 1;
		String orderByPart = page.getSort() != null ? page.getSort().toString().replace(":", "") : table.getIdColumns().get(0);
		String selectAll = super.selectAll(table);
		return String.format(ROW_NUM_WRAPPER, orderByPart, selectAll, offset, (offset + page.getPageSize() - 1));
	}
}
