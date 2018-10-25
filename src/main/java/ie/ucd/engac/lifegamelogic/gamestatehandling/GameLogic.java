package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.CareerPath;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;

// This holds all the elements; players, bank, etc.
public class GameLogic {
	private Bank bank;
	private ArrayList<Player> players;
	private LogicGameBoard gameBoard;
	private int currentPlayerIndex;
	private int numberOfUnconfiguredPlayers;
	private LifeGameMessage currentLifeGameMessageResponse;	
	
	private ArrayList<Card> pendingCardChoices;

	private GameState currentState;
	
	public GameLogic(LogicGameBoard gameBoard, int numPlayers) {
		this.gameBoard = gameBoard;
		bank = new Bank();
		initialisePlayers(numPlayers);
		
		currentState = new PathChoiceState();
		currentState.enter(this);
	}
	
	public LifeGameMessage handleInput(LifeGameMessage lifeGameMessage) {
		System.out.println("Trying to handle input");
		System.out.println("Current state is " + currentState.toString());
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
	
	protected Player getPlayerByIndex(int playerIndex) {
		if(playerIndex < 0 || playerIndex > players.size()) {
			return null;
		}
		
		return players.get(playerIndex);
	}

	protected int getNextPlayerIndex(int playerIndex) {
		if(playerIndex < 0 || playerIndex > players.size()) {
			return -1;
		}
		
		return (playerIndex + 1) % players.size();
	}
	
	protected void movePlayerToInitialCollegeCareerPath(int playerNumber) {
		BoardLocation collegeCareerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(1); 
		
		players.get(playerNumber).setCurrentLocation(collegeCareerPathInitialLocation);
	}
	
	protected void movePlayerToInitialCareerPath(int playerNumber) {
		BoardLocation careerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(0); 
		
		players.get(playerNumber).setCurrentLocation(careerPathInitialLocation);
	}

	protected int getNumberOfPlayers() {
		return players.size();
	}
	
	public int getNumberOfUninitialisedPlayers() {
		return numberOfUnconfiguredPlayers;
	}

	protected void decrementNumberOfUninitialisedPlayers() {
		if(numberOfUnconfiguredPlayers > 0) {
			numberOfUnconfiguredPlayers--;
		}
	}

	protected void setResponseMessage(LifeGameMessage lifeGameMessage) {
		currentLifeGameMessageResponse = lifeGameMessage;
	}

	protected OccupationCard getTopStandardCareerCard() {
		return bank.getTopStandardCareerCard();
    }
	
	protected void returnStandardCareerCard(OccupationCard careerCardToBeReturned) {
		bank.returnStandardCareerCard(careerCardToBeReturned);
	}
	
	protected OccupationCard getTopCollegeCareerCard() {
		return bank.getTopCollegeCareerCard();
	}
	
	protected void returnOccupationCard(OccupationCard occupationCardToBeReturned) {
		if(occupationCardToBeReturned.getOccupationCardType() == OccupationCardTypes.Career) {
			bank.returnStandardCareerCard(occupationCardToBeReturned);
		}
		else {
			bank.returnCollegeCareerCard(occupationCardToBeReturned);
		}
	}

	protected ActionCard getTopActionCard() {
		return bank.getTopActionCard();
	}

	protected void returnCareerCard(OccupationCard careerCardToBeReturned, CareerPathTypes careerPathType) {
	    if(careerPathType == CareerPathTypes.StandardCareer) {
            bank.returnStandardCareerCard(careerCardToBeReturned);
        }
        else{
            bank.returnCollegeCareerCard(careerCardToBeReturned);
        }
	}
    protected HouseCard getTopHouseCard() {
        return bank.getTopHouseCard();
    }

    protected void returnHouseCard(HouseCard houseCardToBeReturned) {
        bank.returnHouseCard(houseCardToBeReturned);
    }

    protected void storePendingChoiceCards(ArrayList<Card> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    protected ArrayList<Card> getPendingCardChoices(){
        return pendingCardChoices;
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
