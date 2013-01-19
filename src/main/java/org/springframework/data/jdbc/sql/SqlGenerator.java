package org.springframework.data.jdbc.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.TableDescription;
import org.springframework.util.StringUtils;

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

	public String count(TableDescription table) {
		return "SELECT COUNT(*) FROM " + table.getFromClause();
	}

	public String deleteById(TableDescription table) {
		return "DELETE FROM " + table.getName() + whereByIdClause(table);
	}

	private String whereByIdClause(TableDescription table) {
		final String idColumnNames = StringUtils.collectionToCommaDelimitedString(table.getIdColumns());
		return " WHERE " + idColumnNames + " = ?";
	}

	public String selectAll(TableDescription table) {
		return "SELECT " + allColumnsClause + " FROM " + table.getFromClause();
	}

	public String selectAll(TableDescription table, Pageable page) {
		return selectAll(table, page.getSort()) + limitClause(page);
	}

	public String selectAll(TableDescription table, Sort sort) {
		return selectAll(table) + sortingClauseIfRequired(sort);
	}

	protected String limitClause(Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize();
		return " LIMIT " + offset + ", " + page.getPageSize();
	}

	public String selectById(TableDescription table) {
		return selectAll(table) + whereByIdClause(table);
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

	public String update(TableDescription table, Map<String, Object> columns) {
		final StringBuilder updateQuery = new StringBuilder("UPDATE " + table.getName() + " SET ");
		for(Iterator<Map.Entry<String,Object>> iterator = columns.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> column = iterator.next();
			updateQuery.append(column.getKey()).append(" = ?");
			if (iterator.hasNext()) {
				updateQuery.append(", ");
			}
		}
		updateQuery.append(whereByIdClause(table));
		return updateQuery.toString();
	}

	public String create(TableDescription table, Map<String, Object> columns) {
		final StringBuilder createQuery = new StringBuilder("INSERT INTO " + table.getName() + " (");
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


	public String deleteAll(TableDescription table) {
		return "DELETE FROM " + table.getName();
	}

	public String countById(TableDescription table) {
		return count(table) + whereByIdClause(table);
	}

	public String getAllColumnsClause() {
		return allColumnsClause;
	}
}
