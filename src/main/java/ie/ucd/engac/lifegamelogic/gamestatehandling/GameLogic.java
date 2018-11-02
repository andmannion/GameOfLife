package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerMoneyComparator;
import ie.ucd.engac.messaging.LifeGameMessage;

public class GameLogic {
    public static final int MAX_NUM_PLAYERS = 4;
	private Bank bank;
	private Spinnable spinner;
	private ArrayList<Player> players;
	private ArrayList<Player> retiredPlayers;
	private LogicGameBoard gameBoard;
	private int currentPlayerIndex;
	private int numberOfUnconfiguredPlayers;
	private LifeGameMessage currentLifeGameMessageResponse;
	
	private ArrayList<Card> pendingCardChoices;

	private GameState currentState;
	
	public GameLogic(LogicGameBoard gameBoard, int numPlayers, Spinnable spinner) {
		this.gameBoard = gameBoard;
		this.spinner = spinner;
		bank = new Bank();
		
		initialisePlayers(numPlayers);
		
        retiredPlayers = new ArrayList<>();
		
		currentState = new PathChoiceState();
		currentState.enter(this);
	}

    public void setSpinner(Spinnable spinner) {
        this.spinner = spinner;
    }

    public LifeGameMessage handleInput(LifeGameMessage lifeGameMessage) {
		GameState nextGameState = currentState.handleInput(this, lifeGameMessage);
		
		if(nextGameState != null) {
			currentState = nextGameState;
			currentState.enter(this);
		}
		
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

    public ArrayList<Player> getPlayers(){
    	return players;
    }

    public int getCurrentPlayerIndex(){
	    return currentPlayerIndex;
    }
	
	public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setNextPlayerToCurrent() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Player getPlayerByIndex(int playerIndex) {
        if(playerIndex < 0 || playerIndex > players.size()) {
            return null;
        }
        return players.get(playerIndex);
    }

    public int getNextPlayerIndex(int playerIndex) {
        if(playerIndex < 0 || playerIndex > players.size()) {
            return -1;
        }
        return (playerIndex + 1) % players.size();
    }
    
    public int getNumberOfUninitialisedPlayers() {
        return numberOfUnconfiguredPlayers;
    }

    public void decrementNumberOfUninitialisedPlayers() {
        if(numberOfUnconfiguredPlayers > 0) {
            numberOfUnconfiguredPlayers--;
        }
    }

    public Spinnable getSpinner() {
    	return spinner;
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
    public void movePlayerToInitialCollegeCareerPath(int playerIndex) {
        BoardLocation collegeCareerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(1);

        players.get(playerIndex).setCurrentLocation(collegeCareerPathInitialLocation);
    }

    public void movePlayerToInitialCareerPath(int playerIndex) {
        BoardLocation careerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(0);

        players.get(playerIndex).setCurrentLocation(careerPathInitialLocation);
    }

    // GameBoard related
    public LogicGameBoard getGameBoard() {
		return gameBoard;
	}

    public ArrayList<BoardLocation> getAdjacentForwardLocations(BoardLocation currentBoardLocation) {
        return gameBoard.getOutboundNeighbours(currentBoardLocation);
    }

	// Bank related
    public void extractMoneyFromBank(int amountToExtract) {
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

    public void setResponseMessage(LifeGameMessage lifeGameMessage) {
        currentLifeGameMessageResponse = lifeGameMessage;
    }

    // Generic card related
    public void storePendingChoiceCards(ArrayList<Card> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    public ArrayList<Card> getPendingCardChoices(){
        return pendingCardChoices;
    }

    // Occupation card related
    public OccupationCard getTopStandardCareerCard() {
        return bank.getTopStandardCareerCard();
    }

    public OccupationCard getTopCollegeCareerCard() {
        return bank.getTopCollegeCareerCard();
    }

    public void returnOccupationCard(OccupationCard occupationCardToBeReturned) {
        if(occupationCardToBeReturned.getOccupationCardType() == OccupationCardTypes.Career) {
            bank.returnStandardCareerCard(occupationCardToBeReturned);
        }
        else {
            bank.returnCollegeCareerCard(occupationCardToBeReturned);
        }
    }

    // Action card related
    public ActionCard getTopActionCard() {
        return bank.getTopActionCard();
    }

    // House card related
    public HouseCard getTopHouseCard() {
        return bank.getTopHouseCard();
    }

    public void returnHouseCard(HouseCard houseCardToBeReturned) {
        bank.returnHouseCard(houseCardToBeReturned);
    }
}
