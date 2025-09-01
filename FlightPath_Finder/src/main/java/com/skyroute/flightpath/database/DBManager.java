package com.skyroute.flightpath.database;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBManager {

	@Autowired
	private DataSource dataSource;

	public final JdbcTemplate getConnection() {
		try (var connection = dataSource.getConnection()) {
			System.out.println("Connected to DB: " + connection.getMetaData().getURL());
			return new JdbcTemplate(dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
