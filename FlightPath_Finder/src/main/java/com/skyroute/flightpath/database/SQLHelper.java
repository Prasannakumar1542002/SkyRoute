package com.skyroute.flightpath.database;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class SQLHelper extends ORMHelper {

	@Autowired
	private DataSource dataSource;
	private static Connection con = null;

	@Autowired
	public SQLHelper(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
		if (con == null) {
			try {
				con = dataSource.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static ResultSet executePreparedStatement(String query, List<Object> parameters) {
		try {
			final PreparedStatement ps = con.prepareStatement(query);
			int[] index = { 1 };
			parameters.forEach(value -> {
				try {
					if (value instanceof Integer) {
						ps.setInt(index[0]++, (Integer) value);
					} else if (value instanceof String) {
						ps.setString(index[0]++, (String) value);
					} else if (value instanceof Double) {
						ps.setDouble(index[0]++, (Double) value);
					} else if (value instanceof Timestamp) {
						ps.setTimestamp(index[0]++, (Timestamp) value);
					} else {
						ps.setObject(index[0]++, value); // fallback
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			return ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// convert list of json string to list of map
	public static List<Map<String, Object>> convertJsontoListOfMap(String jsonStrings) {
		/*
		 * jsonStrings =
		 * "[{\"ordersummaryid\":\"18467\",\"orderid\":null,\"itemid\":null,...}]"
		 * result =
		 * [{\"ordersummaryid\":\"18467\",\"orderid\":null,\"itemid\":null,...}]
		 */
		Gson gson = new Gson();
		Type type = new TypeToken<List<Map<String, Object>>>() {
		}.getType();
		return gson.fromJson(jsonStrings, type);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.finalize();
	}
}
