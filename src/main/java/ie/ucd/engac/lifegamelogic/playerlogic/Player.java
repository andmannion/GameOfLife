package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.ActionCards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.CollegeCareerCards.CollegeCareerCard;

public class Player {
	// Action cards held by this player
	private ArrayList<ActionCard> actionCards;

	// House cards held by this player
	private ArrayList<HouseCard> houseCards;

	private CareerCard careerCard;
	private CollegeCareerCard collegeCareerCard;

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
		careerCard = null;
		collegeCareerCard = null;

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

	// Number of people in the car
	public int getNumDependants() {
		return numDependants;
	}

	public void addDependants(int numNewDependants) {
		numDependants += numNewDependants;
	}

	// Current career card
	public CareerCard getCareerCard() {
		return careerCard;
	}

	public void setCareerCard(CareerCard careerCard) {
		this.careerCard = careerCard;
	}

	public CollegeCareerCard getCollegeCareerCard() {
		return collegeCareerCard;
	}

	public void setCollegeCareerCard(CollegeCareerCard collegeCareerCard) {
		this.collegeCareerCard = collegeCareerCard;
	}

	// Current amount of money
	public int getCurrentMoney() {
		return currentMoney;
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