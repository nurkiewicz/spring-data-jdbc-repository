package com.nurkiewicz.jdbcrepository.derby;

import com.nurkiewicz.jdbcrepository.sql.DerbySqlGenerator;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/19/13, 10:13 PM
 */
public class CommentWithUserDerbySqlGenerator extends DerbySqlGenerator {

	public CommentWithUserDerbySqlGenerator() {
		super("c.*, u.date_of_birth, u.reputation, u.enabled");
	}

}
