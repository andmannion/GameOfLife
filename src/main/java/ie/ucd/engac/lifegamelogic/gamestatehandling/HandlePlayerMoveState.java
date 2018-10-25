package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class HandlePlayerMoveState implements GameState {
	private final int PAYDAY_LANDED_ON_BONUS = 100000;
	GameState currentSubstate = null;
	
	@Override
	public void enter(GameLogic gameLogic) {
        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        String eventMessage = "Player " + playNum + "'s turn.";
        SpinRequestMessage spinRequestMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),playNum, eventMessage);
        gameLogic.setResponseMessage(spinRequestMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null;

		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) { //TODO redo entry cond. if substates being used
			// TODO: when should the fork choice after a stop tile has been
			// encountered be handled - at the beginning or the end of a turn?
			// I would say the beginning... TODO this should be done when you land on it - thats the natural flow as the user

			LogicGameBoard gameBoard = gameLogic.getGameBoard();
			int tilesToMove;
            int tilesMoved = 0;
            GameBoardTile endTile;

            // Need to spin the spinner
            tilesToMove = Spinner.spinTheWheel();
            System.out.println("The number spun by player " + gameLogic.getCurrentPlayer().getPlayerNumber() + " was " + tilesToMove); //TODO remove

			// Need to alternate between moving and evaluating the tile we're on
            endTile = tryToMove(gameLogic, gameBoard, tilesToMove, tilesMoved);

			// At this point, we have landed on a tile, either through the number of goes running out, or by encountering a stop tile.
            nextState = evaluateTile(gameLogic,endTile);
            System.out.println("Finished evaluating tile, nextState is: "+ nextState); //TODO remove

		}
		else { //TODO finish this when doing substates again, or remove
		    System.out.println("accessed else cond in if loop xd"); //TODO remove
            //GameState nextSubstate = currentSubstate.handleInput(gameLogic,lifeGameMessage);
        }

        /* //TODO uncommment when doing substates again, or remove
        if(currentSubstate == null){
            if(gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                // Must send a message to choose a career path, etc.
                System.out.println("Still player left to initialise");
                LifeGameMessage replyMessage = PathChoiceState.constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber());
                gameLogic.setResponseMessage(replyMessage);

                return new PathChoiceState();
            }
            else //TO DO write this case
                System.out.println("What should this do, HandleMoveState@HandleInput return selection"); //TODO remove
        }
        else{
            return null; //exit returns null to stay here or something else to change states.
        }
        */

        return nextState;
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
	    GameState nextState = null; //TODO -> next SUB state
        System.out.println(currentTile.getGameBoardTileType());
        switch (currentTile.getGameBoardTileType()) {
            case Start:
                break;
            case Payday:
                OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
                if(currentOccupationCard != null) {
                    int currentSalary = currentOccupationCard.getSalary();
                    gameLogic.extractMoneyFromBank(currentSalary + PAYDAY_LANDED_ON_BONUS);
                    gameLogic.getCurrentPlayer().addToBalance(currentSalary + PAYDAY_LANDED_ON_BONUS);
                }
                nextState = new EndTurnState(); //turn is now over for this player
                break;
            case Action: //TODO using this as test, fix
                ActionCard thisAction = gameLogic.getTopActionCard();
                nextState = new HouseTileDecisionState();
                break;
            case Holiday:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                // Do nothing
                break;
            case SpinToWin:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            case Baby:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            case House:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            case Stop:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            case Retire:
                System.out.println("House state"); //TODO remove
                nextState = new HouseTileDecisionState();
                break;
            default:
                // There's no console to print to...
                // TODO: use some utility log file class to write the errors of the program to
                break;
        }
        /* //TODO uncommment when doing substates again, or remove
        if(nextSubstate != null) {
            currentSubstate = nextSubstate;
            currentSubstate.enter(gameLogic);
        }
        */
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
				gameLogic.extractMoneyFromBank(currentSalary);
				gameLogic.getCurrentPlayer().addToBalance(currentSalary);
			}
		}
	}
}
