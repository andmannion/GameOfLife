package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.GetCashFromBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.PayTheBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.PlayersPayActionCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboard.GameBoard;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinRequestMessage;

import java.util.ArrayList;

public class HandlePlayerMoveState extends GameState {
    private String eventMessage ;

	public HandlePlayerMoveState(String eventMessage){
        this.eventMessage = eventMessage;
    }

	public HandlePlayerMoveState() {}
	
	@Override
	public void enter(GameLogic gameLogic) {
        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        if (eventMessage == null){
            eventMessage = "Player " + playNum + "'s turn.";
        }
        SpinRequestMessage spinRequestMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()), playNum, eventMessage);
        gameLogic.setResponseMessage(spinRequestMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null;

		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) { 
			// TODO: forkchoice should be done when you land on it - that's the natural flow as the user
			GameBoard gameBoard = gameLogic.getGameBoard();
			int tilesToMove;
            int tilesMoved = 0;
            GameBoardTile endTile;

            // Need to spin the spinner
            tilesToMove = gameLogic.getSpinner().spinTheWheel();
            
            /* Must give the bonus salary to the player(s) depending with the value has been spun
			*  based on the bonus number on their current OccupationCard.	
		    */            
            assignSpinBonusIfRequired(gameLogic.getPlayers(), tilesToMove);

			// Need to alternate between moving and evaluating the tile we're on
            endTile = tryToMove(gameLogic.getCurrentPlayer(), gameBoard, tilesToMove, tilesMoved);
            
            
			// At this point, we have landed on a tile, either through the number of goes running out, or by encountering a stop tile.
            nextState = evaluateTile(gameLogic, endTile);
		}

        if (nextState == null) {

            if (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                // Must send a message to choose a career path, etc.
                LifeGameMessage replyMessage = PathChoiceState.constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber());
                gameLogic.setResponseMessage(replyMessage);

                return new PathChoiceState();
            }
            return null;
        }
        else {
            return nextState;
        }
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
	}

	private void assignSpinBonusIfRequired(ArrayList<Player> players, int spinResult) {
		for(Player player : players) {
			OccupationCard occupationCard = player.getOccupationCard();
			if(occupationCard != null) {
				if(occupationCard.getBonusNumber() == spinResult) {
					player.addToBalance(occupationCard.getBonusPaymentAmount());
				}
			}
		}
	}

	private GameBoardTile tryToMove(Player currentPlayer, GameBoard gameBoard, int tilesToMove, int tilesMoved){
        boolean stopTileEncountered = false;
        BoardLocation currentBoardLocation = currentPlayer.getCurrentLocation();
        GameBoardTile currentTile = gameBoard.getGameBoardTileFromID(currentBoardLocation);

        BoardLocation pendingLocation = currentPlayer.getPendingBoardForkChoice();
        
        if(pendingLocation != null) {
        	// Move to the pending choice
            currentPlayer.setCurrentLocation(pendingLocation);
            currentPlayer.setPendingBoardForkChoice(null);
        	tilesMoved++;
        }
        
        while (tilesMoved < tilesToMove && !stopTileEncountered) {
            tilesMoved++;
            // Go forward
            currentBoardLocation = currentPlayer.getCurrentLocation();
            
            ArrayList<BoardLocation> adjacentForwardLocations = gameBoard.getOutboundNeighbours(currentBoardLocation);    
            
            // For the moment, no tiles other than stop tiles have branches
            if (1 == adjacentForwardLocations.size()) {
                BoardLocation currentLocation = adjacentForwardLocations.get(0);
                currentPlayer.setCurrentLocation(currentLocation);

                // Need to get the tile that this boardLocation relates to
                currentTile = gameBoard.getGameBoardTileFromID(currentLocation);

                stopTileEncountered = (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop);

                if (tilesMoved < tilesToMove && !stopTileEncountered) {
                    // Perform actions if the tile requires action when passed over
                    performUpdateIfPassingOverTile(currentPlayer, currentTile);
                }
            }
            else if(0 == adjacentForwardLocations.size()) {
                // Must initiate retirement procedure
                System.err.println("No spaces remaining ahead"); //TODO this should now be unreachable
            }
        }

        return currentTile;
    }

	private GameState evaluateTile(GameLogic gameLogic, GameBoardTile currentTile){
	    GameState nextState = null;
        switch (currentTile.getGameBoardTileType()) {
            case Start:
                nextState = new EndTurnState();
                break;
            case Payday:
            	String paydayLandedOnMessage = handlePaydayTile(gameLogic.getCurrentPlayer());
                nextState = new EndTurnState(paydayLandedOnMessage);
                break;
            case Action:                
                nextState = evaluateActionTile(gameLogic);
                break;
            case Holiday:  
            	String holidayMessage = "You are on holiday, so do nothing for this turn.";
                nextState = new EndTurnState(holidayMessage);
                break;
            case SpinToWin:
                nextState = new SpinToWinSetupState();
                break;
            case Baby:
            	gameLogic.getCurrentPlayer().addDependants(1);
                int currentPlayerIndex = gameLogic.getCurrentPlayerIndex();
            	String babyNonStopTileMessage = "Player " + gameLogic.getPlayerByIndex(currentPlayerIndex).getPlayerNumber() + ", you have had a baby.";
                nextState = new EndTurnState(babyNonStopTileMessage); 
                break;
            case Twins:
                gameLogic.getCurrentPlayer().addDependants(2);
                currentPlayerIndex = gameLogic.getCurrentPlayerIndex();
                String twinsNonStopTileMessage = "Player " + gameLogic.getPlayerByIndex(currentPlayerIndex).getPlayerNumber() + ", you have had twins.";
                nextState = new EndTurnState(twinsNonStopTileMessage);
                break;
            case House:
                nextState = new HouseTileDecisionState();
                break;
            case Stop:
            	nextState = evaluateStopTile(gameLogic, (GameBoardStopTile) currentTile);
                break;
            default:
                // There's no console to print to...
                // TODO: use some utility log file class to write the errors of the program to
                break;
        }
        return nextState;
    }
	
	private GameState evaluateStopTile(GameLogic gameLogic, GameBoardStopTile currentTile) { 
        GameState nextState = null;
        
        switch(currentTile.getGameBoardStopTileType()) {
            case Graduation:
                nextState = new GraduationState();
                break;
            case GetMarried:
                int currentNumberOfPlayers = gameLogic.getNumberOfPlayers();
                int currentPlayerNumber = gameLogic.getPlayerByIndex(gameLogic.getCurrentPlayerIndex()).getPlayerNumber();
                nextState = handleGetMarriedTile(currentNumberOfPlayers, currentPlayerNumber);
                break;
            case NightSchool:
                nextState = new NightSchoolState();
                break;
            case Family:
                nextState = new FamilyState(); 
                break;
            case Baby:
                nextState = new BabyState(); 
                break;
            case Holiday:
            	String holidayMessage = "You are on holiday, so do nothing for this turn.";
                nextState = new EndTurnState(holidayMessage);
                break;
            case Retire:
                nextState = retireThisPlayer(gameLogic);
                break;
            default:
                nextState = new EndTurnState();
                System.err.println("Player has landed on an unhandled stop tile.");
                break;
        }
        return nextState;
	}

	private GameState evaluateActionTile(GameLogic gameLogic) {
		ActionCard thisAction = gameLogic.getTopActionCard();
        Player player = gameLogic.getCurrentPlayer();
        player.addActionCard(thisAction);
        
        GameState nextActionState = null;
        
        switch (thisAction.getActionCardType()){
            case CareerChange:
                if(player.getOccupationCard() != null) {
                    nextActionState = new CareerChangeState(); //TODO test
                }
                else{
                    String eventMessage = "CareerChange: Cannot change career before graduation.";
                    nextActionState = new EndTurnState(eventMessage);
                }
                break;
            case PlayersPay:
                if(gameLogic.getNumberOfPlayers() == 1){
                	nextActionState = new EndTurnState("PlayersPay: No players remaining to pick");
                }
                else {
                	nextActionState = new PickPlayerState((PlayersPayActionCard) thisAction); ///TODO test
                }
                break;
            case PayTheBank:
                PayTheBankActionCard payBank = (PayTheBankActionCard) thisAction;
                gameLogic.subtractFromCurrentPlayersBalance(payBank.getValue()); //TODO test
                nextActionState = new EndTurnState("Action: Paid the bank " + payBank.getValue());
                break;
            case GetCashFromBank:
                GetCashFromBankActionCard getCash = (GetCashFromBankActionCard) thisAction;//TODO test
                player.addToBalance(getCash.getAmountToPay());
                nextActionState = new EndTurnState("Action: Received " + getCash.getAmountToPay());
                break;
        }
        
        return nextActionState;
	}
	
	private GameState handleGetMarriedTile(int currentNumberOfPlayers, int currentPlayerNumber){
	    GameState nextState;
        if(1 == currentNumberOfPlayers){

            String eventMsg = "You got married, player " + currentPlayerNumber + ", so take an extra turn.";

            nextState = new HandlePlayerMoveState(eventMsg);
        }
        else {
            nextState = new GetMarriedState();
        }
        return nextState;
    }

	private void performUpdateIfPassingOverTile(Player currentPlayer, GameBoardTile currentTile) {
		if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday) {
			// Player should collect the salary indicated in their Career/College Career
			// card from the Bank
			OccupationCard currentOccupationCard = currentPlayer.getOccupationCard();
			
			if(currentOccupationCard != null) {
				int currentSalary = currentOccupationCard.getSalary();
				// Get money from the bank, increment the player's balance by that amount
                currentPlayer.addToBalance(currentSalary);
			}
		}
	}

	private String handlePaydayTile(Player currentPlayer){
		String paydayUpdateString = "";
		
        OccupationCard currentOccupationCard = currentPlayer.getOccupationCard();
        
        if(currentOccupationCard != null) {
            int currentSalary = currentOccupationCard.getSalary();
            currentPlayer.addToBalance(currentSalary + GameConfig.payday_landed_on_bonus);
            paydayUpdateString = "Player " + currentPlayer.getPlayerNumber() + ", you obtained " + (currentSalary + GameConfig.payday_landed_on_bonus) +
            					 " after landing on a Payday tile.";
        }
        
        return paydayUpdateString;
    }

    private GameState retireThisPlayer(GameLogic gameLogic){
        GameState nextState = null;
        Player retiree = gameLogic.getCurrentPlayer(); //TODO this code appears in 2 states
        if (retiree.getNumberOfHouseCards() == 0){ //if they have houses need to sell them
            int retirementCash = gameLogic.retireCurrentPlayer();
            String eventMessage = "Player " + retiree.getPlayerNumber() + " has retired with " + retirementCash;
            if (gameLogic.getNumberOfPlayers() == 0) {
                nextState = new GameOverState();
            }
            else {
                nextState = new EndTurnState(eventMessage);
            }
        }
        else {
            nextState = new RetirePlayerState(); //TODO test
        }
        return nextState;
    }
}
