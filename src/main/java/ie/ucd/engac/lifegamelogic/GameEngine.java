package ie.ucd.engac.lifegamelogic;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;

public class GameEngine {
	private LogicGameBoard logicGameBoard;
	private Bank bank;
	private ArrayList<Player> playerList;
	private GameState currentGameState;
	
	private GameEngine() {
		
	}
}
