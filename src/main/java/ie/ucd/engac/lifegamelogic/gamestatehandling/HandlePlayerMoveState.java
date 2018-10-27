package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

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
	GameState currentSubstate = null; //TODO substates?
	
	@Override
	public void enter(GameLogic gameLogic) {
        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        String eventMessage = "Player " + playNum + "'s turn.";
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
            tilesToMove = 11;//Spinner.spinTheWheel();

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

	private GameBoardTile tryToMove(GameLogic gameLogic, LogicGameBoard gameBoard, int tilesToMove, int tilesMoved){
        boolean stopTileEncountered = false;
        GameBoardTile currentTile = null;
        while (tilesMoved < tilesToMove && !stopTileEncountered) {
            // Go forward
            BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
            ArrayList<BoardLocation> adjacentForwardLocations = gameBoard.getOutboundNeighbours(currentBoardLocation);

            // For the moment, no tiles other than stop tiles have branches
            if (1 == adjacentForwardLocations.size()) {
                BoardLocation currentLocation = adjacentForwardLocations.get(0);
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

                System.out.println("No spaces remaining ahead");
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
               paydayTile(gameLogic);
               nextState = new EndTurnState(); //turn is now over for this player
                break;
            case Action: //TODO move to own function
                ActionCard thisAction = gameLogic.getTopActionCard();
                Player player = gameLogic.getCurrentPlayer();
                switch (thisAction.getActionCardType()){
                    case CareerChange:
                        new CareerChangeState(); //TODO test
                        break;
                    case PlayersPay:
                        return new PickPlayerState((PlayersPayActionCard) thisAction); ///TODO test
                    case PayTheBank:
                        PayTheBankActionCard payBank = (PayTheBankActionCard) thisAction;
                        player.subtractFromBalance(payBank.getValue()); //TODO test
                        return new EndTurnState();
                    case GetCashFromBank:
                        GetCashFromBankActionCard getCash = (GetCashFromBankActionCard) thisAction;//TODO test
                        player.addToBalance(getCash.getAmountToPay());
                        return new EndTurnState();
                }
                break;
            case Holiday:
            	System.out.println("Holiday tile"); //TODO remove
                //TODO notify user that it is a holiday
                nextState = new EndTurnState();
                break;
            case SpinToWin:
                System.out.println("Spin to win state");
                nextState = new SpinToWinSetupState(); //TODO Check that spin2win restores control to the correct player
                break;
            case Baby:
            	System.out.println("Baby tile"); //TODO remove
                //TODO notify user that it is a baby
                gameLogic.getCurrentPlayer().addDependants(1);
                nextState = new EndTurnState();
                break;
            case House:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            case Stop:
            	System.out.println("Stop tile"); //TODO remove
                nextState = evaluateStopTile(gameLogic, (GameBoardStopTile) currentTile);
                break;
            default:
                // There's no console to print to...
                // TODO: use some utility log file class to write the errors of the program to
                break;
        }
        return nextState;
    }
	
	private GameState evaluateStopTile(GameLogic gameLogic, GameBoardStopTile currentTile) { //TODO potentially merge into normal tile eval
        GameState nextState = null;
        switch(currentTile.getGameBoardStopTileType()) {
                case Graduation:
                    //return new NightSchoolState();
                    nextState = new EndTurnState();
                    break;
                case GetMarried:
                    //nextState = new GetMarriedState();
                    nextState = new EndTurnState();
                    break;
                case NightSchool:
                    //return new NightSchoolState();
                    nextState = new EndTurnState();
                    break;
                case Family:
                    //return new NightSchoolState();
                    nextState = new EndTurnState();
                    break;
                case Baby:
                    //return new NightSchoolState();
                    nextState = new EndTurnState();
                    break;
                case Holiday:
                    //return new NightSchoolState();
                    nextState = new EndTurnState();
                    break;
                case Retire:
                    System.out.println("Retirement tile"); //TODO remove
                    if (gameLogic.getCurrentPlayer().getNumberOfHouseCards() == 0){ //if they have houses need to sell them
                        gameLogic.retireCurrentPlayer();
                        nextState = new EndTurnState();
                    }
                    else {
                        nextState = new RetirePlayerState(); //TODO test
                    }
                    break;
                default:
                    // Should be some error message logged to a log file here, quit altogether?
                    break;
            }
            return nextState;
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

	private void paydayTile(GameLogic gameLogic){
        OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
        if(currentOccupationCard != null) {
            int currentSalary = currentOccupationCard.getSalary();
            gameLogic.getCurrentPlayer().addToBalance(currentSalary + PAYDAY_LANDED_ON_BONUS);
        }
    }

    private void retirePlayer(){

    }
}
