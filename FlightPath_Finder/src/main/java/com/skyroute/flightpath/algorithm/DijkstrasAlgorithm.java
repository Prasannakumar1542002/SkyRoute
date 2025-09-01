package com.skyroute.flightpath.algorithm;

import java.util.*;

import com.skyroute.flightpath.model.*;

public final class DijkstrasAlgorithm implements PathFinder {


	private static class NodeWrapper {
		AirportNode node;
		int distance;

		NodeWrapper(AirportNode node, int distance) {
			this.node = node;
			this.distance = distance;
		}
	}

	@Override
	public Result findShortestPath(List<AirportNode> nodes, AirportNode source, AirportNode destination) {
	    Map<AirportNode, Integer> distanceMap = new HashMap<>();
	    Map<AirportNode, List<AirportNode>> previousMap = new HashMap<>();

	    for (AirportNode node : nodes) {
	        distanceMap.put(node, Integer.MAX_VALUE);
	        previousMap.put(node, new ArrayList<>());
	    }
	    distanceMap.put(source, 0);

	    PriorityQueue<NodeWrapper> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
	    pq.offer(new NodeWrapper(source, 0));

	    while (!pq.isEmpty()) {
	        NodeWrapper current = pq.poll();
	        AirportNode currentNode = current.node;

	        for (FlightEdge edge : currentNode.getEdges()) {
	            if (!edge.isAvailable()) continue;

	            AirportNode neighbor = edge.getNode();
	            int newDist = distanceMap.get(currentNode) + edge.getDistance();

	            if (newDist < distanceMap.get(neighbor)) {
	                distanceMap.put(neighbor, newDist);
	                previousMap.get(neighbor).clear();
	                previousMap.get(neighbor).add(currentNode);
	                pq.offer(new NodeWrapper(neighbor, newDist));
	            } else if (newDist == distanceMap.get(neighbor)) {
	                previousMap.get(neighbor).add(currentNode);
	            }
	        }
	    }

	    if (distanceMap.get(destination) == Integer.MAX_VALUE) {
	        return new Result(-1, Collections.emptyList()); // no path
	    }

	    // build all shortest paths
	    List<List<AirportNode>> allPaths = new ArrayList<>();
	    buildAllPaths(destination, source, previousMap, new ArrayList<>(), allPaths);

	    return new Result(distanceMap.get(destination), allPaths);
	}

	private void buildAllPaths(AirportNode current, AirportNode source,
	                           Map<AirportNode, List<AirportNode>> previousMap,
	                           List<AirportNode> currentPath, List<List<AirportNode>> allPaths) {
	    currentPath.add(current);
	    if (current.equals(source)) {
	        List<AirportNode> path = new ArrayList<>(currentPath);
	        Collections.reverse(path);
	        allPaths.add(path);
	    } else {
	        for (AirportNode prev : previousMap.get(current)) {
	            buildAllPaths(prev, source, previousMap, currentPath, allPaths);
	        }
	    }
	    currentPath.remove(currentPath.size() - 1);
	}

	
//	public static void main(String[] args) {
//        // Create nodes first
//        AirportNode chennai = new AirportNode("Chennai", new ArrayList<>(), "MAA");
//        AirportNode mumbai = new AirportNode("Mumbai", new ArrayList<>(), "BOM");
//        AirportNode delhi = new AirportNode("Delhi", new ArrayList<>(), "DEL");
//        AirportNode kolkata = new AirportNode("Kolkata", new ArrayList<>(), "CCU");
//
//        // Connect flights
//        chennai.getEdges().add(new FlightEdge(true, mumbai, 300));
//        chennai.getEdges().add(new FlightEdge(true, kolkata, 450));
//        mumbai.getEdges().add(new FlightEdge(true, delhi, 200));
//        kolkata.getEdges().add(new FlightEdge(true, delhi, 100));
//
//        List<AirportNode> allNodes = Arrays.asList(chennai, mumbai, delhi, kolkata);
//
//        Result result = PathFinder.loadAlgorithm().findShortestPath(allNodes, chennai, delhi);
//
//        if (result.distance != -1) {
//            System.out.println("Shortest distance from Chennai to Delhi: " + result.distance);
//            System.out.print("Path: ");
//            for (AirportNode node : result.path) {
//                System.out.print(node.getName() + " -> ");
//            }
//            System.out.println("END");
//        } else {
//            System.out.println("No available path found.");
//        }
//    }

}
