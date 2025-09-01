package com.skyroute.flightpath.helper;

import java.util.*;
import java.util.stream.Collectors;

public class UtilHelper {

	public static Map<String, String> parseKeyValuePairs(String content, String delimiter) {
		return Arrays.asList(content.split(delimiter)).stream().map(str -> str.split("=", 2))
				.collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
	}

}
