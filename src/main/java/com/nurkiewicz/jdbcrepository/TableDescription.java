package com.nurkiewicz.jdbcrepository;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/18/12, 10:06 PM
 */
public class TableDescription {

	private final String name;
	private final List<String> idColumns;
	private final String fromClause;

	public TableDescription(String name, String fromClause, String... idColumns) {
		Assert.notNull(name);
		Assert.notNull(idColumns);
		Assert.isTrue(idColumns.length > 0, "At least one primary key column must be provided");

		this.name = name;
		this.idColumns = Collections.unmodifiableList(Arrays.asList(idColumns));
		if (StringUtils.hasText(fromClause)) {
			this.fromClause = fromClause;
		} else {
			this.fromClause = name;
		}
	}

	public TableDescription(String name, String idColumn) {
		this(name, null, idColumn);
	}

	public String getName() {
		return name;
	}

	public List<String> getIdColumns() {
		return idColumns;
	}

	public String getFromClause() {
		return fromClause;
	}
}
