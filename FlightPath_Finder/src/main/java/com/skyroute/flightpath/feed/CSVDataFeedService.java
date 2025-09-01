package com.skyroute.flightpath.feed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.database.ORMHelper;
import com.skyroute.flightpath.model.FlightPathConstants;

@Component
class CSVDataFeedService extends FeedImportServices {

	@Autowired
	private DBManager dbManager;

	@Override
	public Map<String, Object> loadFileDataToTable(String filePath, String tableName) {
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			List<String> allLines = lines.collect(Collectors.toList());

			if (allLines.isEmpty()) {
				System.out.println("CSV file is empty");
				return Map.of(FlightPathConstants.RESULT, FlightPathConstants.ERROR);
			}

			String[] headers = allLines.get(0).split(",");

			List<Map<String, Object>> rows = allLines.stream().skip(1) // skip header
					.map(line -> line.split(",")).map(values -> {
						Map<String, Object> rowMap = new LinkedHashMap<>();
						for (int i = 0; i < headers.length && i < values.length; i++) {
							rowMap.put(headers[i].trim().replace("\uFEFF", ""), values[i].trim().replace("\uFEFF", ""));
						}
						return rowMap;
					}).collect(Collectors.toList());

			JdbcTemplate jdbcTemplate = dbManager.getConnection();
			for(Map<String,Object> oneRow:rows){
				try {
					ORMHelper.insertRow(jdbcTemplate, tableName, oneRow);
					System.out.println("Entry inserted in db");
				} catch (Exception e) {
					try (BufferedWriter writer = new BufferedWriter(
							new FileWriter(FlightPathConstants.LOG_PATH, true))) {
						String result = oneRow.values().stream().map(Object::toString).collect(Collectors.joining(","));
						writer.newLine(); // Adds a new line before appending
						writer.write(result+","+e.getCause().getMessage());
						System.out.println("Sentence appended to file successfully.");
					} catch (IOException e1) {
						e.printStackTrace();
					}
				}
			}
			return Map.of(FlightPathConstants.RESULT, FlightPathConstants.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
			return Map.of(FlightPathConstants.RESULT, FlightPathConstants.ERROR);
		}
	}

}
