package com.nurkiewicz.jdbcrepository.sql;

import com.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
public class SQL99Helper {
	public static String ROW_NUM_WRAPPER = "SELECT a__.* FROM (SELECT row_number() OVER (ORDER BY %s) AS ROW_NUM,  t__.*  FROM   (%s) t__) a__ WHERE  a__.row_num BETWEEN %s AND %s";

	public static String generateSelectAllWithPagination(TableDescription table, Pageable page, SqlGenerator sqlGenerator) {
		final int beginOffset = page.getPageNumber() * page.getPageSize() + 1;
		final int endOffset = beginOffset + page.getPageSize() - 1;
		String orderByPart = page.getSort() != null ? page.getSort().toString().replace(":", "") : table.getIdColumns().get(0);
		String selectAllPart = sqlGenerator.selectAll(table);
		return String.format(ROW_NUM_WRAPPER, orderByPart, selectAllPart, beginOffset, endOffset);
	}
}
