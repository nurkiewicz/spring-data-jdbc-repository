# Generic DAO implementation based on `JdbcTemplate`

The purpose of this project is to provide generic, lightweight and easy to use DAO implementation for relational databases based on [`JdbcTemplate`](http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/jdbc/core/JdbcTemplate.html) from [Spring framework](http://www.springsource.org/spring-framework).

## Design objectives

* Lightweight, fast and low-overhead. Only a handful of classes, **no XML, annotations, reflection**
* **This is not full-blown ORM**. No relationship handling, lazy loading, dirty checking, caching
* CRUD implemented in seconds
* For small applications where JPA is an overkill
* Use when simplicity is needed or when future migration e.g. to JPA is considered
* Minimalistic support for database dialect differences (e.g. transparent paging of results)

## Features

Each DAO provides built-in support for

* Mapping to/from domain objects through [`RowMapper`](http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/jdbc/core/RowMapper.html) abstraction
* Generated and user-defined primary keys
* Extracting generated key
* Compound (multi-column) primary keys
* Can work with immutable domain objects
* Paging (requesting subset of results)
* Sorting over several columns (database agnostic)
* Optional support for *many-to-one* relationships
* Supported databases (continuously tested)
	* MySQL
	* PostgreSQL
	* H2
	* HSQLDB
	* Derby
* Easily extendable to other database dialects via [`SqlGenerator`](https://github.com/nurkiewicz/spring-data-jdbc-repository/blob/master/src/main/java/org/springframework/data/jdbc/sql/SqlGenerator.java) class.

## API

Compatible with Spring Data [`PagingAndSortingRepository`](http://static.springsource.org/spring-data/data-commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html) abstraction, all these methods are implemented for you:

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

## Reasons to use

* You consider migration to JPA or even some NoSQL database in the future.

	Since your code will rely only on methods defined in [`PagingAndSortingRepository`](http://static.springsource.org/spring-data/data-commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html) and [`CrudRepository`](http://static.springsource.org/spring-data/data-commons/docs/current/api/org/springframework/data/repository/CrudRepository.html) from [Spring Data Commons](http://www.springsource.org/spring-data/commons) umbrella project you are free to switch from [`AbstractJdbcRepository`](https://github.com/nurkiewicz/spring-data-jdbc-repository/blob/master/src/main/java/org/springframework/data/jdbc/AbstractJdbcRepository.java) implementation (from this project) to: [`JpaRepository`](http://static.springsource.org/spring-data/data-jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html), [`MongoRepository`](http://static.springsource.org/spring-data/data-mongodb/docs/current/api/org/springframework/data/mongodb/repository/MongoRepository.html), [`GemfireRepository`](http://static.springsource.org/spring-data-gemfire/docs/current/api/org/springframework/data/gemfire/repository/GemfireRepository.html) or [`GraphRepository`](http://static.springsource.org/spring-data/data-graph/docs/current/api/org/springframework/data/neo4j/repository/GraphRepository.html). They all implement the same common API. Of course don't expect that switching from JDBC to JPA or MongoDB will be as simple as switching imported JAR dependencies - but at least you minimize the impact by using same DAO API.

* You need a fast, simple JDBC wrapper library. JPA or even [MyBatis](http://blog.mybatis.org/) is an overkill

* You want to have full control over generated SQL if needed

* You want to work with objects, but don't need lazy loading, relationship handling, multi-level caching, dirty checking... You need [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) and not much more

* You want to by [*DRY*](http://en.wikipedia.org/wiki/Don't_repeat_yourself)

* You are already using Spring or maybe even `JdbcTemplate`, but still feel like there is too much manual work

* You have very few database tables

## Contributions

..are always welcome. Don't hesitate to [submit bug reports](https://github.com/nurkiewicz/spring-data-jdbc-repository/issues) and [pull requests](https://github.com/nurkiewicz/spring-data-jdbc-repository/pulls). Biggest missing feature now is support for MSSQL and Oracle databases. It would be terrific if someone could have a look at it.

Original implementation was based on [this Gist](https://gist.github.com/1206700) by [*sheenobu*](https://github.com/sheenobu).

### Testing

This library is continuously tested using Travis ([![Build Status](https://secure.travis-ci.org/nurkiewicz/spring-data-jdbc-repository.png?branch=master)](https://travis-ci.org/nurkiewicz/spring-data-jdbc-repository)). Test suite consists of **265 tests** (53 distinct tests each run against 5 different databases: MySQL, PostgreSQL, H2, HSQLDB and Derby.

When filling [bug reports](https://github.com/nurkiewicz/spring-data-jdbc-repository/issues) or submitting new features please try including supporting test cases. Each [pull request](https://github.com/nurkiewicz/spring-data-jdbc-repository/pulls) is automatically tested on a separate branch.

## License
This project is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0) (same as [Spring framework](https://github.com/SpringSource/spring-framework)).
