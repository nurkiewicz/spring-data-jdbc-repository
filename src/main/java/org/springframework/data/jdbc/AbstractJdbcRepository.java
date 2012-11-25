package org.springframework.data.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Implementation of PagingAndSortingRepository using JdbcTemplate
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
		public void mapColumns(T t,Map<String,Object> mapping);	
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
		
		this.deleteQuery = String.format("delete from %s where %s = ?",tableName,idColumn);
		this.selectAll = String.format("select * from %s",tableName);
		this.selectById = String.format("select * from %s where %s = ?",tableName,idColumn);
		this.countQuery = String.format("select count(%s) from %s",idColumn, tableName);
	
	}
	
	@Override
	public long count() {
		return jdbcTemplate.queryForLong(this.countQuery);
	}

	@Override
	public void delete(ID arg0) {
		this.jdbcTemplate.update(this.deleteQuery,arg0);
	}

	@Override
	public void delete(T arg0) {
		this.jdbcTemplate.update(this.deleteQuery,arg0.getId());
	}

	@Override
	public void delete(Iterable<? extends T> arg0) {
		for(T t : arg0)
		{
			delete(t);
		}
	}

	@Override
	public void deleteAll() {
		delete(findAll());
	}

	@Override
	public boolean exists(ID arg0) {
		return findOne(arg0) != null;
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
	public T save(T arg0) {

		Map<String,Object> columns = new HashMap<String, Object>();
		updater.mapColumns(arg0, columns);

		String updateQuery = String.format("update %s set ",this.tableName);
		
		Object[] obj = new Object[columns.size()];
		int i = 0;
		
		for(Map.Entry<String,Object> e : columns.entrySet())
		{
			obj[i++] = e.getValue();
			updateQuery += " "  + e.getKey() + " = ? "; 
		}
		
		obj[i] = arg0.getId();
		
		updateQuery += String.format(" where %s = ? ",this.idColumn);
				
		jdbcTemplate.update(updateQuery,obj);		
		
		return arg0;
	}

	@Override
	public Iterable<T> save(Iterable<? extends T> arg0) {
		List<T> ret = new ArrayList<T>();
		for(T t : arg0)
		{
			ret.add(save(t));
		}
		return ret;
	}

	@Override
	public Iterable<T> findAll(Sort arg0) {
		String qu = this.selectAll;
		
		for(Order o : arg0)
		{
			qu += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
		}
		
		return jdbcTemplate.query(qu,this.rowMapper);
	}


	@Override
	public Page<T> findAll(Pageable page) {
		StringBuilder query = new StringBuilder(this.selectAll);
		if (page.getSort() != null) {
			for(Order o : page.getSort())
			{
				query.
						append(" ORDER BY ").
						append(o.getProperty()).
						append(" ").
						append(o.getDirection().toString()).
						append(" ");
			}
		}

		query.
				append(" LIMIT ").
				append(page.getPageNumber() * page.getPageSize()).
				append(", ").
				append(page.getPageSize()).
				append(" ");
		return new PageImpl<T>(jdbcTemplate.query(query.toString(), this.rowMapper), page, count());
	}

}


