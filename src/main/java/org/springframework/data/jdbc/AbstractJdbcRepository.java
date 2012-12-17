package org.springframework.data.jdbc;

import java.io.Serializable;
import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Implementation of {@link PagingAndSortingRepository} using {@link JdbcTemplate}
 */
public abstract class AbstractJdbcRepository<T extends Persistable<ID>,ID extends Serializable> implements PagingAndSortingRepository<T,ID>{

	private JdbcTemplate jdbcTemplate;
	private String tableName;
	private String idColumn;

	private final String deleteQuery;
	private final String selectAll;
	private final String selectById;
	private final String countQuery;

	RowMapper<T> rowMapper;
	Updater<T> updater;

	public interface Updater<T> {
		void mapColumns(T t,Map<String,Object> mapping);
	}

	public AbstractJdbcRepository(
			RowMapper<T> rowMapper,
			Updater<T> updater,
			String tableName,
			String idColumn,
			JdbcTemplate jdbcTemplate) {

		this.updater = updater;
		this.rowMapper = rowMapper;

		this.jdbcTemplate = jdbcTemplate;
		this.tableName = tableName;
		this.idColumn = idColumn;

		this.deleteQuery = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
		this.selectAll = "SELECT * FROM " + tableName;
		this.selectById = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
		this.countQuery = "SELECT COUNT(" + idColumn + ") FROM " + tableName;

	}

	@Override
	public long count() {
		return jdbcTemplate.queryForLong(this.countQuery);
	}

	@Override
	public void delete(ID id) {
		this.jdbcTemplate.update(this.deleteQuery,id);
	}

	@Override
	public void delete(T entity) {
		this.jdbcTemplate.update(this.deleteQuery, entity.getId());
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		for(T t : entities)
		{
			delete(t);
		}
	}

	@Override
	public void deleteAll() {
		delete(findAll());
	}

	@Override
	public boolean exists(ID id) {
		return findOne(id) != null;
	}

	@Override
	public Iterable<T> findAll() {
		return jdbcTemplate.query(this.selectAll,this.rowMapper);
	}

	@Override
	public T findOne(ID id) {
		List<T> entityOrEmpty = jdbcTemplate.query(selectById, new Object[]{id}, rowMapper);
		return entityOrEmpty.isEmpty()? null : entityOrEmpty.get(0);
	}

	@Override
	public T save(T entity) {

		Map<String,Object> columns = new LinkedHashMap<String, Object>();
		updater.mapColumns(entity, columns);
		if(entity.isNew()) {
			return create(entity, columns);
		} else {
			return update(entity, columns);
		}
	}

	private T update(T entity, Map<String, Object> columns) {
		final Object idValue = columns.remove(idColumn);
		final String updateQuery = buildUpdateQuery(columns);
		columns.put(idColumn, idValue);
		final Object[] queryParams = columns.values().toArray();
		jdbcTemplate.update(updateQuery, queryParams);
		return postUpdate(entity);
	}

	private T create(T entity, Map<String, Object> columns) {
		final String createQuery = buildCreateQuery(columns);
		final Object[] queryParams = columns.values().toArray();
		jdbcTemplate.update(createQuery, queryParams);
		return postCreate(entity);
	}

	protected T postUpdate(T entity) {
		return entity;
	}

	/**
	 * General purpose hook method that is called every time {@link #create} is called with a new entity.
	 *
	 * OVerride this method e.g. if you want to fetch auto-generated key from database
	 * @param entity Entity that was passed to {@link #create}
	 * @return Either the same object as an argument or completely different one
	 */
	protected T postCreate(T entity) {
		return entity;
	}

	private String buildUpdateQuery(Map<String, Object> columns) {
		final StringBuilder updateQuery = new StringBuilder("UPDATE " + this.tableName + " SET ");
		for(Iterator<Map.Entry<String,Object>> iterator = columns.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> column = iterator.next();
			updateQuery.append(column.getKey()).append(" = ?");
			if (iterator.hasNext()) {
				updateQuery.append(", ");
			}
		}
		updateQuery.append(" WHERE ").append(this.idColumn).append(" = ?");
		return updateQuery.toString();
	}

	private String buildCreateQuery(Map<String, Object> columns) {
		final StringBuilder createQuery = new StringBuilder("INSERT INTO " + this.tableName + " (");
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

	@Override
	public Iterable<T> save(Iterable<? extends T> entities) {
		List<T> ret = new ArrayList<T>();
		for(T t : entities)
		{
			ret.add(save(t));
		}
		return ret;
	}

	@Override
	public Iterable<T> findAll(Sort sort) {
		String qu = this.selectAll;

		for(Order o : sort)
		{
			qu += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
		}

		return jdbcTemplate.query(qu,this.rowMapper);
	}


	@Override
	public Page<T> findAll(Pageable page) {
		StringBuilder query = new StringBuilder(this.selectAll);
		applySortingIfRequired(page, query);

		query.
				append(" LIMIT ").
				append(page.getPageNumber() * page.getPageSize()).
				append(", ").
				append(page.getPageSize()).
				append(" ");
		return new PageImpl<T>(jdbcTemplate.query(query.toString(), this.rowMapper), page, count());
	}

	private void applySortingIfRequired(Pageable page, StringBuilder query) {
		if (page.getSort() != null) {
			query.append(" ORDER BY ");
			for(Iterator<Order> iterator = page.getSort().iterator(); iterator.hasNext();) {
				final Order order = iterator.next();
				query.
						append(order.getProperty()).
						append(" ").
						append(order.getDirection().toString());
				if (iterator.hasNext()) {
					query.append(", ");
				}
			}
		}
	}

}


