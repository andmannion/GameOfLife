package ie.ucd.engac.lifegamelogic.gameboard;

import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.messaging.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {
	private UDAGraph<String> boardGraph;
	private HashMap<String, GameBoardTile> idToGameBoardTileMap;
	private BoardConfigHandler boardConfigHandler;
	private Spinner spinningWheel;

	public GameBoard(BoardConfigHandler boardConfigHandler) {
		this.boardConfigHandler = boardConfigHandler;
		spinningWheel = new Spinner();
		
		initialiseBoard();

		// Call destructor on boardConfigHandler

	}

	public int spinTheWheel() {
		return spinningWheel.spinTheWheel();
	}
	
	public ArrayList<BoardLocation> getOutboundNeighbours(BoardLocation boardLocation) {
		ArrayList<BoardLocation> outboundLocations = new ArrayList<>();
		
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
			return null;
		}
		return null;
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

	private void initialiseBoard() {
		boardConfigHandler.initialiseBoard();
		boardGraph = boardConfigHandler.getBoardGraph();
		idToGameBoardTileMap = boardConfigHandler.getIDGameBoardTileMap();
	}

	private ArrayList<Tile> convertGameBoardTilesToMessageTiles(){
	    ArrayList<Tile> tiles = new ArrayList<>();
        for (GameBoardTile gameBoardTile:idToGameBoardTileMap.values()){
            tiles.add(new Tile(gameBoardTile));
        }
        return tiles;
    }
}
