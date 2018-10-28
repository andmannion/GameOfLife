package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.GetCashFromBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.PayTheBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.PlayersPayActionCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class HandlePlayerMoveState implements GameState {

    private final static int PAYDAY_LANDED_ON_BONUS = 100000;
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
        SpinRequestMessage spinRequestMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer(), gameLogic),playNum, eventMessage);
        gameLogic.setResponseMessage(spinRequestMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null;

		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) { 
			// TODO: forkchoice should be done when you land on it - that's the natural flow as the user
			LogicGameBoard gameBoard = gameLogic.getGameBoard();
			int tilesToMove;
            int tilesMoved = 0;
            GameBoardTile endTile;

            // Need to spin the spinner
            tilesToMove = Spinner.spinTheWheel();
            
            /* Must give the bonus salary to the player(s) depending with the value has been spun
			*  based on the bonus number on their current OccupationCard.	
		    */            
            assignSpinBonusIfRequired(gameLogic.getPlayers(), tilesToMove);

			// Need to alternate between moving and evaluating the tile we're on
            endTile = tryToMove(gameLogic, gameBoard, tilesToMove, tilesMoved);

			// At this point, we have landed on a tile, either through the number of goes running out, or by encountering a stop tile.
            nextState = evaluateTile(gameLogic,endTile);
		}

        if (nextState == null) {

            if (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                // Must send a message to choose a career path, etc.
                System.out.println("Still player left to initialise");
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

	private GameBoardTile tryToMove(GameLogic gameLogic, LogicGameBoard gameBoard, int tilesToMove, int tilesMoved){
        boolean stopTileEncountered = false;
        GameBoardTile currentTile = null;
        
        BoardLocation pendingLocation = gameLogic.getCurrentPlayer().getPendingBoardForkChoice();
        
        if(pendingLocation != null) {
        	// Move to the pending choice
        	gameLogic.getCurrentPlayer().setCurrentLocation(pendingLocation);
        	gameLogic.getCurrentPlayer().setPendingBoardForkChoice(null);
        	tilesMoved++;
        }
        
        while (tilesMoved < tilesToMove && !stopTileEncountered) {
            // Go forward
            BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
            ArrayList<BoardLocation> adjacentForwardLocations = gameBoard.getOutboundNeighbours(currentBoardLocation);

            // For the moment, no tiles other than stop tiles have branches
            if (1 == adjacentForwardLocations.size()) {
                BoardLocation currentLocation = adjacentForwardLocations.get(0);
                System.out.println("Current tile: " + currentLocation.getLocation()); //TODO remove
                gameLogic.getCurrentPlayer().setCurrentLocation(currentLocation);

                // Need to get the tile that this boardLocation relates to
                currentTile = gameBoard.getGameBoardTileFromID(currentLocation);

                stopTileEncountered = (currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop);

                if (!stopTileEncountered) {
                    // Perform actions if the tile requires action when passed over
                    performUpdateIfPassingOverTile(currentTile, gameLogic);
                }
            }
            else if(0 == adjacentForwardLocations.size()) {
                // Must initiate retirement procedure
                System.out.println("No spaces remaining ahead"); //TODO this should now be unreachable
            }
            tilesMoved++;
        }
        System.out.println("Landed on a " + currentTile.getGameBoardTileType() + " tile."); //TODO remove
        return currentTile;
    }

	private GameState evaluateTile(GameLogic gameLogic, GameBoardTile currentTile){
	    GameState nextState = null; 
	    
        System.out.println(currentTile.getGameBoardTileType());
        switch (currentTile.getGameBoardTileType()) {
            case Start:
                break;
            case Payday:
            	String paydayLandedOnMessage = handlePaydayTile(gameLogic);
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
            	String babyNonStopTileMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", you have had a baby.";                
                nextState = new EndTurnState(babyNonStopTileMessage); 
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
            	nextState = new GetMarriedState();
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
                System.out.println("Retirement tile"); //TODO remove
                nextState = retireThisPlayer(gameLogic);
                break;
            default:
                // Should be some error message logged to a log file here, quit altogether?
                break;
        }
        return nextState;
	}

	private GameState evaluateActionTile(GameLogic gameLogic) {
		ActionCard thisAction = gameLogic.getTopActionCard();
        Player player = gameLogic.getCurrentPlayer();
        
        GameState nextActionState = null;
        
        switch (thisAction.getActionCardType()){
            case CareerChange:
            	nextActionState = new CareerChangeState(); //TODO test
                break;
            case PlayersPay:
                if(gameLogic.getNumberOfPlayers() == 0){
                	nextActionState = new EndTurnState("No players remaining to pick");
                }
                else {
                	nextActionState = new PickPlayerState((PlayersPayActionCard) thisAction); ///TODO test
                }
                break;
            case PayTheBank:
                PayTheBankActionCard payBank = (PayTheBankActionCard) thisAction;
                player.subtractFromBalance(payBank.getValue(), gameLogic); //TODO test
                nextActionState = new EndTurnState();
                break;
            case GetCashFromBank:
                GetCashFromBankActionCard getCash = (GetCashFromBankActionCard) thisAction;//TODO test
                player.addToBalance(getCash.getAmountToPay());
                nextActionState = new EndTurnState();
                break;
        }
        
        return nextActionState;
	}

	private void performUpdateIfPassingOverTile(GameBoardTile currentTile, GameLogic gameLogic) {
		if (currentTile.getGameBoardTileType() == GameBoardTileTypes.Payday) {
			// Player should collect the salary indicated in their Career/College Career
			// card from the Bank
			OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
			
			if(currentOccupationCard != null) {
				int currentSalary = currentOccupationCard.getSalary();
				// Get money from the bank, increment the player's balance by that amount
				gameLogic.getCurrentPlayer().addToBalance(currentSalary);
			}
		}
	}

	private String handlePaydayTile(GameLogic gameLogic){
		String paydayUpdateString = "";
		
        OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
        
        if(currentOccupationCard != null) {
            int currentSalary = currentOccupationCard.getSalary();
            gameLogic.getCurrentPlayer().addToBalance(currentSalary + PAYDAY_LANDED_ON_BONUS);
            paydayUpdateString = "Player " + gameLogic.getCurrentPlayerIndex() + ", you obtained " + (currentSalary + PAYDAY_LANDED_ON_BONUS) + 
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
                System.out.println("Game Over"); //TODO remove
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
