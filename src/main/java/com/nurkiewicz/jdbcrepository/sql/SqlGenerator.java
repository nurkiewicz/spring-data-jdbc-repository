package com.nurkiewicz.jdbcrepository.sql;

import com.nurkiewicz.jdbcrepository.TableDescription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/18/12, 9:18 PM
 */
public class SqlGenerator {

	public static final String WHERE = " WHERE ";
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	public static final String SELECT = "SELECT ";
	public static final String FROM = "FROM ";
	public static final String DELETE = "DELETE ";
	public static final String COMMA = ", ";
	public static final String PARAM = " = ?";
	private String allColumnsClause;

	public SqlGenerator(String allColumnsClause) {
		this.allColumnsClause = allColumnsClause;
	}

	public SqlGenerator() {
		this("*");
	}

	public String count(TableDescription table) {
		return SELECT + "COUNT(*) " + FROM + table.getFromClause();
	}

	public String deleteById(TableDescription table) {
		return DELETE + FROM + table.getName() + whereByIdClause(table);
	}

	private String whereByIdClause(TableDescription table) {
		final StringBuilder whereClause = new StringBuilder(WHERE);
		for (Iterator<String> idColIterator = table.getIdColumns().iterator(); idColIterator.hasNext(); ) {
			whereClause.append(idColIterator.next()).append(PARAM);
			if (idColIterator.hasNext()) {
				whereClause.append(AND);
			}
		}
		return whereClause.toString();
	}

	private String whereByIdsClause(TableDescription table, int idsCount) {
		final List<String> idColumnNames = table.getIdColumns();
		if (idColumnNames.size() > 1) {
			return whereByIdsWithMultipleIdColumns(idsCount, idColumnNames);
		} else {
			return whereByIdsWithSingleIdColumn(idsCount, idColumnNames.get(0));
		}
	}

	private String whereByIdsWithMultipleIdColumns(int idsCount, List<String> idColumnNames) {
		int idColumnsCount = idColumnNames.size();
		final StringBuilder whereClause = new StringBuilder(WHERE);
		final int totalParams = idsCount * idColumnsCount;
		for (int idColumnIdx = 0; idColumnIdx < totalParams; idColumnIdx += idColumnsCount) {
			if (idColumnIdx > 0) {
				whereClause.append(OR);
			}
			whereClause.append("(");
			for (int i = 0; i < idColumnsCount; ++i) {
				if (i > 0) {
					whereClause.append(AND);
				}
				whereClause.append(idColumnNames.get(i)).append(" = ?");
			}
			whereClause.append(")");
		}
		return whereClause.toString();
	}

	private String whereByIdsWithSingleIdColumn(int idsCount, String idColumn) {
		final StringBuilder whereClause = new StringBuilder(WHERE);
		return whereClause.
				append(idColumn).
				append(" IN (").
				append(repeat("?", COMMA, idsCount)).
				append(")").
				toString();
	}

	public String selectAll(TableDescription table) {
		return SELECT + allColumnsClause + " " + FROM + table.getFromClause();
	}

	public String selectAll(TableDescription table, Pageable page) {
		return selectAll(table, page.getSort()) + limitClause(page);
	}

	public String selectAll(TableDescription table, Sort sort) {
		return selectAll(table) + sortingClauseIfRequired(sort);
	}

	protected String limitClause(Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize();
		return " LIMIT " + offset + COMMA + page.getPageSize();
	}

	public String selectById(TableDescription table) {
		return selectAll(table) + whereByIdClause(table);
	}

	public String selectByIds(TableDescription table, int idsCount) {
		switch (idsCount) {
			case 0:
				return selectAll(table);
			case 1:
				return selectById(table);
			default:
				return selectAll(table) + whereByIdsClause(table, idsCount);
		}
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
				orderByClause.append(COMMA);
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
				updateQuery.append(COMMA);
			}
		}
		updateQuery.append(whereByIdClause(table));
		return updateQuery.toString();
	}

	public String create(TableDescription table, Map<String, Object> columns) {
		final StringBuilder createQuery = new StringBuilder("INSERT INTO " + table.getName() + " (");
		appendColumnNames(createQuery, columns.keySet());
		createQuery.append(")").append(" VALUES (");
		createQuery.append(repeat("?", COMMA, columns.size()));
		return createQuery.append(")").toString();
	}

	private void appendColumnNames(StringBuilder createQuery, Set<String> columnNames) {
		for(Iterator<String> iterator = columnNames.iterator(); iterator.hasNext();) {
			final String column = iterator.next();
			createQuery.append(column);
			if (iterator.hasNext()) {
				createQuery.append(COMMA);
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
		return DELETE + FROM + table.getName();
	}

	public String countById(TableDescription table) {
		return count(table) + whereByIdClause(table);
	}

	public String getAllColumnsClause() {
		return allColumnsClause;
	}
}
