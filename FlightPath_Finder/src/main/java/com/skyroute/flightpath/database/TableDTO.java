package com.skyroute.flightpath.database;

import java.util.Map;

public class TableDTO {
	final String tableName;
	private Map<String, Object> tableRows;

	protected TableDTO(String tableName, Map<String, Object> tableRows) {
		this.tableName = tableName;
		this.tableRows = tableRows;
	}

	public Map<String, Object> getData() {
		return tableRows;
	}

	public void setData(Map<String, Object> tableRows) {
		this.tableRows=tableRows;
	}
	
	public void setFields(String fieldName, Object value) {
		tableRows.put(fieldName, value);
    }

}
