package ie.ucd.engac.customdatastructures;

import java.util.ArrayList;
import java.util.HashMap;

public class UDAGraph<V>{
	// logicgameboard is a graph of BoardTiles, canonical form
		public static class Edge<V>{
			private V vertex;
			
			public Edge(V v) {
				vertex = v;
			}
			
			public V getVertex() {
				return vertex;
			}
		}
		
		// Map each vertex to a list of adjacent vertices
		private HashMap<V, ArrayList<Edge<V>>> neighbours;

		public UDAGraph() {
			neighbours = new HashMap<V, ArrayList<Edge<V>>>();
		}
		
		public void add (V vertex) {
			if(neighbours.containsKey(vertex)) {
				return;
			}
			
			neighbours.put(vertex, new ArrayList<Edge<V>>());
		}
		
		public int getNumberOfEdges() {
			int sum = 0;
			
			for(ArrayList<Edge<V>> outBounds : neighbours.values()) {
				sum += outBounds.size();
			}
			
			return sum;
		}
		
		public void add(V from, V to) {
			this.add(from);
			this.add(to);
			
			neighbours.get(from).add(new Edge<V>(to));
		}
		
		public ArrayList<V> outboundNeighbours(V vertex){
			ArrayList<V> neighbourList = new ArrayList<V>();
			
			for(Edge<V> edge : neighbours.get(vertex)){
				neighbourList.add(edge.vertex);
			}
			
			return neighbourList;
		}
}
