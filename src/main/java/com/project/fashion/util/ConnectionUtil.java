package com.project.fashion.util;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.project.fashion.exception.DataSourceException;

public class ConnectionUtil {
	private ConnectionUtil() throws DataSourceException {
		throw new DataSourceException("Connection Util Class");
	}

	private static DataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/fashion");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;

	}

	public static JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		DataSource dataSource = getDataSource();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}

}
