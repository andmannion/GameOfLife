package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SpinToWinSetupState extends GameState {

	private HashSet<Integer> remainingNumberChoices;
	private HashMap<Integer, ArrayList<Integer>> playerIndexChoiceMap;
	private ArrayList<Chooseable> outgoingChoices;
	private int initialPlayerIndex;
    private int awaitingInfoFromPlayerIndex;
	private boolean sendSecondFromInitialPlayer = true;

	@Override
	public void enter(GameLogic gameLogic) {
		// Must set the response message to a choice between 1 and 10
		remainingNumberChoices = new HashSet<>();
		playerIndexChoiceMap = new HashMap<>();
		
		awaitingInfoFromPlayerIndex = gameLogic.getCurrentPlayerIndex();
        initialPlayerIndex = awaitingInfoFromPlayerIndex;

		for (int i = 1; i <= Spinner.numPossibleValues; i++) {
			remainingNumberChoices.add(i);
		}
		
		outgoingChoices = getRemainingChooseableNumberChoices();

		// First message with current player's number
		int playerNumber = gameLogic.getPlayerByIndex(awaitingInfoFromPlayerIndex).getPlayerNumber();

		LifeGameMessageTypes requestType = LifeGameMessageTypes.LargeDecisionRequest;
		LifeGameMessage replyMessage = new DecisionRequestMessage(outgoingChoices,
                playerNumber, "Player "+playerNumber+", pick a SpinToWin number.", requestType);
		gameLogic.setResponseMessage(replyMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		// Get two numbers back, store them in the player's spinToWinSelection
		// Send out a LargeDecisionRequestMessage for each other player
		if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
			// Check who this reply relates to
			parsePlayerResponse(gameLogic,
				    			(DecisionResponseMessage) lifeGameMessage,
                    awaitingInfoFromPlayerIndex);
                    //gameLogic.getPlayerByIndex(awaitingInfoFromPlayerIndex).getPlayerNumber());
			
			int nextPlayer = gameLogic.getNextPlayerIndex(awaitingInfoFromPlayerIndex);
			
			if (!sendSecondFromInitialPlayer) {
                awaitingInfoFromPlayerIndex = gameLogic.getNextPlayerIndex(awaitingInfoFromPlayerIndex);
			} 
			else {				
				// Don't increment the awaitingInfoFromPlayerIndex
				sendSecondFromInitialPlayer = false;
			}
			
			if (nextPlayer == initialPlayerIndex) {
				// We have received a message from all players, and now need to begin spinning
				// the wheel to select numbers for each player until one of the chosen numbers is spun.
                int playerNumber = gameLogic.getPlayerByIndex(initialPlayerIndex).getPlayerNumber();
				String eventMsg = " Player " + playerNumber + ", spin the wheel to try to win.";
				LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),
						gameLogic.getPlayerByIndex(initialPlayerIndex).getPlayerNumber(), eventMsg);
				
				gameLogic.setResponseMessage(responseMessage);
				return new SpinToWinGetWinnerState(playerIndexChoiceMap);
			}
			else {
				// Set normal response message
				outgoingChoices = getRemainingChooseableNumberChoices();

				// First message with current player's number
                int playerNumber = gameLogic.getPlayerByIndex(awaitingInfoFromPlayerIndex).getPlayerNumber();

                LifeGameMessageTypes requestType = LifeGameMessageTypes.LargeDecisionRequest;
				LifeGameMessage replyMessage = new DecisionRequestMessage(outgoingChoices,
                        gameLogic.getPlayerByIndex(initialPlayerIndex).getPlayerNumber(),
                        "Player " + playerNumber + ", pick a SpinToWin number.", requestType);
				gameLogic.setResponseMessage(replyMessage);
			}
		}

		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub

	}

	private void parsePlayerResponse(GameLogic gameLogic, DecisionResponseMessage lifeGameMessage, int relatedPlayerIndex) {
		// Must set what the player has chosen, and remove what they have chosen from the allowable remaining
		// choices
		int selectedNumber = Integer.parseInt(outgoingChoices.get(lifeGameMessage.getChoiceIndex()).displayChoiceDetails());

		if(!playerIndexChoiceMap.containsKey(relatedPlayerIndex)) {
			playerIndexChoiceMap.put(relatedPlayerIndex, new ArrayList<Integer>());
			playerIndexChoiceMap.get(relatedPlayerIndex).add(selectedNumber);
		}
		else {
			playerIndexChoiceMap.get(relatedPlayerIndex).add(selectedNumber);
		}
		
		// Remove the selected number from the set of allowable numbers
		remainingNumberChoices.remove(selectedNumber);
	}

	private ArrayList<Chooseable> getRemainingChooseableNumberChoices() {
		ArrayList<Chooseable> chooseableNumbers = new ArrayList<>();

		for (Integer chooseableNumber : remainingNumberChoices) {
			chooseableNumbers.add(new ChooseableString(Integer.toString(chooseableNumber)));
		}

		return chooseableNumbers;
	}
}
