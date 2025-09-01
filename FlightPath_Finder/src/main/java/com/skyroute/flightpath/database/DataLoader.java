package com.skyroute.flightpath.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;

public class DataLoader {

    public static Set<String> loadDataToSet(String resourcePath) throws IOException {
    	Path filePath = new ClassPathResource(resourcePath).getFile().toPath();
    	try (Stream<String> stream = Files.lines(filePath)) {
            return stream
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());
        }
    }

//    public static void main(String[] args) {
//        try {
//            Set<String> countries = loadDataToSet("path/to/countries.txt");
//            Set<String> states = loadDataToSet("path/to/states.txt");
//            Set<String> cities = loadDataToSet("path/to/cities.txt");
//
//            // Just to verify
//            System.out.println("Loaded countries: " + countries.size());
//            System.out.println("Loaded states: " + states.size());
//            System.out.println("Loaded cities: " + cities.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

