package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;


// This holds all the elements; players, bank, etc.
public class GameLogic {

	private Bank bank;
	private ArrayList<Player> players;
	private LogicGameBoard gameBoard;
	private int currentPlayerIndex;
	private int numberOfUnconfiguredPlayers;
	private LifeGameMessage currentLifeGameMessageResponse;
	
	// Queue of expected responses
	private Queue<LifeGameMessage> expectedResponses;
	private Queue<LifeGameMessage> replyMessagesSent;
	private ArrayList<Card> pendingCardChoices;
	
	private GameState currentState;
	
	public GameLogic(LogicGameBoard gameBoard, int numPlayers) {		
		this.gameBoard = gameBoard;
		bank = new Bank();
		expectedResponses = new LinkedList<>();
		replyMessagesSent = new LinkedList<>();
		initialisePlayers(numPlayers);
	}
	
	public LifeGameMessage handleInput(LifeGameMessage lifeGameMessage) {
		// Startup
		if(currentState == null) {
			currentState = new InitialisePlayerState();
			currentState.enter(this);
		}		
		
		GameState nextGameState = currentState.handleInput(this, lifeGameMessage);
		
		if(nextGameState != null) {
			currentState = nextGameState;
			currentState.enter(this);
		}
		
		// Need to send a response message to the user
		// When there are no pending transitions, must send the current
		// LifeGameMessage as stored in the GameLogic object.
		return getLifeGameMessageResponse();
	}	
	
	protected Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	protected LogicGameBoard getGameBoard() {
		return gameBoard;
	}

	protected void setNextPlayerToCurrent() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}
	
	protected void decrementNumberOfUnconfiguredPlayers() {
		if(numberOfUnconfiguredPlayers > 0) {
			numberOfUnconfiguredPlayers--;
		}
	}

	protected void setResponseMessage(LifeGameMessage lifeGameMessage) {
		currentLifeGameMessageResponse = lifeGameMessage;
		
		// Store the reply sent so that we can be aware of the context the resulting reply should be 
		// interpreted in
		replyMessagesSent.add(lifeGameMessage);
	}

	protected void addExpectedResponse(LifeGameMessage lifeGameMessage) {
		expectedResponses.add(lifeGameMessage);
	}
	
	protected LifeGameMessage getExpectedResponse() {
		return expectedResponses.remove();
	}
	
	protected OccupationCard getTopStandardCareerCard() {
		return bank.getTopStandardCareerCard();
	}

	protected void storePendingChoiceCards(ArrayList<Card> pendingCardChoices) {
		this.pendingCardChoices = pendingCardChoices;
	}
	
	protected ArrayList<Card> getPendingCardChoices(){
		return pendingCardChoices;
	}
	
	protected void returnCareerCard(OccupationCard careerCardToBeReturned) {
		bank.returnStandardCareerCard(careerCardToBeReturned);
	}

	protected ArrayList<BoardLocation> getAdjacentForwardLocations(BoardLocation currentBoardLocation) {
		return gameBoard.getOutboundNeighbours(currentBoardLocation);
	}

	protected void extractMoneyFromBank(int amountToExtract) {
		bank.extractMoney(amountToExtract);
	}

	private LifeGameMessage getLifeGameMessageResponse() {
		return currentLifeGameMessageResponse;
	}

	private void initialisePlayers(int numPlayers) {
		players = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
			players.add(new Player(playerIndex));
		}
		
		// All of these players require the user to set some initial characteristics
		numberOfUnconfiguredPlayers = numPlayers;
	}
}
