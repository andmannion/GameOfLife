package ie.ucd.engac.lifegamelogic;

import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboard.GameBoard;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gamestates.GameSetupState;
import ie.ucd.engac.lifegamelogic.gamestates.GameState;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerMoneyComparator;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.ShadowPlayer;

import java.util.ArrayList;

public class GameLogic {
	private Bank bank;
	private Spinnable spinner;
	private ArrayList<Player> players;
	private ArrayList<Player> retiredPlayers;
	private GameBoard gameBoard;
	private int currentPlayerIndex;
	private int numberOfUnConfiguredPlayers;
	private LifeGameMessage currentLifeGameMessageResponse;
	
	private GameState currentState;

    /**
     * Constructor
     * @param gameBoard the board to use for this game
     * @param numPlayers the number of players
     * @param spinner spinnable object to determine player rolls
     */
	public GameLogic(GameBoard gameBoard, int numPlayers, Spinnable spinner) {
		this.gameBoard = gameBoard;
		this.spinner = spinner;
		bank = new Bank();
		
		initialisePlayers(numPlayers);
		
        retiredPlayers = new ArrayList<>();
		
		currentState = new GameSetupState();
		currentState.enter(this);
	}

    /**
     * sets the spinnable object
     */
    public void setSpinner(Spinnable spinner) {
        this.spinner = spinner;
    }

    /**
     * function to handle messages from the user interface and compute the response
     * @param lifeGameMessage message from the user interface
     * @return message from the logic to the user interface
     */
    public LifeGameMessage handleInput(LifeGameMessage lifeGameMessage) {
		GameState nextGameState = currentState.handleInput(this, lifeGameMessage);
		
		if(nextGameState != null) {
			currentState = nextGameState;
			currentState.enter(this);
		}
		
		return getLifeGameMessageResponse();
	}

	// Player related

    /**
     * create a shadow player object based on a player
     * @param playerIndex the index of the player to create the shadow player based on
     * @return shadow player object
     */
    public ShadowPlayer getShadowPlayer(int playerIndex){
	    Player player = getPlayerByIndex(playerIndex);

        int numLoans = player.getNumberOfLoans(this);
        int loans = player.getTotalLoansOutstanding(this);
        GameBoardTile gameBoardTile = gameBoard.getGameBoardTileFromID(player.getCurrentLocation());

        return new ShadowPlayer(player,numLoans,loans,gameBoardTile);
    }

    /**
     * create a shadow player object based on the current player
     * @return shadow player object
     */
    public ShadowPlayer getCurrentShadowPlayer(){
        return getShadowPlayer(currentPlayerIndex);
    }

    /**
     * subtract from the balance of the current player
     * @param amountToSubtract non negative amount to subtract
     */
    public void subtractFromCurrentPlayersBalance(int amountToSubtract){
        subtractFromPlayersBalance(currentPlayerIndex, amountToSubtract);
    }

    /**
     * subtract from a given players balance
     * @param playerIndex the player to deduct the money from
     * @param amountToSubtract non negative amount to subtract
     */
    public void subtractFromPlayersBalance(int playerIndex, int amountToSubtract){
        if (amountToSubtract >= 0) {
            Player player = getPlayerByIndex(playerIndex);
            while (player.getCurrentMoney() - amountToSubtract < 0) { // user has to take out loans or else they go bankrupt
                player.addToBalance(takeOutALoan(player.getPlayerNumber()));
            }
            player.subtractFromBalance(amountToSubtract);
        }
    }

    private void initialisePlayers(int numPlayers) {
        players = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
            players.add(new Player(playerIndex+1));
        }
        // All of these players require the user to set some initial characteristics
        numberOfUnConfiguredPlayers = numPlayers;
    }

    /**
     * returns an ArrayList of the players
     */
    public ArrayList<Player> getPlayers(){
    	return players;
    }

    /**
     * gets the index of the current player
     */
    public int getCurrentPlayerIndex(){
	    return currentPlayerIndex;
    }

    /**
     * gets the current player
     */
	public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * increment the current player index
     */
    public void setNextPlayerToCurrent() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * get the number of players
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * get the player at a certain index
     * @param playerIndex index at which to get the player
     */
    public Player getPlayerByIndex(int playerIndex) {
        if(playerIndex < 0 || playerIndex > players.size()) {
            return null;
        }
        return players.get(playerIndex);
    }

    /**
     * get the index of the next player
     * @param playerIndex the index of the original player
     * @return the index of the player after the current one
     */
    public int getNextPlayerIndex(int playerIndex) {
        if(playerIndex < 0 || playerIndex > players.size()) {
            return -1;
        }
        return (playerIndex + 1) % players.size();
    }

    /**
     * gets the number of player who have not had a turn yet
     */
    public int getNumberOfUninitialisedPlayers() {
        return numberOfUnConfiguredPlayers;
    }

    /**
     * decrements the number of players that have not yet had a turn
     */
    public void decrementNumberOfUninitialisedPlayers() {
        if(numberOfUnConfiguredPlayers > 0) {
            numberOfUnConfiguredPlayers--;
        }
    }

    /**
     * gets the current spinnable object
     */
    public Spinnable getSpinner() {
    	return spinner;
    }
    
    // Retirement related

    /**
     * moves the current player from the active group to the retired group
     * @return the amount of money the player had at retirement
     * @throws RuntimeException if the player cannot be retired correctly
     */
    public int retireCurrentPlayer() throws RuntimeException {
	    try{
	        Player playerToRetire = players.get(currentPlayerIndex);
	        int retirementBonus = playerToRetire.computeRetirementBonuses(getNumberOfRetiredPlayers());
            playerToRetire.addToBalance(retirementBonus);

            int loanRepaymentCost = getTotalOutstandingLoans(playerToRetire.getPlayerNumber());
            subtractFromCurrentPlayersBalance(loanRepaymentCost);
            repayAllLoans(playerToRetire.getPlayerNumber());

            players.remove(currentPlayerIndex);
            retiredPlayers.add(playerToRetire);

            if(players.size()>0){ //correct the index after removal unless the game is over
                correctCurrentPlayerIndexAfterRetirement();
            }
            return playerToRetire.getCurrentMoney();
        }
        catch (IndexOutOfBoundsException ex){
	        throw new RuntimeException("Attempted to retire a player that does not exist. No player at index: " + currentPlayerIndex);
        }
    }

    private void correctCurrentPlayerIndexAfterRetirement(){
        currentPlayerIndex = (currentPlayerIndex-1)%players.size();
    }

    private int getNumberOfRetiredPlayers(){
	    return retiredPlayers.size();
    }

    /**
     * returns a list of the retired players, sorted based on their balance
     */
    public ArrayList<Player> getRankedRetiredPlayers(){
	    ArrayList<Player> ranked = retiredPlayers;
        ranked.sort(new PlayerMoneyComparator());
        return ranked;
    }

    // Career related
    /**
     * moves a player to the start tile for the college career path
     * @param playerIndex the player to move
     */
    public void movePlayerToInitialCollegeCareerPath(int playerIndex) {
        BoardLocation collegeCareerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(1);

        players.get(playerIndex).setCurrentLocation(collegeCareerPathInitialLocation);
    }

    /**
     * moves a player to the start tile for the standard career path
     * @param playerIndex the player to move
     */
    public void movePlayerToInitialCareerPath(int playerIndex) {
        BoardLocation careerPathInitialLocation = gameBoard.getOutboundNeighbours(new BoardLocation("a")).get(0);

        players.get(playerIndex).setCurrentLocation(careerPathInitialLocation);
    }

    // GameBoard related

    /**
     * gets the gameboard object
     */
    public GameBoard getGameBoard() {
		return gameBoard;
	}

    /**
     * gets a list of the potential forward locations a player can move to
     * @param currentBoardLocation the current location of the player
     */
    public ArrayList<BoardLocation> getAdjacentForwardLocations(BoardLocation currentBoardLocation) {
        return gameBoard.getOutboundNeighbours(currentBoardLocation);
    }

	// Bank related

    /**
     * gets the number of loans a player has withdrawn
     * @param playerNumber the player's number to use as an identifier
     */
    public int getNumberOfLoans(int playerNumber) {
        return bank.getNumberOfOutstandingLoans(playerNumber);
    }

    /**
     * repays all the loans belonging to a player
     * @param playerNumber the player's number to use as an identifier
     */
    private void repayAllLoans(int playerNumber){
        bank.repayAllLoans(playerNumber);
    }

    /**
     * gets the total amount of money owing by the player
     * @param playerNumber the player's number to use as an identifier
     */
    public int getTotalOutstandingLoans(int playerNumber) {
        return bank.getOutstandingLoanTotal(playerNumber);
    }

    /**
     * withdraws a loan
     * @param playerNumber the player's number to use as an identifier
     */
    public int takeOutALoan(int playerNumber){
	    return bank.takeOutALoan(playerNumber);
    }

    // Message related
    private LifeGameMessage getLifeGameMessageResponse() {
        return currentLifeGameMessageResponse;
    }

    /**
     * gets the response message from the logic to the user interface
     * @param lifeGameMessage the message to set as the response
     */
    public void setResponseMessage(LifeGameMessage lifeGameMessage) {
        currentLifeGameMessageResponse = lifeGameMessage;
    }

    // Occupation card related
    /**
     * gets the top standard career card from the deck
     */
    public OccupationCard getTopStandardCareerCard() {
        return bank.getTopStandardCareerCard();
    }

    /**
     * gets the top college career card from the deck
     */
    public OccupationCard getTopCollegeCareerCard() {
        return bank.getTopCollegeCareerCard();
    }

    /**
     * returns an occupation card to the bottom of the deck
     * @param occupationCardToBeReturned the card to return
     */
    public void returnOccupationCard(OccupationCard occupationCardToBeReturned) {
        if(occupationCardToBeReturned.getOccupationCardType() == OccupationCardTypes.Career) {
            bank.returnStandardCareerCard(occupationCardToBeReturned);
        }
        else {
            bank.returnCollegeCareerCard(occupationCardToBeReturned);
        }
    }

    // Action card related
    /**
     * gets the top action card from the deck
     */
    public ActionCard getTopActionCard() {
        return bank.getTopActionCard();
    }

    // House card related

    /**
     * gets the top house card from the deck
     */
    public HouseCard getTopHouseCard() {
        return bank.getTopHouseCard();
    }

    /**
     * returns a house card to the bottom of the deck
     * @param houseCardToBeReturned the house card to return
     */
    public void returnHouseCard(HouseCard houseCardToBeReturned) {
        bank.returnHouseCard(houseCardToBeReturned);
    }
}
