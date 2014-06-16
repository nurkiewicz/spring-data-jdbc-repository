package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUser;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUserRepository;
import com.nurkiewicz.jdbcrepository.repositories.UserRepository;
import com.nurkiewicz.jdbcrepository.sql.MssqlSqlGenerator;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestMssqlConfig extends JdbcRepositoryTestConfig {

	public static final int MSSQL_PORT = Integer.parseInt(System.getProperty("mssql.port", "1433"));

	@Bean
	@Override
	public CommentRepository commentRepository() {
		return new CommentRepository("comments");
	}

	@Bean
	@Override
	public UserRepository userRepository() {
		return new UserRepository("users");
	}

	@Override
	public BoardingPassRepository boardingPassRepository() {
		return new BoardingPassRepository("boarding_pass");
	}

	@Bean
	public SqlGenerator sqlGenerator() {
		return new MssqlSqlGenerator();
	}

	@Override
	public CommentWithUserRepository commentWithUserRepository() {
		return new CommentWithUserRepository(CommentWithUserRepository.MAPPER,
				new RowUnmapper<CommentWithUser>() {
					@Override
					public Map<String, Object> mapColumns(CommentWithUser comment) {
						Map<String, Object> mapping = new LinkedHashMap<String, Object>();
						mapping.put("ID", comment.getId());
						mapping.put("USER_NAME", comment.getUser().getUserName());
						mapping.put("CONTENTS", comment.getContents());
						mapping.put("CREATED_TIME", new Timestamp(comment.getCreatedTime().getTime()));
						mapping.put("FAVOURITE_COUNT", comment.getFavouriteCount());
						return mapping;
					}
				},
				new CommentWithUserMssqlGenerator(),
				new TableDescription("COMMENTS", "COMMENTS c JOIN USERS u ON c.USER_NAME = u.USER_NAME", "ID")
		);
	}

	@Bean
	@Override
	public DataSource dataSource() {
		JtdsDataSource ds = new JtdsDataSource();
		ds.setUser(System.getProperty("mssql.user", "unittest"));
		ds.setPassword(System.getProperty("mssql.password"));
		ds.setInstance(System.getProperty("mssql.instance"));
		ds.setServerName(System.getProperty("mssql.hostname", "localhost"));
		ds.setDatabaseName("spring_data_jdbc_repository_test");
		return ds;
	}

}
