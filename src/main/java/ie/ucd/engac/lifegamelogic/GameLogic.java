package ie.ucd.engac.lifegamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.bcel.internal.Const;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerColour;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.MessageReciever;

public class GameLogic implements MessageReciever<LifeGameMessage> {
	private final String GAME_BOARD_CONFIG_FILE_LOCATION = "src/main/resources/LogicGameBoard/GameBoardConfig.json";

	private Bank bank;
	private ArrayList<Player> players;
	private LogicGameBoard gameBoard;
	private int currentPlayerIndex;
	private TurnState currentTurnState;

	public GameLogic(LogicGameBoard gameBoard, int numPlayers) {
		this.gameBoard = gameBoard;
		bank = new Bank();
		currentPlayerIndex = 0;
		currentTurnState = TurnState.AwaitSpin;

		initialisePlayers(numPlayers);
	}

	@Override
	public LifeGameMessage receiveMessage(LifeGameMessage receivedMessage) {
		// Deals with transitions of overall game state only
		switch (currentTurnState) {
		case AwaitSpin:
			handleAwaitSpin(receivedMessage);			
			handleMoving();
			handleAwaitUserInput();
			break;
		case AwaitUserInput:
			handleAwaitUserInput(receivedMessage);
			break;
		default:
			break;
		}

		return null;
	}

	private void handleAwaitSpin(LifeGameMessage receivedMessage) {
		if (receivedMessage.getMessageType() == LifeGameMessageTypes.SpinTheWheel) {
			int tilesToMove = gameBoard.spinTheWheel();
			TurnAction movementActionRequired = processMovement(tilesToMove);

			if (movementActionRequired == TurnAction.TileAction) {
				Player currentPlayer = players.get(currentPlayerIndex);
				TurnAction tileActionRequired = processTileAction(currentPlayer,
						gameBoard.getGameBoardTileFromID(currentPlayer.getCurrentLocation()));
			}
		}

	}

	private void handleMoving() {
		
	}
	
	private void handleAwaitUserInput(LifeGameMessage receivedMessage) {
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
	}

	private void initialisePlayers(int numPlayers) {
		players = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
			players.add(new Player(playerIndex));
		}
	}

	private void initialiseGameBoard() {
		LogicGameBoard logicGameBoard = new LogicGameBoard(GAME_BOARD_CONFIG_FILE_LOCATION);
	}

	private int nextPlayerIndex() {
		return (++currentPlayerIndex % players.size());
	}

	/*
	 * Method which moves a player correctly to their destination tile. Picks up
	 * from previous turn if a fork was required. Invokes methods if move-over tiles
	 * require actions to be performed? No. Indicates if the player needs to make a
	 * decision on a fork at the end of the turn.
	 */
	private TurnAction processMovement(int tilesToMove) {
		currentTurnState = TurnState.Moving;
		Player currentPlayer = players.get(currentPlayerIndex);

		int movesRemaining = tilesToMove;
		currentPlayer.setMovesRemaining(tilesToMove);

		// Need to check if the previous turn required the player to choose a direction
		BoardLocation pendingBoardForkChoice = currentPlayer.getPendingBoardForkChoice();

		if (pendingBoardForkChoice != null) {
			currentPlayer.setCurrentLocation(pendingBoardForkChoice);
			movesRemaining--;
		}

		boolean stopConditionEncountered = false;

		// Try move, then decrement moves left.
		do {
			BoardLocation currentPlayerLocation = currentPlayer.getCurrentLocation();
			ArrayList<BoardLocation> neighbouringLocations = gameBoard.getOutboundNeighbours(currentPlayerLocation);

			// No check if fork
			currentPlayer.setCurrentLocation(neighbouringLocations.get(0));
			GameBoardTile currentTile = gameBoard.getGameBoardTileFromID(currentPlayer.getCurrentLocation());

			// Must stop moving if passing over a tile of certain type
			if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday && movesRemaining > 1) {
				// Store the current moves remaining for this player
				currentPlayer.setMovesRemaining(movesRemaining);

				// Indicate we need to act immediately
				return TurnAction.TileAction;

			} else if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop) {
				movesRemaining = 0;

				// Lookahead for fork in board, requiring a choice
				// Perform action based on the stop tile
				currentPlayer.setMovesRemaining(0);

			}

			movesRemaining--;
		} while (movesRemaining > 0 && !stopConditionEncountered);
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
		bank.extractMoney(currentSalary);
		currentPlayer.addToBalance(currentSalary);

		return TurnAction.PlayerUpdateAction;
	}
}
