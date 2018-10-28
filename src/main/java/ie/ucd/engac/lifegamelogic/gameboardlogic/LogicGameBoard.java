package ie.ucd.engac.lifegamelogic.gameboardlogic;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.fileutilities.FileUtilities;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.*;

public class LogicGameBoard {
	private UDAGraph<String> boardGraph;
	private HashMap<String, GameBoardTile> idToGameBoardTileMap;
	private Spinner spinningWheel;
	private String jsonBoardConfigFileLocation;
	private String jsonBoardConfigFileContent;
	private JsonElement overallJSONElement;

	public LogicGameBoard(String jsonBoardConfigFileLocation) {
		this.jsonBoardConfigFileLocation = jsonBoardConfigFileLocation;
		boardGraph = new UDAGraph<String>();
		idToGameBoardTileMap = new HashMap<String, GameBoardTile>();
		spinningWheel = new Spinner();
		
		initialiseBoard();
	}

	public int spinTheWheel() {
		return spinningWheel.spinTheWheel();
	}
	
	public ArrayList<BoardLocation> getOutboundNeighbours(BoardLocation boardLocation) {
		ArrayList<BoardLocation> outboundLocations = new ArrayList<BoardLocation>();
		
		for(String locationID : boardGraph.outboundNeighbours(boardLocation.getLocation())) {
			if(locationID != null) {
				outboundLocations.add(new BoardLocation(locationID));
			}
		}
		return outboundLocations;
	}

	// Need to be able to get a tile from an id
	public GameBoardTile getGameBoardTileFromID(BoardLocation boardLocation) {
		String id = boardLocation.getLocation();
		
		if(idToGameBoardTileMap.containsKey(id)) {
			return idToGameBoardTileMap.get(id);
		}
		
		return null;
	}

	private void initialiseParser() {
		JsonParser parser = new JsonParser();
		overallJSONElement = null;

		try {
			overallJSONElement = (JsonElement) parser.parse(jsonBoardConfigFileContent);
		} catch (Exception e) {
			System.out.println("Exception in LogicGameBoard...initialiseParser(): \n" + e.toString());
			System.exit(-1);
		}
	}

	private void initialiseBoard() {
		jsonBoardConfigFileContent = FileUtilities.GetEntireContentsAsString(jsonBoardConfigFileLocation);
		
		initialiseParser();
		
		initialiseConnectivity();	
		
		initialiseTiles();
	}

	private void initialiseTiles() {
		JsonArray verticesAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray("vertices");
		
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
	
	private void initialiseConnectivity() {
		JsonArray edgesAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray("edges");

		for (JsonElement edgeAsJsonObj : edgesAsJsonArray) {
			String sourceVertex = ((JsonObject) edgeAsJsonObj).get("source").getAsString();
			String targetVertex = ((JsonObject) edgeAsJsonObj).get("target").getAsString();

			boardGraph.add(sourceVertex, targetVertex);
		}
	}
}
