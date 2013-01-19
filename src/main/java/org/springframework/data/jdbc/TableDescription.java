package org.springframework.data.jdbc;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/18/12, 10:06 PM
 */
public class TableDescription {

	private final String name;
	private final String idColumn;
	private final String fromClause;

	public TableDescription(String name, String fromClause, String idColumn) {
		Assert.notNull(name);
		Assert.notNull(idColumn);

		this.name = name;
		this.idColumn = idColumn;
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

	public String getIdColumn() {
		return idColumn;
	}

	public String getFromClause() {
		return fromClause;
	}
}
