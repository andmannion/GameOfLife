package ie.ucd.engac.lifegamelogic.gameboardlogic;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.google.gson.*;

import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.WheelSpin;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.*;

public class LogicGameBoard {
	private UDAGraph<String> boardGraph;
	private HashMap<String, GameBoardTile> idToGameBoardTileMap;
	private WheelSpin spinningWheel;
	private JsonObject overallObject;

	public LogicGameBoard(String jsonBoardLayout) {
		boardGraph = new UDAGraph<String>();
		idToGameBoardTileMap = new HashMap<String, GameBoardTile>();
		spinningWheel = new WheelSpin();
		
		initialiseBoard(jsonBoardLayout);
	}

	public int spinTheWheel() {
		return spinningWheel.spinTheWheel();
	}
	
	private void initialiseParser(String jsonBoardLayout) {
		JsonParser parser = new JsonParser();
		overallObject = null;

		try {
			overallObject = (JsonObject) parser.parse(jsonBoardLayout);
		} catch (Exception e) {
			System.out.println("Exception in LogicGameBoard....initialiseParser(): \n" + e.toString());
			System.exit(-1);
		}
	}

	private void initialiseBoard(String jsonBoardLayout) {
		byte[] encodedBoardLayoutConfigContent = new byte[0];

		try {
			encodedBoardLayoutConfigContent = Files.readAllBytes(Paths.get(jsonBoardLayout));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		Charset charset = Charset.defaultCharset();
		String boardLayoutConfigString = new String(encodedBoardLayoutConfigContent, charset);
		
		initialiseParser(jsonBoardLayout);
		
		initialiseConnectivity(boardLayoutConfigString);	
		
		initialiseTiles(boardLayoutConfigString);
	}

	private void initialiseTiles(String jsonBoardLayout) {
		JsonArray verticesAsJsonArray = overallObject.getAsJsonArray("vertices");
		
		RuntimeTypeAdapterFactory<GameBoardTile> tileAdapterFactory = RuntimeTypeAdapterFactory.of(GameBoardTile.class,	"tileMovementType");

		tileAdapterFactory.registerSubtype(GameBoardContinueTile.class);
		tileAdapterFactory.registerSubtype(GameBoardStopTile.class);

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(tileAdapterFactory).create();

		for (JsonElement vertexAsJsonObj : verticesAsJsonArray) {
			String id = ((JsonObject) vertexAsJsonObj).get("id").getAsString();
			JsonElement tileAsJsonObject = ((JsonObject) vertexAsJsonObj).get("gameBoardTile");

			GameBoardTile gameBoardTile = gson.fromJson(tileAsJsonObject, GameBoardTile.class);

			idToGameBoardTileMap.put(id, gameBoardTile);
		}
	}
	
	private void initialiseConnectivity(String jsonBoardLayout) {
		JsonArray edgesAsJsonArray = overallObject.getAsJsonArray("edges");

		for (JsonElement edgeAsJsonObj : edgesAsJsonArray) {
			String sourceVertex = ((JsonObject) edgeAsJsonObj).get("source").getAsString();
			String targetVertex = ((JsonObject) edgeAsJsonObj).get("target").getAsString();

			boardGraph.add(sourceVertex, targetVertex);
		}
	}
}
