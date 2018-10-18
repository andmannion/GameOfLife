package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.ui.GameBoard;

public class WaitForSpinState implements GameState {
	private final int PAYDAY_LANDED_ON_BONUS = 100000;	
	
	@Override
	public void enter(GameLogic gameLogic) {

	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			// TODO: when should the fork choice after a stop tile has been
			// encountered be handled - at the beginning or the end of a turn?
			// I would say the beginning...

			LogicGameBoard gameBoard = gameLogic.getGameBoard();

			// Need to spin the spinner
			int tilesToMove = Spinner.spinTheWheel();
			int tilesMoved = 0;
			boolean stopTileEncountered = false;
			GameBoardTile currentTile = null;

			// Need to alternate between moving and evaluating the tile we're on
			while (tilesMoved < tilesToMove && !stopTileEncountered) {
				// Go forward
				BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
				ArrayList<BoardLocation> adjacentForwardLocations = gameBoard
						.getOutboundNeighbours(currentBoardLocation);

				// For the moment, no tiles other than stop tiles have branches
				if (1 == adjacentForwardLocations.size()) {
					BoardLocation currentLocation = adjacentForwardLocations.get(0);
					gameLogic.getCurrentPlayer().setCurrentLocation(currentLocation);

					// Need to get the tile that this boardLocation relates to
					currentTile = gameBoard.getGameBoardTileFromID(currentLocation);

					stopTileEncountered = (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop);

					if (!stopTileEncountered) {
						// Perform actions if the tile requires action when passed over
						performUpdateIfPassingOverTile(currentTile, gameLogic);
					}
				}
				else if(0 == adjacentForwardLocations.size()) {
					// Must initiate retirement procedure
				}
				
				tilesMoved++;
			}

			// At this point, we have landed on a tile, either through the number of goes
			// running out,
			// or by encountering a stop tile.
			switch (currentTile.getGameBoardTileType()) {
			case Payday:
				OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
				int currentSalary = currentOccupationCard.getSalary();
				gameLogic.extractMoneyFromBank(currentSalary + PAYDAY_LANDED_ON_BONUS);
				gameLogic.getCurrentPlayer().addToBalance(currentSalary + PAYDAY_LANDED_ON_BONUS);
				break;
			case Action:
				
				break;
//				Holiday,
//				SpinToWin,
//				Baby,
//				House,
//				Stop,
//				Retire
			default:
				// There's no console to print to...
				// TODO: use some utility log file class to write the errors of the program to				
				break;
			}
		}

		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub

	}

	private void performUpdateIfPassingOverTile(GameBoardTile currentTile, GameLogic gameLogic) {
		if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday) {
			// Player should collect the salary indicated in their Career/College Career
			// card from the Bank
			OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
			int currentSalary = currentOccupationCard.getSalary();

			// Get money from the bank, increment the player's balance by that amount
			gameLogic.extractMoneyFromBank(currentSalary);
			gameLogic.getCurrentPlayer().addToBalance(currentSalary);
		}
	}
}
