package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;

public class GameLogicInterface implements MessageReceiverAndResponder<LifeGameMessage> {
	private GameLogic gameLogic;
	
	public GameLogicInterface(LogicGameBoard gameBoard, int numPlayers) {
		gameLogic = new GameLogic(gameBoard, numPlayers);
	}

	@Override
	public LifeGameMessage receiveMessage(LifeGameMessage lifeGameMessage) {
		LifeGameMessage gameLogicResponse = gameLogic.handleInput(lifeGameMessage);
		
		return gameLogicResponse;
	}
	
	
	   /* What types of user input could I be awaiting?
		* - Initial path selection choice
		* - Holiday Space:
		* 		- Request a new spin for the next turn
		* - Action Space:
		* 		- CareerChangeDecision - Choice of one career card from two
		* 		- PlayersPayDecision - Choice of one player from all players excluding the current player
		* - Spin to Win Space:
		* 		- Current player chooses two different numbers between 1 and 10
		* 		  <Other players choose a DIFFERENT number between 1 and 10, get these n numbers, spin once>
		* 		  Tell UI if the correct number has been spun, if yes, perform action on users,
		* 		  Otherwise request another spinEvent, keep doing this until the number has been matched, then send update message.
		* - House Space:
		* 		- Must choose to buy or sell a house
		* 			- Buy a house: 
		* 				Need to get a choice of the favourite of the two top cards in the HouseCard deck.
		* 				Check that the player has enough money, if not, notify of confirmation to proceed
		* 				notifying them of the amount of loans needed to purchase that house.
		* 				Add the favourite to the player, deducting price, adding loans as necessary
		* 				Add the unchosen housecard to the bottom of the deck.
		* 			- Sell a house
		* 				Request spin event
		* 				Even number - get black price, odd number - get red price
		* 				Return house card to the deck
		* - Stop Spaces:
		* 		- 	  
		*/ 		 
	

	


	/*
	 * Method which moves a player correctly to their destination tile. Picks up
	 * from previous turn if a fork was required. Invokes methods if move-over tiles
	 * require actions to be performed? No. Indicates if the player needs to make a
	 * decision on a fork at the end of the turn.
	 */
	private void processMovement(int tilesToMove) {
//		currentTurnState = TurnState.Moving;
//		Player currentPlayer = players.get(currentPlayerIndex);
//
//		int movesRemaining = tilesToMove;
//		currentPlayer.setMovesRemaining(tilesToMove);
//
//		// Need to check if the previous turn required the player to choose a direction
//		BoardLocation pendingBoardForkChoice = currentPlayer.getPendingBoardForkChoice();
//
//		if (pendingBoardForkChoice != null) {
//			currentPlayer.setCurrentLocation(pendingBoardForkChoice);
//			movesRemaining--;
//		}
//
//		boolean stopConditionEncountered = false;
//
//		// Try move, then decrement moves left.
//		do {
//			BoardLocation currentPlayerLocation = currentPlayer.getCurrentLocation();
//			ArrayList<BoardLocation> neighbouringLocations = gameBoard.getOutboundNeighbours(currentPlayerLocation);
//
//			// No check if fork
//			currentPlayer.setCurrentLocation(neighbouringLocations.get(0));
//			GameBoardTile currentTile = gameBoard.getGameBoardTileFromID(currentPlayer.getCurrentLocation());
//
//			// Must stop moving if passing over a tile of certain type
//			if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday && movesRemaining > 1) {
//				// Store the current moves remaining for this player
//				currentPlayer.setMovesRemaining(movesRemaining);
//
//				// Indicate we need to act immediately
//				return TurnAction.TileAction;
//
//			} else if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop) {
//				movesRemaining = 0;
//
//				// Lookahead for fork in board, requiring a choice
//				// Perform action based on the stop tile
//				currentPlayer.setMovesRemaining(0);
//
//			}
//
//			movesRemaining--;
//		} while (movesRemaining > 0 && !stopConditionEncountered);
//		
//		return TurnAction.TileAction;
	}

	private TurnAction processTileAction(Player currentPlayer, GameBoardTile gameBoardTile) {
		if (currentPlayer.getMovesRemaining() > 1
				&& gameBoardTile.getGameBoardTileType() == GameBoardTileTypes.Payday) {
			return processPaydayPassoverEvent(currentPlayer, gameBoardTile);
		}

		return TurnAction.PlayerUpdateAction;
	}

	private TurnAction processPaydayPassoverEvent(Player currentPlayer, GameBoardTile gameBoardTile) {
		// Player should collect the salary indicated in their Career/College Career
		// card from the Bank
		OccupationCard currentOccupationCard = currentPlayer.getOccupationCard();

		int currentSalary = currentOccupationCard.getSalary();

		// Get money from the bank, increment the player's balance by that amount
		//bank.extractMoney(currentSalary);
		currentPlayer.addToBalance(currentSalary);

		return TurnAction.PlayerUpdateAction;
	}
}
