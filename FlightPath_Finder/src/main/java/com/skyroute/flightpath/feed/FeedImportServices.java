package com.skyroute.flightpath.feed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.skyroute.flightpath.helper.UtilHelper;
import com.skyroute.flightpath.model.FlightPathConstants;

@Component
public abstract class FeedImportServices {

	public static FeedImportServices build() {
		Properties props = new Properties();

		try {
			InputStream is;

			// 1. Try external file (useful if you want to override after packaging)
			File externalFile = new File(FlightPathConstants.PROPERTIES_PATH);
			if (externalFile.exists()) {
				is = new FileInputStream(externalFile);
			} else {
				// 2. Fallback: load from inside JAR (resources folder)
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");

				if (is == null) {
					throw new FileNotFoundException("application.properties not found in classpath!");
				}
			}

			props.load(is);

			String propertie = props.getProperty(FlightPathConstants.CLASS_MAPPING);
			Map<String, String> propertieMap = UtilHelper.parseKeyValuePairs(propertie, FlightPathConstants.COMMA);

			String className = propertieMap.get(props.getProperty(FlightPathConstants.FILE_TYPE));
			if (className == null) {
				throw new IllegalArgumentException(
						"Property '" + FlightPathConstants.FILE_TYPE + "' not found in file.");
			}

			Class<?> clazz = Class.forName(className);
			return (FeedImportServices) clazz.getDeclaredConstructor().newInstance();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public abstract Map<String, Object> loadFileDataToTable(String filePath, String tableName);
}
