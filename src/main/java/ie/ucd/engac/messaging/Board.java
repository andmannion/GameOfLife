package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class Board {
    private ArrayList<Tile> tiles;

    public Board(ArrayList<Tile> tiles){
        this.tiles = tiles;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
