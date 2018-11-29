package ie.ucd.engac.lifegamelogic.gameboard;

import com.google.gson.*;
import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardContinueTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.messaging.Tile;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {
	private UDAGraph<String> boardGraph;
	private HashMap<String, GameBoardTile> idToGameBoardTileMap;
	private Spinner spinningWheel;
	private String jsonBoardConfigFileLocation;
	private InputStream boardInputStream;
	private JsonElement overallJSONElement;

	public GameBoard(String jsonBoardConfigFileLocation) {
		this.jsonBoardConfigFileLocation = jsonBoardConfigFileLocation;
		boardGraph = new UDAGraph<>();
		idToGameBoardTileMap = new HashMap<>();
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
		if (boardLocation != null) {
			String id = boardLocation.getLocation();

			if (idToGameBoardTileMap.containsKey(id)) {
				return idToGameBoardTileMap.get(id);
			}
			return  null;
		}
		return null;
	}

	private void initialiseParser() {

        JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(boardInputStream));
        overallJSONElement = null;

        try {
            overallJSONElement = (JsonElement) streamParser.next();
        } catch (Exception e) {
            System.err.println("Exception in GameBoard...initialiseParser(): \n" + e.toString());
            System.exit(-1);
        }

	}

	private void initialiseBoard() {
        boardInputStream = GameBoard.class.getClassLoader().getResourceAsStream(jsonBoardConfigFileLocation);//FileUtilities.GetEntireContentsAsString(jsonBoardConfigFileLocation);
		
		initialiseParser();
		
		initialiseConnectivity();	
		
		initialiseTiles();

	}

    /**
     * Get the board's layout
     * @return the layout of the board
     */
	public ArrayList<Tile> getLayout(){
	    ArrayList<Tile> tiles = null;
	    try {
            tiles = convertGameBoardTilesToMessageTiles();
        }
        catch (Exception e){
	        System.err.println("Could not get board locations from config file. " + e.toString());
        }
        return tiles;
    }

    private ArrayList<Tile> convertGameBoardTilesToMessageTiles(){
	    ArrayList<Tile> tiles = new ArrayList<>();
        for (GameBoardTile gameBoardTile:idToGameBoardTileMap.values()){
            tiles.add(new Tile(gameBoardTile));
        }
        return tiles;
    }

	private void initialiseTiles() {
		JsonArray verticesAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray("vertices");

        Gson gson = getGson();

        for (JsonElement vertexAsJsonObj : verticesAsJsonArray) {
			String id = ((JsonObject) vertexAsJsonObj).get("id").getAsString();
			JsonElement tileAsJsonObject = ((JsonObject) vertexAsJsonObj).get("gameBoardTile");
			GameBoardTile gameBoardTile = gson.fromJson(tileAsJsonObject, GameBoardTile.class);

			idToGameBoardTileMap.put(id,gameBoardTile);
		}
	}

    @NotNull
    private Gson getGson() {
        RuntimeTypeAdapterFactory<GameBoardTile> tileAdapterFactory =
                RuntimeTypeAdapterFactory.of(GameBoardTile.class, "tileMovementType");

        tileAdapterFactory.registerSubtype(GameBoardContinueTile.class);
        tileAdapterFactory.registerSubtype(GameBoardStopTile.class);

        return new GsonBuilder().registerTypeAdapterFactory(tileAdapterFactory).create();
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
