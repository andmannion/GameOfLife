package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;

public class Player {
	// Action cards held by this player
	private ArrayList<ActionCard> actionCards;

	// House cards held by this player
	private ArrayList<HouseCard> houseCards;

	private OccupationCard occupationCard;

	// Marital status
	private MaritalStatus maritalStatus;

	private PlayerColour playerColour;

	private int playerNumber;

	private int numDependants;

	private int currentMoney;

	private BoardLocation currentBoardLocation;
	private BoardLocation pendingBoardForkChoice;	
	private int movesRemaining;

	public Player(int playerNumber) {
		actionCards = new ArrayList<>();
		houseCards = new ArrayList<>();
		occupationCard = null;

		maritalStatus = MaritalStatus.Single;
		this.playerColour = PlayerColour.fromInt(playerNumber);
		this.playerNumber = playerNumber;
		numDependants = 0;
		currentMoney = 200000;
		pendingBoardForkChoice = null;
		movesRemaining = 0;
	}

	public BoardLocation getCurrentLocation() {
		return currentBoardLocation;
	}
	
	public void setCurrentLocation(BoardLocation boardLocation) {
		currentBoardLocation = boardLocation;
	}

	// Number of people in the car
	public int getNumDependants() {
		return numDependants;
	}

	public void addDependants(int numNewDependants) {
		numDependants += numNewDependants;
	}

	// Current career card
	public OccupationCard getOccupationCard() {
		return occupationCard;
	}

	public void setOccupationCard(OccupationCard occupationCard) {
		this.occupationCard = occupationCard;
	}

	// Current amount of money
	public int getCurrentMoney() {
		return currentMoney;
	}
	
	public void addToBalance(int amountToAdd) {
		currentMoney += amountToAdd;
	}
	
	/* TODO: Should return type be boolean to signal if a loan is required, as the amount to be subtracted would
	* send the balance negative?
	*/
	public void subtractFromBalance(int amountToSubtract) {
		currentMoney -= amountToSubtract;
	}

	public ArrayList<ActionCard> getActionCards() {
		return actionCards;
	}

	public ArrayList<HouseCard> getHouseCards() {
		return houseCards;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	
	public PlayerColour getPlayerColour() {
		return playerColour;
	}

	public BoardLocation getPendingBoardForkChoice() {
		return pendingBoardForkChoice;
	}
	
	public void setPendingBoardForkChoice(BoardLocation pendingBoardForkChoice) {
		this.pendingBoardForkChoice = pendingBoardForkChoice;
	}

	public int getMovesRemaining() {
		return movesRemaining;
	}
	
	public void setMovesRemaining(int movesRemaining) {
		this.movesRemaining = movesRemaining;
	}
}