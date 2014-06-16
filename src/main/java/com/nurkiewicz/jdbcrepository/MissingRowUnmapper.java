package com.nurkiewicz.jdbcrepository;

import java.util.Map;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/19/12, 9:45 PM
 */
public class MissingRowUnmapper<T> implements RowUnmapper<T> {
	@Override
	public Map<String, Object> mapColumns(Object o) {
		throw new UnsupportedOperationException("This repository is read-only, it can't store or update entities");
	}
}
