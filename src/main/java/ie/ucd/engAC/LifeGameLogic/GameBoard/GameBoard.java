package ie.ucd.engAC.LifeGameLogic.GameBoard;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

import ie.ucd.engAC.CustomDataStructures.UDAGraph;
import ie.ucd.engAC.LifeGameLogic.GameBoard.GameBoardTiles.*;
import ie.ucd.engAC.gsonExtender.RuntimeTypeAdapterFactory;

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
			
			boardGraph.add(sourceVertex, targetVertex);			
		}
		
		JsonArray verticesAsJsonArray = obj.getAsJsonArray("vertices");
		
		RuntimeTypeAdapterFactory<GameBoardTile> tileAdapterFactory = RuntimeTypeAdapterFactory.of(GameBoardTile.class, "tileMovementType");																						;
		
		tileAdapterFactory.registerSubtype(GameBoardContinueTile.class);
		tileAdapterFactory.registerSubtype(GameBoardStopTile.class);
		
		Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(tileAdapterFactory)
				.create();
		
		for(JsonElement vertexAsJsonObj : verticesAsJsonArray) {
			String id = ((JsonObject) vertexAsJsonObj).get("id").getAsString();
			JsonElement tileAsJsonObject = ((JsonObject) vertexAsJsonObj).get("gameBoardTile");
			
			GameBoardTile gameBoardTile = gson.fromJson(tileAsJsonObject, GameBoardTile.class);
			
			idToGameBoardTileMap.put(id, gameBoardTile);
		}
	}
}
