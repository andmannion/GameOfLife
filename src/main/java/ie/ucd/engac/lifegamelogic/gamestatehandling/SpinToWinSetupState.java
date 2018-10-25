package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.ChooseableString;
import ie.ucd.engac.messaging.LargeDecisionRequestMessage;
import ie.ucd.engac.messaging.LargeDecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class SpinToWinSetupState implements GameState {

	private HashSet<Integer> remainingNumberChoices;
	private HashMap<Integer, ArrayList<Integer>> playerNumberChoiceMap;
	private ArrayList<Chooseable> outgoingChoices;
	private int initialPlayerNumber;
	private int awaitingInfoFromPlayerNumber;
	private boolean sendSecondFromInitialPlayer = true;

	@Override
	public void enter(GameLogic gameLogic) {
		// Must set the response message to a choice between 1 and 10
		remainingNumberChoices = new HashSet<>();
		playerNumberChoiceMap = new HashMap<>();
		ArrayList<Chooseable> choices = new ArrayList<>();
		
		awaitingInfoFromPlayerNumber = gameLogic.getCurrentPlayer().getPlayerNumber();
		initialPlayerNumber = awaitingInfoFromPlayerNumber;

		for (int i = 1; i <= Spinner.numPossibleValues; i++) {
			remainingNumberChoices.add(i);
		}
		
		outgoingChoices = getRemainingChooseableNumberChoices();

		// First message with current player's number
		LifeGameMessage replyMessage = new LargeDecisionRequestMessage(outgoingChoices,
																	   awaitingInfoFromPlayerNumber);
		gameLogic.setResponseMessage(replyMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		// TODO Auto-generated method stub
		// Get two numbers back, store them in the player's spinToWinSelection
		// Send out a LargeDecisionRequestMessage for each other player
		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
			// Check who this reply relates to
			System.out.println("Taking number from player " + awaitingInfoFromPlayerNumber);
			parsePlayerResponse(gameLogic,
				    			(LargeDecisionResponseMessage) lifeGameMessage,
				    			awaitingInfoFromPlayerNumber);
			
			int nextPlayer = gameLogic.getNextPlayerIndex(awaitingInfoFromPlayerNumber);
			
			if (!sendSecondFromInitialPlayer) {
				awaitingInfoFromPlayerNumber = gameLogic.getNextPlayerIndex(awaitingInfoFromPlayerNumber);				
			} 
			else {				
				// Don't increment the awaitingInforFromPlayerNumber				
				sendSecondFromInitialPlayer = false;
			}
			
			if (nextPlayer == initialPlayerNumber) {
				// We have received a message from all players, and now need to begin spinning
				// the wheel to select numbers for each player until one of the chosen numbers is spun.
				String eventMsg = "Spin the wheel to try to win.";
				LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()), initialPlayerNumber, eventMsg); 
				
				gameLogic.setResponseMessage(responseMessage);
				return new SpinToWinGetWinnerState(playerNumberChoiceMap);
			}
			else {
				// Set normal response message
				outgoingChoices = getRemainingChooseableNumberChoices();

				// First message with current player's number
				LifeGameMessage replyMessage = new LargeDecisionRequestMessage(outgoingChoices,
																			   awaitingInfoFromPlayerNumber);
				gameLogic.setResponseMessage(replyMessage);
			}
		}

		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub

	}

	private void parsePlayerResponse(GameLogic gameLogic, LargeDecisionResponseMessage lifeGameMessage, int relatedPlayerNumber) {
		// Must set what the player has chosen, and remove what they have chosen from the allowable remaining
		// choices
		int selectedNumber = Integer.parseInt(outgoingChoices.get(lifeGameMessage.getChoiceIndex()).displayChoiceDetails());
		for (Integer chooseableNumber : remainingNumberChoices) {
			System.out.println(chooseableNumber);
		}

		if(!playerNumberChoiceMap.containsKey(relatedPlayerNumber)) {
			playerNumberChoiceMap.put(relatedPlayerNumber, new ArrayList<Integer>());
			playerNumberChoiceMap.get(relatedPlayerNumber).add(selectedNumber);
		}
		else {
			playerNumberChoiceMap.get(relatedPlayerNumber).add(selectedNumber);
		}
		
		// Remove the selected number from the set of allowable numbers
		remainingNumberChoices.remove(selectedNumber);
		
		for (Integer chooseableNumber : remainingNumberChoices) {
			System.out.println(chooseableNumber);
		}
	}

	private ArrayList<Chooseable> getRemainingChooseableNumberChoices() {
		ArrayList<Chooseable> chooseableNumbers = new ArrayList<>();

		for (Integer chooseableNumber : remainingNumberChoices) {
			chooseableNumbers.add(new ChooseableString(Integer.toString(chooseableNumber)));
		}

		return chooseableNumbers;
	}
}
