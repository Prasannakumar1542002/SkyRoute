package com.skyroute.flightpath.database;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public abstract class ORMHelper {

	/**
	 * Fetches all column names from the specified table using
	 * {@link ResultSetMetaData}. Executes a query with `LIMIT 1` to get metadata
	 * without scanning the entire table.
	 *
	 * @param tableName    the name of the table from which to fetch column names
	 * @param jdbcTemplate the {@link JdbcTemplate} used to execute the query
	 * @return a list of column names in the table
	 * @throws RuntimeException if any SQL error occurs while reading metadata
	 */
	public static List<String> fetchTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
		String sql = "SELECT * FROM " + tableName + " LIMIT 1";
		return jdbcTemplate.query(sql, (ResultSetExtractor<List<String>>) rs -> {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			return IntStream.rangeClosed(1, columnCount).mapToObj(i -> {
				try {
					return metaData.getColumnName(i);
				} catch (SQLException e) {
					throw new RuntimeException("Error fetching column name at index " + i, e);
				}
			}).collect(Collectors.toList());
		});
	}

	/**
	 * Creates a {@link TableDTO} object using the given table name and field-value
	 * mappings.
	 *
	 * @param tableName the name of the table
	 * @param tableRows a map representing column-value pairs
	 * @return a new {@link TableDTO} instance
	 */
	private static TableDTO buildDTO(String tableName, Map<String, Object> tableRows) {
		return new TableDTO(tableName, tableRows);
	}

	/**
	 * Fetches all rows from the specified table and returns a list of
	 * {@link TableDTO} objects.
	 *
	 * @param tableName    the name of the table to query
	 * @param jdbcTemplate the {@link JdbcTemplate} to execute the query
	 * @return a list of {@link TableDTO} objects, each representing one row
	 */
	public static List<TableDTO> getAllRecords(String tableName, JdbcTemplate jdbcTemplate) {
		List<String> columnNames = fetchTableColumns(tableName, jdbcTemplate);
		List<TableDTO> tableDTO = jdbcTemplate.query("SELECT * FROM " + tableName, (rs, rowNum) -> {
			TableDTO table = buildDTO(tableName, new HashMap<String, Object>());
			final int[] count = { 1 };
			columnNames.forEach((String fieldName) -> {
				try {
					table.setFields(fieldName, rs.getObject(count[0]++));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			return table;
		});
		return tableDTO;
	}

	/**
	 * Retrieves a single row from the given table using the specified primary
	 * key(s), and maps it into a {@link TableDTO}. If no row is found, returns
	 * null.
	 *
	 * @param tableName    the name of the table to query
	 * @param jdbcTemplate the {@link JdbcTemplate} to execute the query
	 * @param pkFields     a map containing primary key column(s) and value(s)
	 * @return a {@link TableDTO} object representing the row, or null if not found
	 */
	public static TableDTO getSingleRecord(String tableName, JdbcTemplate jdbcTemplate, Map<String, String> pkFields) {
		StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
		pkFields.forEach((String fieldName, String fieldValue) -> query
				.append(fieldName.concat("='".concat(fieldValue.concat("' AND ")))));
		String sqlQuery = query.substring(0, query.length() - 5);
		List<String> columnNames = fetchTableColumns(tableName, jdbcTemplate);
		return jdbcTemplate.query(sqlQuery, rs -> {
			List<TableDTO> tableDTOList = new ArrayList<>();
			if (rs.next()) {
				Map<String, Object> resultMap = new HashMap<>();
				columnNames.forEach((String column) -> {
					try {
						resultMap.put(column.toLowerCase(), rs.getObject(column));
					} catch (SQLException e) {
						if (e.getMessage().contains("Zero date value prohibited")) {
							// fallback logic
							resultMap.put(column, null);
						} else {
							System.err.println("Column not found in ResultSet: " + column);
							throw new RuntimeException("Error reading column '" + column + "'", e);
						}
					}
				});
				tableDTOList.add(buildDTO(tableName, resultMap));
			}
			if (tableDTOList.size() <= 0) {
				tableDTOList.add(null);
			}
			return tableDTOList; // No rows returned
		}).get(0);
	}

	/**
	 * Retrieves multiple rows from the given table using the specified conditions
	 * (typically primary or foreign keys), and maps each row into a
	 * {@link TableDTO}.
	 *
	 * @param tableName    the name of the table to query
	 * @param jdbcTemplate the {@link JdbcTemplate} to execute the query
	 * @param pkFields     a map of field names and their corresponding filter
	 *                     values
	 * @return a list of {@link TableDTO} objects for the matched rows
	 */
	public static List<TableDTO> getMultipleRecord(String tableName, JdbcTemplate jdbcTemplate,
			Map<String, String> pkFields) {
		StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
		pkFields.forEach((String fieldName, String fieldValue) -> query
				.append(fieldName.concat("='".concat(fieldValue.concat("' AND ")))));
		String sqlQuery = query.substring(0, query.length() - 5);
		List<String> columnNames = fetchTableColumns(tableName, jdbcTemplate);
		return jdbcTemplate.query(sqlQuery, rs -> {
			List<TableDTO> tableDTOList = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<>();
				columnNames.forEach((String column) -> {
					try {
						resultMap.put(column.toLowerCase(), rs.getObject(column));
					} catch (SQLException e) {
						if (e.getMessage().contains("Zero date value prohibited")) {
							// fallback logic
							resultMap.put(column, null);
						} else {
							System.err.println("Column not found in ResultSet: " + column);
							throw new RuntimeException("Error reading column '" + column + "'", e);
						}
					}
				});
				tableDTOList.add(buildDTO(tableName, resultMap));
			}
			return tableDTOList; // No rows returned
		});
	}

	/**
	 * Dynamically builds and executes an SQL UPDATE query using the provided
	 * parameters.
	 * <p>
	 * This method uses the given table name, a map of columns to update, and a map
	 * of primary key conditions to construct a parameterized SQL update query and
	 * execute it using the provided {@link JdbcTemplate}. The method ensures SQL
	 * injection safety by using `?` placeholders.
	 * </p>
	 *
	 * <pre>{@code
	 * Example usage:
	 * 
	 * Map<String, Object> primaryKeyMap = Map.of("orderid", 2793, "branchid", "B100051");
	 * Map<String, Object> updateMap = Map.of("payment", 5000);
	 * String tableName = "sr_hall_orders_tbl";
	 *
	 * int rows = updateRecord(jdbcTemplate, primaryKeyMap, updateMap, tableName);
	 * }</pre>
	 *
	 * @param jdbcTemplate the {@link JdbcTemplate} used to execute the update
	 * @param primaryKeys  a map containing the primary key column(s) and their
	 *                     values (for WHERE clause)
	 * @param updateFields a map containing the column(s) and new value(s) to set
	 *                     (for SET clause)
	 * @param tableName    the name of the database table to update
	 * @return the number of rows affected by the update
	 * @throws IllegalArgumentException if any of the input parameters are null,
	 *                                  empty, or invalid
	 */
	public static int updateRecord(JdbcTemplate jdbcTemplate, Map<String, Object> primaryKeys,
			Map<String, Object> updateFields, String tableName) throws IllegalArgumentException {
		if (primaryKeys == null || primaryKeys.isEmpty() || updateFields == null || updateFields.isEmpty()
				|| tableName == null || tableName.isBlank()) {
			throw new IllegalArgumentException("Invalid input for update query");
		}

		// Build SET clause
		String setClause = updateFields.keySet().stream().map(key -> key + " = ?").collect(Collectors.joining(", "));

		// Build WHERE clause
		String whereClause = primaryKeys.keySet().stream().map(key -> key + " = ?")
				.collect(Collectors.joining(" AND "));

		// Combine query
		String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;

		// Combine values for both SET and WHERE in order
		List<Object> params = Stream.concat(updateFields.values().stream(), primaryKeys.values().stream())
				.collect(Collectors.toList());

		// Execute update
		return jdbcTemplate.update(sql, params.toArray());
	}
	
	
	/**
	 * Inserts a new row into the specified database table using the provided {@link JdbcTemplate}.
	 * <p>
	 * This method dynamically fetches the table's column names using {@code fetchTableColumns()},
	 * constructs an SQL INSERT statement, and executes it with the given values.
	 * </p>
	 *
	 * @param jdbcTemplate the {@link JdbcTemplate} instance to execute the query.
	 * @param tableName    the name of the table where the row should be inserted.
	 * @param values       the list of values to be inserted, in the same order as the table's columns.
	 *                     The number of values must match the number of columns returned by {@code fetchTableColumns()}.
	 * @return the number of rows affected (should be 1 if the insert is successful).
	 *
	 * @throws IllegalArgumentException if the number of values does not match the number of table columns.
	 * @throws org.springframework.dao.DataAccessException if there is any issue executing the SQL statement.
	 *
	 * <b>Example:</b>
	 * <pre>
	 * List&lt;Object&gt; values = List.of(1, "John Doe", "IT", 50000);
	 * int result = insertRow(jdbcTemplate, "employees", values);
	 * System.out.println("Rows inserted: " + result);
	 * </pre>
	 */
	public static int insertRow(JdbcTemplate jdbcTemplate, String tableName, Map<String,Object> values) {
		List<String> columns = fetchTableColumns(tableName, jdbcTemplate);
		if(columns.contains("id")) {
			columns.remove("id");
		}
		if (columns.size() != values.size()) {
			throw new IllegalArgumentException("Number of columns and values must match");
		}

		List<Object> columnValues=columns.stream().map((columnName)->values.get(columnName.trim())).toList();

		// Create comma-separated column names
		String columnNames = String.join(", ", columns);

		// Create placeholders (?, ?, ?)
		String placeholders = columns.stream().map(col -> "?").collect(Collectors.joining(", "));

		// Build SQL
		String sql = "REPLACE INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";

		// Execute
		return jdbcTemplate.update(sql, columnValues.toArray());
	}

}
