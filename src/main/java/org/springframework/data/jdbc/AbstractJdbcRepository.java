package org.springframework.data.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link PagingAndSortingRepository} using {@link JdbcTemplate}
 */
public abstract class AbstractJdbcRepository<T extends Persistable<ID>,ID extends Serializable> implements PagingAndSortingRepository<T,ID>, InitializingBean, BeanFactoryAware {

	private JdbcOperations jdbcOperations;
	private SqlGenerator sqlGenerator;
	private String tableName;
	private String idColumn;

	private RowMapper<T> rowMapper;
	private RowUnmapper<T> rowUnmapper;
	private BeanFactory beanFactory;

	public AbstractJdbcRepository(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, SqlGenerator sqlGenerator, String tableName, String idColumn) {
		this.rowUnmapper = rowUnmapper;
		this.rowMapper = rowMapper;
		this.sqlGenerator = sqlGenerator;
		this.tableName = tableName;
		this.idColumn = idColumn;
	}

	public AbstractJdbcRepository(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, String tableName, String idColumn) {
		this(rowMapper, rowUnmapper, new SqlGenerator(), tableName, idColumn);
	}

	public AbstractJdbcRepository(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, String tableName) {
		this(rowMapper, rowUnmapper, tableName, "id");
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
		return jdbcOperations.queryForLong(sqlGenerator.count(tableName, idColumn));
	}

	@Override
	public void delete(ID id) {
		jdbcOperations.update(sqlGenerator.deleteById(tableName, idColumn), id);
	}

	@Override
	public void delete(T entity) {
		jdbcOperations.update(sqlGenerator.deleteById(tableName, idColumn), entity.getId());
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		for (T t : entities) {
			delete(t);
		}
	}

	@Override
	public void deleteAll() {
		//TODO [tnurkiewicz] Very inefficient
		delete(findAll());
	}

	@Override
	public boolean exists(ID id) {
		//TODO [tnurkiewicz] Consider COUNT(id) == 0
		return findOne(id) != null;
	}

	@Override
	public Iterable<T> findAll() {
		return jdbcOperations.query(sqlGenerator.selectAll(tableName), rowMapper);
	}

	@Override
	public T findOne(ID id) {
		List<T> entityOrEmpty = jdbcOperations.query(sqlGenerator.selectById(tableName, idColumn), new Object[]{id}, rowMapper);
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
		final String updateQuery = sqlGenerator.update(tableName, idColumn, columns);
		columns.put(idColumn, idValue);
		final Object[] queryParams = columns.values().toArray();
		jdbcOperations.update(updateQuery, queryParams);
		return postUpdate(entity);
	}

	private T create(T entity) {
		final Map<String, Object> columns = columns(entity);
		final String createQuery = sqlGenerator.create(tableName, columns);
		final Object[] queryParams = columns.values().toArray();
		jdbcOperations.update(createQuery, queryParams);
		return postCreate(entity);
	}

	private LinkedHashMap<String, Object> columns(T entity) {
		return new LinkedHashMap<String, Object>(rowUnmapper.mapColumns(entity));
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
		return jdbcOperations.query(sqlGenerator.selectAll(tableName, sort), rowMapper);
	}

	@Override
	public Page<T> findAll(Pageable page) {
		String query = sqlGenerator.selectAll(tableName, page);
		return new PageImpl<T>(jdbcOperations.query(query, rowMapper), page, count());
	}

}


