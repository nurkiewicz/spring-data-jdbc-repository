package com.blogspot.nurkiewicz.jdbcrepository.sql;

import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
public abstract class AbstractMssqlSqlGenerator extends SqlGenerator {
    @Override
    protected String limitClause(Pageable page) {
        return "";
    }
}
