package ie.ucd.engac.lifegamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.bcel.internal.Const;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
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
		if (currentTurnState == TurnState.AwaitSpin) {
			if (receivedMessage.getMessageType() == LifeGameMessageTypes.SpinTheWheel) {
				int tilesToMove = gameBoard.spinTheWheel();

				currentTurnState = TurnState.Moving;

				processMovement(tilesToMove);

				switch (currentTurnState) {
				case SpecialTile:
					break;
				case AwaitUserInput:
					// Construct a message detailing the information required, and send it
					return new LifeGameMessage();
				default:
					System.out.println("Should not enter this state in GameLogic.receiveMessage()");
				}

				processTileAction();
			}
		}

		return null;
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
	private void processMovement(int tilesToMove) {
		Player currentPlayer = players.get(currentPlayerIndex);
		int movesRemaining = tilesToMove;
		currentPlayer.setMovesRemaining(tilesToMove);

		boolean stopConditionEncountered = false;

		// Need to check if the previous turn required the player to choose a direction
		BoardLocation pendingBoardForkChoice = currentPlayer.getPendingBoardForkChoice();

		if (pendingBoardForkChoice != null) {
			currentPlayer.setCurrentLocation(pendingBoardForkChoice);
			movesRemaining--;
		}

		do {
			BoardLocation currentPlayerLocation = currentPlayer.getCurrentLocation();

			ArrayList<BoardLocation> neighbouringLocations = gameBoard.getOutboundNeighbours(currentPlayerLocation);

			currentPlayer.setCurrentLocation(neighbouringLocations.get(0));
			GameBoardTile currentTile = gameBoard.getGameBoardTileFromID(currentPlayer.getCurrentLocation());

			if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday) {
				currentTurnState = TurnState.SpecialTile;
				// Store the current moves remaining for this player
				currentPlayer.setMovesRemaining(movesRemaining);
			} else if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop) {
				movesRemaining = 0;

				// Lookahead for fork in board, requiring a choice
				// Perform action based on the stop tile
				currentPlayer.setMovesRemaining(0);

			}

			movesRemaining--;
		} while (movesRemaining > 0 && !stopConditionEncountered);
	}

	private void processTileAction() {

	}
}
