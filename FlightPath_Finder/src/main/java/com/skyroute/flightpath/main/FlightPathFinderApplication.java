package com.skyroute.flightpath.main;

//import java.util.*;
//import java.util.stream.Collectors;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//import com.skyroute.flightpath.algorithm.NodeLoader;
//import com.skyroute.flightpath.algorithm.PathFinder;
//import com.skyroute.flightpath.database.DatabaseConstants;
//import com.skyroute.flightpath.feed.FeedImportServices;
//import com.skyroute.flightpath.model.AirportNetwork;
//import com.skyroute.flightpath.model.AirportNode;
//import com.skyroute.flightpath.model.Result;

@SpringBootApplication
@ComponentScan(basePackages = {"com.skyroute.flightpath"})
public class FlightPathFinderApplication implements CommandLineRunner  {
	
//	@Autowired
//    private FeedImportServices feedImportServices;
//	
//	@Autowired
//    private NodeLoader nodeLoader;

	public static void main(String[] args) {
		SpringApplication.run(FlightPathFinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
//		String path="D:\\SkyPath Datasets\\archive\\airports_with_coords.json";
//		Map<String,Object> result=feedImportServices.loadFileDataToTable(path, DatabaseConstants.AIRPORTS_TBL);
//		System.out.println(result);
		System.out.println("Have a Good Day");
		
//		AirportNetwork airportNetwork=nodeLoader.loadNodes();
//		
//		
//		
//        Result result = PathFinder.loadAlgorithm().findShortestPath(airportNetwork.getAirportNodes(), airportNetwork.getNodeMap().get("MAA"), airportNetwork.getNodeMap().get("STV"));
//
//        if (result.distance != -1) {
//            System.out.println("Shortest distance from Chennai to Delhi: " + result.distance);
//            System.out.println("All shortest paths:");
//
//            // Use a set to remove duplicates
//            Set<String> uniquePaths = new LinkedHashSet<>();
//            for (List<AirportNode> path : result.getAllPaths()) {
//                String pathStr = path.stream()
//                                     .map(AirportNode::getName)
//                                     .collect(Collectors.joining(" -> "));
//                uniquePaths.add(pathStr);
//            }
//
//            int count = 1;
//            for (String pathStr : uniquePaths) {
//                System.out.println("Path " + (count++) + ": " + pathStr + " -> END");
//            }
//        }

		
//		airportNodes.forEach((node)->{
//			System.out.println(node.getName()+" : ");
//			node.getEdges().forEach((oneNode)->System.out.println("   "+oneNode.getNode().getName()));
//		});
		
	}


}
