package com.blogspot.nurkiewicz.jdbcrepository.sql;

import com.blogspot.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
public class MssqlSqlGenerator extends AbstractMssqlSqlGenerator {

    public static final String ROW_NUM_COLUMN = "ROW_NUM";
    public static final String ROW_NUM_COLUMN_CLAUSE = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY %s) AS " + ROW_NUM_COLUMN + ", __t.* FROM (";

    @Override
    public String selectAll(TableDescription table, Pageable page) {
        String sql = super.selectAll(table);
        final int offset = page.getPageNumber() * page.getPageSize() + 1;
        String orderByPart = page.getSort() != null ? page.getSort().toString().replace(":", "") : table.getIdColumns().get(0);
        return String.format(ROW_NUM_COLUMN_CLAUSE, orderByPart) + super.selectAll(table) + ") AS __t) AS a WHERE " + ROW_NUM_COLUMN + " BETWEEN " + offset + " AND " + (offset + page.getPageSize() - 1);
    }
}
