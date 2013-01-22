package com.blogspot.nurkiewicz.jdbcrepository.mssql;

import com.blogspot.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.BoardingPassRepository;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.CommentRepository;
import com.blogspot.nurkiewicz.jdbcrepository.repositories.UserRepository;
import com.blogspot.nurkiewicz.jdbcrepository.sql.MssqlSql2012Generator;
import com.blogspot.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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
        return new MssqlSql2012Generator();
    }

    @Bean
    @Override
    public DataSource dataSource() {
        JtdsDataSource ds = new JtdsDataSource();
        ds.setUser("unittest");
        ds.setPassword(System.getProperty("mssql.password"));
        ds.setInstance(System.getProperty("mssql.instance"));
        ds.setServerName("localhost");
        ds.setDatabaseName("spring_data_jdbc_repository_test");
        return ds;
    }

}
