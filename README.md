# Spring Data generic JDBC DAO implementation

The purpose of this project is to provide generic, lightweight and easy to use DAO implementation for relational databases based on [`JdbcTemplate`](http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/jdbc/core/JdbcTemplate.html) from [Spring framework](http://www.springsource.org/spring-framework).

## Design objectives

* Lightweight, fast and low-overhead. Only a handful of classes, **no XML, annotations, reflection**
* **This is not full-blown ORM**. No relationship handling, lazy loading, dirty checking, caching
* CRUD implemented in seconds
* For small applications where JPA is an overkill
* Use when simplicity is needed or when future migration e.g. to JPA is considered
* Minimalistic support for database dialect differences (e.g. transparent paging of results)

## Features

Each DAO provides built-in support for:

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
* Easy retrieval of records by ID and total count

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

`Pageable` and `Sort` parameters are also fully supported, which means you get paging and sorting by arbitrary properties for free. For example say you have `userRepository` extending `PagingAndSortingRepository<User, String>` interface (implemented for you by the library) and you request 5th page of `USERS` table, 10 per page:

	Page<User> page = userRepository.findAll(
		new PageRequest(
			5, 10, 
			new Sort(
				new Order(DESC, "reputation"), 
				new Order(ASC, "user_name")
			)
		)
	);

Spring Data JDBC repository library will translate this call into (PostgreSQL syntax):

	SELECT *
	FROM USERS
	ORDER BY reputation DESC, user_name ASC
	LIMIT 50 OFFSET 10

...or even (Derby):

	SELECT * FROM (
		SELECT ROW_NUMBER() OVER () AS ROW_NUM, t.*
		FROM (
			SELECT * 
			FROM USERS 
			ORDER BY reputation DESC, user_name ASC
			) AS t
		) AS a 
	WHERE ROW_NUM BETWEEN 51 AND 60

No matter which database you use, you'll get `Page<User>` object in return (you still ave to provide `RowMapper<User>` yourself to translate from `ResultSet` to domain object. If you don't know Spring Data project yet, [`Page<T>`](http://static.springsource.org/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html) is a wonderful abstraction, not only encapsulating `List<User>`, but also providing metadata such as total number of records, on which page we currently are, etc.

## Reasons to use

* You consider migration to JPA or even some NoSQL database in the future.

	Since your code will rely only on methods defined in [`PagingAndSortingRepository`](http://static.springsource.org/spring-data/data-commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html) and [`CrudRepository`](http://static.springsource.org/spring-data/data-commons/docs/current/api/org/springframework/data/repository/CrudRepository.html) from [Spring Data Commons](http://www.springsource.org/spring-data/commons) umbrella project you are free to switch from [`AbstractJdbcRepository`](https://github.com/nurkiewicz/spring-data-jdbc-repository/blob/master/src/main/java/org/springframework/data/jdbc/AbstractJdbcRepository.java) implementation (from this project) to: [`JpaRepository`](http://static.springsource.org/spring-data/data-jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html), [`MongoRepository`](http://static.springsource.org/spring-data/data-mongodb/docs/current/api/org/springframework/data/mongodb/repository/MongoRepository.html), [`GemfireRepository`](http://static.springsource.org/spring-data-gemfire/docs/current/api/org/springframework/data/gemfire/repository/GemfireRepository.html) or [`GraphRepository`](http://static.springsource.org/spring-data/data-graph/docs/current/api/org/springframework/data/neo4j/repository/GraphRepository.html). They all implement the same common API. Of course don't expect that switching from JDBC to JPA or MongoDB will be as simple as switching imported JAR dependencies - but at least you minimize the impact by using same DAO API.

* You need a fast, simple JDBC wrapper library. JPA or even [MyBatis](http://blog.mybatis.org/) is an overkill

* You want to have full control over generated SQL if needed

* You want to work with objects, but don't need lazy loading, relationship handling, multi-level caching, dirty checking... You need [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) and not much more

* You want to by [*DRY*](http://en.wikipedia.org/wiki/Don't_repeat_yourself)

* You are already using Spring or maybe even `JdbcTemplate`, but still feel like there is too much manual work

* You have very few database tables

## Quick start

### Prerequisites

Maven coordinates:

	<dependency>
		<groupId>com.blogspot.nurkiewicz</groupId>
		<artifactId>jdbcrepository</artifactId>
		<version>0.0.1</version>
	</dependency>

Unfortunately the project **is not yet in maven central repository**. For the time being you can install the library in your local repository by cloning it:

	$ git clone git://github.com/nurkiewicz/spring-data-jdbc-repository.git
	$ git checkout 0.0.1
	$ mvn clean install

In order to start your project must have `DataSource` bean present and transaction management enabled. Here is a minimal MySQL configuration:

	@EnableTransactionManagement
	@Configuration
	public class MinimalConfig {

		@Bean
		public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(dataSource());
		}

		@Bean
		public DataSource dataSource() {
			MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
			ds.setUser("user");
			ds.setPassword("secret");
			ds.setDatabaseName("db_name");
			return ds;
		}

	}


### Entity with auto-generated key

Say you have a following database table with auto-generated key (MySQL syntax):

	CREATE TABLE COMMENTS (
		id INT AUTO_INCREMENT,
		user_name varchar(256),
		contents varchar(1000),
		created_time TIMESTAMP NOT NULL,
		PRIMARY KEY (id)
	);

First you need to create domain object mapping to that table (just like in any other ORM):

	public class Comment implements Persistable<Integer> {

		private Integer id;
		private String userName;
		private String contents;
		private Date createdTime;

		@Override
		public Integer getId() {
			return id;
		}

		@Override
		public boolean isNew() {
			return id == null;
		}
		
		//getters/setters/constructors/...
	}

Apart from standard Java boilerplate you should notice implementing [`Persistable<Integer>`](http://static.springsource.org/spring-data/commons/docs/current/api/org/springframework/data/domain/Persistable.html) where `Integer` is the type of primary key. `Persistable<T>` is an interface coming from Spring Data project and it's the only requirement we place on your domain object.

Finally we are ready to create our DAO:

	@Repository
	public class CommentRepository extends JdbcRepository<Comment, Integer> {

		public CommentRepository() {
			super(ROW_MAPPER, ROW_UNMAPPER, "COMMENTS");
		}

		public static final RowMapper<Comment> ROW_MAPPER = //...

		private static final RowUnmapper<Comment> ROW_UNMAPPER = //...

		@Override
		protected Comment postCreate(Comment entity, Number generatedId) {
			entity.setId(generatedId.intValue());
			return entity;
		}
	}

It's quite a lot of code, so let's go step by step. First of all I use [`@Repository`](http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/stereotype/Repository.html) annotation to mark DAO bean. It enables persistence exception translation. Also such annotated classes are discovered by CLASSPATH scanning.

As you can see we extend `JdbcRepository<Comment, Integer>` which is the central class of this library, providing implementations of all `PagingAndSortingRepository` methods. Its constructor has three required dependencies: `RowMapper`, `RowUnmapper` and table name. You may also provide ID column name, otherwise default `"id"` is used.

### Entity with manually assigned key

### Compound primary key

### Transactions

This library is completely orthogonal to transaction management. Every method of each repository requires running transaction and it's up to you to set it up. Typically you would place `@Transactional` on service layer (calling DAO beans). I don't recommend [placing `@Transactional` over every DAO](http://stackoverflow.com/questions/8993318/what-is-the-right-way-to-use-spring-mvc-with-hibernate-in-dao-sevice-layer-arch).

## Contributions

..are always welcome. Don't hesitate to [submit bug reports](https://github.com/nurkiewicz/spring-data-jdbc-repository/issues) and [pull requests](https://github.com/nurkiewicz/spring-data-jdbc-repository/pulls). Biggest missing feature now is support for MSSQL and Oracle databases. It would be terrific if someone could have a look at it.

Original implementation was based on [this Gist](https://gist.github.com/1206700) by [*sheenobu*](https://github.com/sheenobu).

### Testing

This library is continuously tested using Travis ([![Build Status](https://secure.travis-ci.org/nurkiewicz/spring-data-jdbc-repository.png?branch=master)](https://travis-ci.org/nurkiewicz/spring-data-jdbc-repository)). Test suite consists of **265 tests** (53 distinct tests each run against 5 different databases: MySQL, PostgreSQL, H2, HSQLDB and Derby.

When filling [bug reports](https://github.com/nurkiewicz/spring-data-jdbc-repository/issues) or submitting new features please try including supporting test cases. Each [pull request](https://github.com/nurkiewicz/spring-data-jdbc-repository/pulls) is automatically tested on a separate branch.

## License
This project is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0) (same as [Spring framework](https://github.com/SpringSource/spring-framework)).
