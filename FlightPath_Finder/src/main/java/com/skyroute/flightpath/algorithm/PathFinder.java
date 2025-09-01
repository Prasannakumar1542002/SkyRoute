package com.skyroute.flightpath.algorithm;

import java.util.*;
import java.io.*;

import com.skyroute.flightpath.helper.UtilHelper;
import com.skyroute.flightpath.model.*;

public interface PathFinder {

	public static PathFinder loadAlgorithm() {
		try (InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("application.properties")) {

			if (is == null) {
				throw new FileNotFoundException("application.properties not found in classpath!");
			}

			Properties props = new Properties();
			props.load(is);

			String propertie = props.getProperty(FlightPathConstants.CLASS_MAPPING);
			Map<String, String> propertieMap = UtilHelper.parseKeyValuePairs(propertie, FlightPathConstants.COMMA);
			String className = propertieMap.get(props.getProperty(FlightPathConstants.ALGORITHM));

			if (className == null) {
				throw new IllegalArgumentException(
						"Property '" + FlightPathConstants.ALGORITHM + "' not found in file.");
			}

			Class<?> clazz = Class.forName(className);
			return (PathFinder) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public Result findShortestPath(List<AirportNode> nodes, AirportNode source, AirportNode destination);
}
