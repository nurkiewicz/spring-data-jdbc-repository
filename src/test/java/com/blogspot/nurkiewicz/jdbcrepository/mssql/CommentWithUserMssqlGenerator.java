package com.blogspot.nurkiewicz.jdbcrepository.mssql;

import com.blogspot.nurkiewicz.jdbcrepository.sql.MssqlSqlGenerator;

/**
 * Author: tom
 */
public class CommentWithUserMssqlGenerator extends MssqlSqlGenerator {
    public CommentWithUserMssqlGenerator() {
        super("c.*, u.date_of_birth, u.reputation, u.enabled");
    }
}
