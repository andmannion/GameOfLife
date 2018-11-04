package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.ArrayList;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogic;

public class Player {

    private int playerNumber;
    private int numberOfDependants; // This doesn't include partner
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
		numberOfDependants = 0;
		currentMoney = GameConfig.starting_money;

		pendingBoardForkChoice = null;
		movesRemaining = 0;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

    public PlayerColour getPlayerColour() {
        return playerColour;
    }

    public int retirePlayer(int numberOfRetirees, GameLogic gameLogic){ //bank hashmap uses number, not index
        final int THOUSAND = 1000;
        /*
        retirement steps:
            add bonus for earliness
            action cards
            houses
            children
            loans
         */
        int retirementBonus = (GameConfig.max_num_players -numberOfRetirees)*100*THOUSAND; //TODO refactor?
        int actionCardBonus = getNumberOfActionCards()*100*THOUSAND;
        int childrenBonus = getNumberOfChildren()*50*THOUSAND;

        addToBalance(retirementBonus+actionCardBonus+childrenBonus);

        int loanRepaymentCost = gameLogic.getTotalOutstandingLoans(playerNumber);

        gameLogic.subtractFromCurrentPlayersBalance(loanRepaymentCost);
        gameLogic.repayAllLoans(playerNumber);
        return getCurrentMoney();
	}

    //Location related
	public BoardLocation getCurrentLocation() {
		return currentBoardLocation;
	}
	
	public void setCurrentLocation(BoardLocation boardLocation) {
		currentBoardLocation = boardLocation;
	}

	//Dependant related
	public int getNumberOfDependants() {
		return numberOfDependants;
	}

	public void addDependants(int numberOfNewDependants) {
		numberOfDependants += numberOfNewDependants;
	}

	private int getNumberOfChildren(){
	    return getNumberOfDependants() - 1;
    }

	// Career related
    public CareerPathTypes getCareerPath() {
        return careerPathTaken;
    }

    public void setCareerPath(CareerPathTypes careerPathTypes) {
        careerPathTaken = careerPathTypes;
    }

	public OccupationCard getOccupationCard() {
		return occupationCard;
	}

	public void setOccupationCard(OccupationCard occupationCard) {
		this.occupationCard = occupationCard;
	}

	//Balance related
    public int getCurrentMoney() {
		return currentMoney;
	}
	
	public void addToBalance(int amountToAdd) {
		currentMoney += amountToAdd;
	}
	
	public void subtractFromBalance(int amountToSubtract) {
	    currentMoney -= amountToSubtract;
	}

	public int getNumberOfLoans(GameLogic gameLogic){
        return gameLogic.getNumberOfLoans(playerNumber);
    }

    public int getTotalLoansOutstanding(GameLogic gameLogic){
        return gameLogic.getTotalOutstandingLoans(playerNumber);
    }

    //Action cards
	public ArrayList<ActionCard> getActionCards() {
		return actionCards;
	}

	public int getNumberOfActionCards(){
        return actionCards.size();
    }

    //House cards
    public ArrayList<HouseCard> getHouseCards() {
        return houseCards;
    }

	public void addHouseCard(HouseCard houseCard){
        houseCards.add(houseCard);
    }

    public int getNumberOfHouseCards(){
	    return houseCards.size();
    }

	public void sellHouseCard(int cardIndex, int spinResult){
		if(cardIndex >= 0) {
			boolean bool;
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

	//Marriage related
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

	//Fork choices
	public BoardLocation getPendingBoardForkChoice() {
		return pendingBoardForkChoice;
	}
	
	public void setPendingBoardForkChoice(BoardLocation pendingBoardForkChoice) {
		this.pendingBoardForkChoice = pendingBoardForkChoice;
	}

	//remaining moves
	public int getMovesRemaining() {
		return movesRemaining;
	}
	
	public void setMovesRemaining(int movesRemaining) {
		this.movesRemaining = movesRemaining;
	}

}
