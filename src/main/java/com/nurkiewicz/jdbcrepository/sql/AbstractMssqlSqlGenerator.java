package com.nurkiewicz.jdbcrepository.sql;

import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
abstract class AbstractMssqlSqlGenerator extends SqlGenerator {
	public AbstractMssqlSqlGenerator() {
	}

	public AbstractMssqlSqlGenerator(String allColumnsClause) {
		super(allColumnsClause);
	}

	@Override
	protected String limitClause(Pageable page) {
		return "";
	}
}
