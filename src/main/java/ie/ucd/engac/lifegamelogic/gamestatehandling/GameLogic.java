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
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerMoneyComparator;
import ie.ucd.engac.messaging.LifeGameMessage;

public class GameLogic {
    public static final int MAX_NUM_PLAYERS = 4;
	private Bank bank;
	private ArrayList<Player> players;
	private ArrayList<Player> retiredPlayers;
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
        retiredPlayers = new ArrayList<>();
		
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

	// Player related
    private void initialisePlayers(int numPlayers) {
        players = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
            players.add(new Player(playerIndex+1));
        }
        // All of these players require the user to set some initial characteristics
        numberOfUnconfiguredPlayers = numPlayers;
    }

    protected ArrayList<Player> getPlayers(){
    	return players;
    }

    protected int getCurrentPlayerIndex(){
	    return currentPlayerIndex;
    }
	
	protected Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    protected void setNextPlayerToCurrent() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    protected int getNumberOfPlayers() {
        return players.size();
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
    
    protected int getNumberOfUninitialisedPlayers() {
        return numberOfUnconfiguredPlayers;
    }

    protected void decrementNumberOfUninitialisedPlayers() {
        if(numberOfUnconfiguredPlayers > 0) {
            numberOfUnconfiguredPlayers--;
        }
    }

    // Retirement related
    public int retireCurrentPlayer(){
	    Player playerToRetire = players.remove(currentPlayerIndex);
	    int retirementCash = playerToRetire.retirePlayer(getNumberOfRetiredPlayers(),this);
	    retiredPlayers.add(playerToRetire);
	    return retirementCash;
    }

    private int getNumberOfRetiredPlayers(){
	    return retiredPlayers.size();
    }

    public ArrayList<Player> getRankedRetiredPlayers(){
	    ArrayList<Player> ranked = retiredPlayers;
        ranked.sort(new PlayerMoneyComparator());
        return ranked;
    }

    // Career related
    protected void movePlayerToInitialCollegeCareerPath(int playerIndex) {
        BoardLocation collegeCareerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(1);

        players.get(playerIndex).setCurrentLocation(collegeCareerPathInitialLocation);
    }

    protected void movePlayerToInitialCareerPath(int playerIndex) {
        BoardLocation careerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(0);
        
        System.out.println("DEBUG: careerPathInitialLocation string is " + careerPathInitialLocation.getLocation());

        players.get(playerIndex).setCurrentLocation(careerPathInitialLocation);
    }

    // GameBoard related
    protected LogicGameBoard getGameBoard() {
		return gameBoard;
	}

    protected ArrayList<BoardLocation> getAdjacentForwardLocations(BoardLocation currentBoardLocation) {
        return gameBoard.getOutboundNeighbours(currentBoardLocation);
    }

	// Bank related
    protected void extractMoneyFromBank(int amountToExtract) {
        bank.extractMoney(amountToExtract);
    }

    public int getNumberOfLoans(int playerNumber) {
        return bank.getNumberOfOutstandingLoans(playerNumber);
    }

    public void repayAllLoans(int playerNumber){
        bank.repayAllLoans(playerNumber);
    }

    public int getTotalOutstandingLoans(int playerNumber) {
        return bank.getOutstandingLoanTotal(playerNumber);
    }

    public int takeOutALoan(int playerNumber){
	    return bank.takeOutALoan(playerNumber);
    }

    // Message related
    private LifeGameMessage getLifeGameMessageResponse() {
        return currentLifeGameMessageResponse;
    }

    protected void setResponseMessage(LifeGameMessage lifeGameMessage) {
        currentLifeGameMessageResponse = lifeGameMessage;
    }

    // Generic card related
    protected void storePendingChoiceCards(ArrayList<Card> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    protected ArrayList<Card> getPendingCardChoices(){
        return pendingCardChoices;
    }

    // Occupation card related
    protected OccupationCard getTopStandardCareerCard() {
        return bank.getTopStandardCareerCard();
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

    // Action card related
    protected ActionCard getTopActionCard() {
        return bank.getTopActionCard();
    }

    // House card related
    protected HouseCard getTopHouseCard() {
        return bank.getTopHouseCard();
    }

    protected void returnHouseCard(HouseCard houseCardToBeReturned) {
        bank.returnHouseCard(houseCardToBeReturned);
    }
}
