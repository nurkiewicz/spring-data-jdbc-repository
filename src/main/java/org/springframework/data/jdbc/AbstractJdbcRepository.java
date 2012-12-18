package org.springframework.data.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;

/**
 * Implementation of {@link PagingAndSortingRepository} using {@link JdbcTemplate}
 */
public abstract class AbstractJdbcRepository<T extends Persistable<ID>,ID extends Serializable> implements PagingAndSortingRepository<T,ID>, InitializingBean, BeanFactoryAware {

	private JdbcOperations jdbcOperations;
	private String tableName;
	private String idColumn;

	private final String deleteQuery;
	private final String selectAll;
	private final String selectById;
	private final String countQuery;

	private RowMapper<T> rowMapper;
	private Updater<T> updater;
	private BeanFactory beanFactory;

	public AbstractJdbcRepository(RowMapper<T> rowMapper, Updater<T> updater, String tableName, String idColumn) {
		this.updater = updater;
		this.rowMapper = rowMapper;
		this.tableName = tableName;
		this.idColumn = idColumn;
		this.deleteQuery = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
		this.selectAll = "SELECT * FROM " + tableName;
		this.selectById = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
		this.countQuery = "SELECT COUNT(" + idColumn + ") FROM " + tableName;
	}

	public AbstractJdbcRepository(RowMapper<T> rowMapper, Updater<T> updater, String tableName) {
		this(rowMapper, updater, tableName, "id");
	}

		@Override
	public void afterPropertiesSet() throws Exception {
		try {
			jdbcOperations = beanFactory.getBean(JdbcOperations.class);
		} catch (NoSuchBeanDefinitionException e) {
			final DataSource dataSource = beanFactory.getBean(DataSource.class);
			jdbcOperations = new JdbcTemplate(dataSource);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public long count() {
		return jdbcOperations.queryForLong(this.countQuery);
	}

	@Override
	public void delete(ID id) {
		this.jdbcOperations.update(this.deleteQuery, id);
	}

	@Override
	public void delete(T entity) {
		this.jdbcOperations.update(this.deleteQuery, entity.getId());
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
		return jdbcOperations.query(this.selectAll,this.rowMapper);
	}

	@Override
	public T findOne(ID id) {
		List<T> entityOrEmpty = jdbcOperations.query(selectById, new Object[]{id}, rowMapper);
		return entityOrEmpty.isEmpty()? null : entityOrEmpty.get(0);
	}

	@Override
	public T save(T entity) {
		if(entity.isNew()) {
			return create(entity);
		} else {
			return update(entity);
		}
	}

	private T update(T entity) {
		final Map<String, Object> columns = columns(entity);
		final Object idValue = columns.remove(idColumn);
		final String updateQuery = buildUpdateQuery(columns);
		columns.put(idColumn, idValue);
		final Object[] queryParams = columns.values().toArray();
		jdbcOperations.update(updateQuery, queryParams);
		return postUpdate(entity);
	}

	private T create(T entity) {
		final Map<String, Object> columns = columns(entity);
		final String createQuery = buildCreateQuery(columns);
		final Object[] queryParams = columns.values().toArray();
		jdbcOperations.update(createQuery, queryParams);
		return postCreate(entity);
	}

	private LinkedHashMap<String, Object> columns(T entity) {
		return new LinkedHashMap<String, Object>(updater.mapColumns(entity));
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

		return jdbcOperations.query(qu,this.rowMapper);
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
		return new PageImpl<T>(jdbcOperations.query(query.toString(), this.rowMapper), page, count());
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


