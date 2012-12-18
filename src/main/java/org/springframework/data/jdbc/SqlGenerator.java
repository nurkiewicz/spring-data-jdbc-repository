package org.springframework.data.jdbc;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/18/12, 9:18 PM
 */
public class SqlGenerator {

	private String allColumnsClause;

	public SqlGenerator(String allColumnsClause) {
		this.allColumnsClause = allColumnsClause;
	}

	public SqlGenerator() {
		this("*");
	}

	public String count(String tableName, String idColumn) {
		return "SELECT COUNT(" + idColumn + ") FROM " + tableName;
	}

	public String deleteById(String tableName, String idColumn) {
		return "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
	}

	public String selectAll(String tableName) {
		return "SELECT " + allColumnsClause + " FROM " + tableName;
	}

	public String selectAll(String tableName, Pageable page) {
		return selectAll(tableName) +
				sortingClauseIfRequired(page.getSort()) +
				limitClause(page);
	}

	public String selectAll(String tableName, Sort sort) {
		return selectAll(tableName) + sortingClauseIfRequired(sort);
	}

	protected String limitClause(Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize();
		return " LIMIT " + offset + ", " + page.getPageSize();
	}

	public String selectById(String tableName, String idColumn) {
		return selectAll(tableName) + " WHERE " + idColumn + " = ?";
	}

	protected String sortingClauseIfRequired(Sort sort) {
		if (sort == null) {
			return "";
		}
		StringBuilder orderByClause = new StringBuilder();
		orderByClause.append(" ORDER BY ");
		for(Iterator<Sort.Order> iterator = sort.iterator(); iterator.hasNext();) {
			final Sort.Order order = iterator.next();
			orderByClause.
					append(order.getProperty()).
					append(" ").
					append(order.getDirection().toString());
			if (iterator.hasNext()) {
				orderByClause.append(", ");
			}
		}
		return orderByClause.toString();
	}

	public String update(String tableName, String idColumn, Map<String, Object> columns) {
		final StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");
		for(Iterator<Map.Entry<String,Object>> iterator = columns.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> column = iterator.next();
			updateQuery.append(column.getKey()).append(" = ?");
			if (iterator.hasNext()) {
				updateQuery.append(", ");
			}
		}
		updateQuery.append(" WHERE ").append(idColumn).append(" = ?");
		return updateQuery.toString();
	}

	public String create(String tableName, Map<String, Object> columns) {
		final StringBuilder createQuery = new StringBuilder("INSERT INTO " + tableName + " (");
		appendColumnNames(createQuery, columns.keySet());
		createQuery.append(")").append(" VALUES (");
		createQuery.append(repeat("?", ", ", columns.size()));
		return createQuery.append(")").toString();
	}

	private void appendColumnNames(StringBuilder createQuery, Set<String> columnNames) {
		for(Iterator<String> iterator = columnNames.iterator(); iterator.hasNext();) {
			final String column = iterator.next();
			createQuery.append(column);
			if (iterator.hasNext()) {
				createQuery.append(", ");
			}
		}
	}

	// Unfortunately {@link org.apache.commons.lang3.StringUtils} not available
	private static String repeat(String s, String separator, int count) {
		StringBuilder string = new StringBuilder((s.length() + separator.length()) * count);
		while (--count > 0) {
			string.append(s).append(separator);
		}
		return string.append(s).toString();
	}


}
