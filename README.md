# Generic DAO implementation based on `JdbcTemplate`

Compatible with Spring Data [`PagingAndSortingRepository`](http://static.springsource.org/spring-data/data-commons/docs/1.1.0.RELEASE/api/org/springframework/data/repository/PagingAndSortingRepository.html) abstraction, all these methods are implemented for you:

	public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
		         T  save(T entity);
		Iterable<T> save(Iterable<? extends T> entities);
		         T  findOne(ID id);
		    boolean exists(ID id);
		Iterable<T> findAll();
		       long count();
		       void delete(ID id);
		       void delete(T entity);
		       void delete(Iterable<? extends T> entities);
		       void deleteAll();
		Iterable<T> findAll(Sort sort);
		    Page<T> findAll(Pageable pageable);
	}

`Pageable` and `Sort` parameters are also fully supported, which means you get paging and sorting by arbitrary properties for free.

## Design objectives

* Lightweight, fast and low-overhead. Only a handful of classes, **no XML, annotations, reflection**
* **This is not full-blown ORM**. No mapping, relationship handling, lazy loading, dirty checking
* CRUD implemented in seconds
* For small applications where JPA is an overkill
* Use when simplicity is needed or when future migration e.g. to JPA is considered

## Contributions

Original implementation based on [this Gist](https://gist.github.com/1206700) by [*sheenobu*](https://github.com/sheenobu).

## Continuous integration

[![Build Status](https://secure.travis-ci.org/nurkiewicz/spring-data-jdbc-repository.png?branch=master)](https://travis-ci.org/nurkiewicz/spring-data-jdbc-repository)

## License
This project is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0) (same as [Spring framework](https://github.com/SpringSource/spring-framework)).
