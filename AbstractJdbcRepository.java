
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Implementation of PagingAndSortingRepository using JdbcTemplate
 */
public abstract class AbstractJdbcRepository<T extends BaseModel,ID extends Serializable> implements PagingAndSortingRepository<T,ID>{

	private JdbcTemplate jdbcTemplate;
	private String tableName;
	private String idColumn;
	
	private String deleteQuery;
	private String selectAll;
	private String selectById;
	private String countQuery;	
	
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
	public T findOne(ID arg0) {
		return jdbcTemplate.queryForObject(this.selectById,new Object[]{arg0},this.rowMapper);
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
		
		obj[i++] = arg0.getId(); 
		
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
	public Page<T> findAll(Pageable arg0) {
		String qu = this.selectAll;
		
		for(Order o : arg0.getSort())
		{
			qu += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
		}
		
		int pageSize = 10;
		
		qu += " LIMIT " + arg0.getPageNumber()*pageSize + " " + pageSize + " ";
		
		
		long count = count();
		
		return new JdbcPage<T>(arg0,
			(int) count/pageSize,
			(int) count,
			jdbcTemplate.query(qu,this.rowMapper));
	}

}


class JdbcPage<T> implements Page<T>
{

	Pageable pageable;
	int totalPages;
	int numberOfElements;
	int totalNumbers;
	List<T> content;	
	
	
	
	public JdbcPage(Pageable pageable, int totalPages, 
			int totalNumbers, List<T> content) {
		super();
		this.pageable = pageable;
		this.totalPages = totalPages;		
		this.totalNumbers = totalNumbers;
		this.content = content;
	}

	@Override
	public int getNumber() {
		return pageable.getPageNumber();
	}

	@Override
	public int getSize() {
		return pageable.getPageSize();
	}

	@Override
	public int getTotalPages() {
		return this.totalPages;
	}

	//number on current page
	@Override
	public int getNumberOfElements() {
		return this.getContent().size();
	}

	@Override
	public long getTotalElements() {
		return this.totalNumbers;
	}

	@Override
	public boolean hasPreviousPage() {
		return pageable.getPageNumber() != 0;
	}

	@Override
	public boolean isFirstPage() {
		return pageable.getPageNumber() == 0;
	}

	@Override
	public boolean hasNextPage() {
		return pageable.getPageNumber() != totalPages;
	}

	@Override
	public boolean isLastPage() {
		return pageable.getPageNumber() == totalPages;
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public List<T> getContent() {
		return content;
	}

	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

	@Override
	public Sort getSort() {
		return this.pageable.getSort();
	}
	
}