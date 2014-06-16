package com.nurkiewicz.jdbcrepository.sql;

import org.springframework.data.domain.Pageable;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/15/13, 11:03 PM
 */
public class PostgreSqlGenerator extends SqlGenerator {
	@Override
	protected String limitClause(Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize();
		return " LIMIT " + page.getPageSize() + " OFFSET " + offset;
	}
}
