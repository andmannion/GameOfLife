package ie.ucd.engac.lifegamelogic.playerlogic;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.GameLogic;

import java.util.ArrayList;

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

    /**
     * Constructor
     * @param playerNumber number this player is to be initialised with
     */
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

    /**
     * returns the number of this player
     */
	public int getPlayerNumber() {
		return playerNumber;
	}

    /**
     * returns the colour of this player
     */
    public PlayerColour getPlayerColour() {
        return playerColour;
    }

    //Retirement related

    /**
     * returns the bonus applicable to this player on retirement
     * @param numberOfRetirees the number of players already retired
     */
    public int computeRetirementBonuses(int numberOfRetirees){
		if(numberOfRetirees >= 0 && numberOfRetirees < GameConfig.max_num_players) {
            int retirementBonus = (GameConfig.max_num_players - numberOfRetirees) * GameConfig.ret_bonus_remaining;
            int actionCardBonus = getNumberOfActionCards() * GameConfig.ret_bonus_action;
            int childrenBonus = getNumberOfChildren() * GameConfig.ret_bonus_kids;
            return retirementBonus + actionCardBonus + childrenBonus;
        }
        else{
            throw  new RuntimeException("Tried retiring invalid number of players. "+ numberOfRetirees + " is not a valid number of retirees");//TODO uhh?
        }
    }

    //Location related

    /**
     * returns the players current location
     */
    public BoardLocation getCurrentLocation() {
		return currentBoardLocation;
	}

    /**
     * sets the players current location
     */
	public void setCurrentLocation(BoardLocation boardLocation) {
		currentBoardLocation = boardLocation;
	}

	//Dependant related

    /**
     * returns the number of dependants this player has
     */
	public int getNumberOfDependants() {
		return numberOfDependants;
	}

    /**
     * increases the number of dependants a player has
     * @param numberOfNewDependants non negative number of dependants to add
     */
	public void addDependants(int numberOfNewDependants) {
		if(numberOfNewDependants >= 0) {
			numberOfDependants += numberOfNewDependants;
		}
	}

    /**
     * returns the number of dependants that are not a spouse
     */
	private int getNumberOfChildren(){
	    return getNumberOfDependants() - 1;
    }

	// Career related

    /**
     * returns the player's career path
     */
    public CareerPathTypes getCareerPath() {
        return careerPathTaken;
    }

    /**
     * sets the players career path
     */
    public void setCareerPath(CareerPathTypes careerPathTypes) {
        careerPathTaken = careerPathTypes;
    }

    /**
     * gets the player's occupation card
     */
	public OccupationCard getOccupationCard() {
		return occupationCard;
	}

    /**
     * sets the players occupation card
     */
	public void setOccupationCard(OccupationCard occupationCard) {
		this.occupationCard = occupationCard;
	}

	//Balance related
    /**
     * gets the player's current balance
     */
    public int getCurrentMoney() {
		return currentMoney;
	}

    /**
     * adds to the players balance
     * @param amountToAdd non negative integer amount of money to add
     */
	public void addToBalance(int amountToAdd) {
        if (amountToAdd > 0) {
            currentMoney += amountToAdd;
		}
	}

    /**
     * subtracts from the players balance
     * @param amountToSubtract non negative integer amount of money to subtract
     */
	public void subtractFromBalance(int amountToSubtract) {
        if (amountToSubtract > 0) {
	        currentMoney -= amountToSubtract;
        }
    }

    /**
     * gets the current number of unpaid loans
     */
	public int getNumberOfLoans(GameLogic gameLogic){
        return gameLogic.getNumberOfLoans(playerNumber);
    }

    /**
     * gets the total repayment value of outstanding loans
     * @param gameLogic GameLogic object which is an interface to the loan provider
     * @return the total money outstanding
     */
    public int getTotalLoansOutstanding(GameLogic gameLogic){ //TODO remove this method
        return gameLogic.getTotalOutstandingLoans(playerNumber);
    }

    //Action cards

    /**
     * adds an action card to the players stash
     */
    public void addActionCard(ActionCard actionCard){
	    actionCards.add(actionCard);
    }

    /**
     * gets the action cards held by the player
     */
	public ArrayList<ActionCard> getActionCards() {
		return actionCards;
	}

    /**
     * gets the number of action cards held by the player
     */
	public int getNumberOfActionCards(){
        return actionCards.size();
    }

    //House cards
    /**
     * gets the house cards held by the player
     */
    public ArrayList<HouseCard> getHouseCards() {
        return houseCards;
    }

    /**
     * adds an action card to the players stash
     */
	public void addHouseCard(HouseCard houseCard){
        houseCards.add(houseCard);
    }

    /**
     * gets the number of action cards held by the player
     */
    public int getNumberOfHouseCards(){
	    return houseCards.size();
    }

    /**
     * sells a housecard owned by the player
     * @param cardIndex the index of the card to sell
     * @param spinResult the spinner value to determine the sale price
     * @return null if invalid index, the sold house card otherwise
     */
	public HouseCard sellHouseCard(int cardIndex, int spinResult){
		HouseCard houseCard = null;
		if(cardIndex >= 0 && cardIndex < houseCards.size()) {
			houseCard = houseCards.get(cardIndex);

			boolean oddNumberWasSpun = !(spinResult%2 == 0);

			addToBalance(houseCard.getSpinForSalePrice(oddNumberWasSpun));
			houseCards.remove(cardIndex);
		}
		return houseCard;
	}

	//Marriage related

    /**
     * sets the players marital status
     */
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		// This method assumes you can only be married to one person at a time...
		if(this.maritalStatus == MaritalStatus.Single && maritalStatus == MaritalStatus.Married) {
			this.maritalStatus = maritalStatus;
			addDependants(1); // Spouse is classed as a dependant
		}
	}
    /**
     * gets the players marital status
     */
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	//Fork choices
    /**
     * checks if there is a pending fork choice this player
     */
	public BoardLocation getPendingBoardForkChoice() {
		return pendingBoardForkChoice;
	}

    /**
     * sets the pending for choice
     */ //TODO Andrew please write this javadoc
	public void setPendingBoardForkChoice(BoardLocation pendingBoardForkChoice) {
		this.pendingBoardForkChoice = pendingBoardForkChoice;
	}

	//remaining moves

    /**
     * get the number of remaining moves this turn
     */
	public int getMovesRemaining() {
		return movesRemaining;
	}

    /**
     *  set the number of remaining moves for this turn
     */
	public void setMovesRemaining(int movesRemaining) {
		this.movesRemaining = movesRemaining;
	}

	//overrides
    @Override
    public boolean equals(Object object){
        if ( object.getClass().isAssignableFrom( this.getClass() ) ) {
            return ((Player) object).getPlayerNumber() == this.getPlayerNumber();
        }
        else{
            return false;
        }
	}
}
