package ie.ucd.engac.lifegamelogic.gameboard;

import com.google.gson.*;
import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardContinueTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DefaultBoardConfigHandler implements BoardConfigHandler{
    private InputStream boardInputStream;
    private JsonElement overallJSONElement;
    private UDAGraph<String> boardGraph;
    private HashMap<String, GameBoardTile> idToGameBoardTileMap;
    private String jsonBoardConfigFileLocation;

    public DefaultBoardConfigHandler(String jsonBoardConfigFileLocation){
        boardGraph = new UDAGraph<>();
        idToGameBoardTileMap = new HashMap<>();
        this.jsonBoardConfigFileLocation = jsonBoardConfigFileLocation;
    }

    public void initialiseBoard() {
        boardInputStream = GameBoard.class.getClassLoader().getResourceAsStream(jsonBoardConfigFileLocation);

        initialiseParser();

        initialiseConnectivity();

        initialiseTiles();
    }

    public UDAGraph<String> getBoardGraph(){
        return boardGraph;
    }

    public HashMap<String, GameBoardTile> getIDGameBoardTileMap(){
        return idToGameBoardTileMap;
    }

    private void initialiseParser() {
        JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(boardInputStream));
        overallJSONElement = null;

        try {
            overallJSONElement = streamParser.next();
        } catch (Exception e) {
            System.err.println("Exception in DefaultBoardConfigHandler...initialiseParser(): \n" + e.toString());
            System.exit(-1);
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

    private void initialiseTiles() {
        JsonArray verticesAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray("vertices");

        Gson gson = getGson();

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
