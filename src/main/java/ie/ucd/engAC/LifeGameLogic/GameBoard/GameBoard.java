package ie.ucd.engAC.LifeGameLogic.GameBoard;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import ie.ucd.engAC.LifeGameLogic.GameBoard.GameBoardTiles.*;

public class GameBoard {
	private UDAGraph<String> boardGraph;
	private HashMap<String, GameBoardTile> idToGameBoardTileMap;
	
	public GameBoard(String jsonBoardLayout) {
		boardGraph = new UDAGraph<String>();
		idToGameBoardTileMap = new HashMap<String, GameBoardTile>();
		
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
			
			//System.out.println("source: " + sourceVertex);
			//System.out.println("target: " + targetVertex);
			
			boardGraph.add(sourceVertex, targetVertex);			
		}
		
		//System.out.println("Number of edges in graph: " + boardGraph.getNumberOfEdges());
		
		//for(String targetEdge : boardGraph.outboundNeighbours("a")) {
		//	System.out.println("a has an outgoing edge to " + targetEdge);
		//}
		
		JsonArray verticesAsJsonArray = obj.getAsJsonArray("vertices");
		
		// Identify the id of the vertex. Then, initialise
		// a GameBoardTile using the second nested JsonObject in each "vertex" JsonObject		
		Gson gson = new Gson();
		
		for(JsonElement vertexAsJsonObj : verticesAsJsonArray) {
			String id = ((JsonObject) vertexAsJsonObj).get("id").getAsString();
			JsonElement tileAsJsonObject = ((JsonObject) vertexAsJsonObj).get("gameBoardTile");
			
			GameBoardTile gameBoardTile = gson.fromJson(tileAsJsonObject, GameBoardTile.class);
			
			idToGameBoardTileMap.put(id, gameBoardTile);
		}
		
		//for(Map.Entry<String, GameBoardTile> entry : idToGameBoardTileMap.entrySet()) {
		//	System.out.println("Vertex with ID " + entry.getKey() + " has gameBoardTile with subtype " + entry.getValue().getGameBoardTileType());
		//}
	}
}
