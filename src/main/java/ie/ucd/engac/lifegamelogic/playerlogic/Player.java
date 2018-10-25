package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;

public class Player {

    private int playerNumber;
    private int numDependants; // This doesn't include partner
    private int currentMoney;

	private ArrayList<ActionCard> actionCards;
	private ArrayList<HouseCard> houseCards;

	private OccupationCard occupationCard;
	private CareerPathTypes careerPathTaken;
	
	private MaritalStatus maritalStatus;
	
	private PlayerColour playerColour;

	private BoardLocation currentBoardLocation;
	private BoardLocation pendingBoardForkChoice;	
	private int movesRemaining;

	public Player(int playerNumber) {
		actionCards = new ArrayList<>();
		houseCards = new ArrayList<>();
		occupationCard = null;
		careerPathTaken = null;

		maritalStatus = MaritalStatus.Single;
		this.playerColour = PlayerColour.fromInt(playerNumber);
		this.playerNumber = playerNumber;
		numDependants = 0;
		currentMoney = 200000;

		pendingBoardForkChoice = null;
		movesRemaining = 0;
	}

	public void sellHouseCard(int cardIndex, int spinResult){
	    if(cardIndex >= 0) {
	        Boolean bool;
	        HouseCard houseCard = houseCards.get(cardIndex);
            if (spinResult%2 == 0){ //TODO simplfy
                bool = false;
            }
            else{
                bool = true;
            }
            addToBalance(houseCard.getSpinForSalePrice(bool));
            houseCards.remove(cardIndex);
        }
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

	public CareerPathTypes getCareerPath() {
		return careerPathTaken;
	}
	
	public void setCareerPath(CareerPathTypes careerPathTypes) {
		careerPathTaken = careerPathTypes;
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

	public void addHouseCard(HouseCard houseCard){
		// TODO: Must prompt the user if they want to go into debt to buy a house
		// before getting to this point
		subtractFromBalance(houseCard.getPurchasePrice());
		
        houseCards.add(houseCard);
    }

    public int getNumHouseCards(){
	    return houseCards.size();
    }

	public int getPlayerNumber() {
		return playerNumber;
	}
	
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		// This method assumes you can only be married to one person at a time...
		if(this.maritalStatus == MaritalStatus.Single && maritalStatus == MaritalStatus.Married) {
			this.maritalStatus = maritalStatus;
			addDependants(1); // Spouse is classed as a dependant
		}
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
