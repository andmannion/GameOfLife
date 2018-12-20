package ie.ucd.engac.lifegamelogic.gameboard;

import ie.ucd.engac.customdatastructures.UDAGraph;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;

import java.util.HashMap;

public interface BoardConfigHandler {
    void initialiseBoard();

    UDAGraph<String> getBoardGraph();

    HashMap<String, GameBoardTile> getIDGameBoardTileMap();
}
