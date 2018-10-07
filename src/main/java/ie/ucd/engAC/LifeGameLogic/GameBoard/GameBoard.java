package ie.ucd.engAC.LifeGameLogic.GameBoard;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;

public class GameBoard {
	private UDAGraph<String> boardGraph;
	
	public GameBoard(String jsonBoardLayout) {
		boardGraph = new UDAGraph<String>();
		
		byte[] encodedBoardLayoutConfigContent = new byte[0];
		
		try {			
			encodedBoardLayoutConfigContent = Files.readAllBytes(Paths.get(jsonBoardLayout));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		Charset charset = Charset.defaultCharset();
		
		String boardLayoutDeckConfigString = new String(encodedBoardLayoutConfigContent, charset);
		
		JsonParser parser = new JsonParser();		
		JsonObject obj = null;
		
		try {
			obj = (JsonObject) parser.parse(boardLayoutDeckConfigString);
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(-1);
		}	
		
		JsonArray edgesAsJsonArray = obj.getAsJsonArray("edges");
		
		for(JsonElement edgeAsJsonObj : edgesAsJsonArray) {
			String sourceVertex = ((JsonObject) edgeAsJsonObj).get("source").getAsString();
			String targetVertex = ((JsonObject) edgeAsJsonObj).get("target").getAsString();
			
			System.out.println("source: " + sourceVertex);
			System.out.println("target: " + targetVertex);
			
			boardGraph.add(sourceVertex, targetVertex);			
		}
		
		System.out.println("Number of edges in graph: " + boardGraph.getNumberOfEdges());
		
		for(String targetEdge : boardGraph.outboundNeighbours("a")) {
			System.out.println("a has an outgoing edge to " + targetEdge);
		}
	}
}
