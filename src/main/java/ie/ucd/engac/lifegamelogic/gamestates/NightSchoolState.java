package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class NightSchoolState extends GameState {
    public static final int KEEP_CAREER_INDEX = 0;

    public static final int ATTEND_NIGHT_SCHOOL_INDEX = 1;
	
	@Override
	public void enter(GameLogic gameLogic) {
		// Must prompt the user to choose between keeping the job,
		// or changing career.
		
		// Can't change career if don't already have one...
		if(gameLogic.getCurrentPlayer().getOccupationCard() == null) {
            throw new RuntimeException("Invalid board configuration, night school before graduation"); //TODO test
		}
		else
		{
            ArrayList<Chooseable> pendingNightSchoolChoice = new ArrayList<>();
			
			// Must give the player the choice to either keep their current job or go to night school			
            String keepCareerMsg = "Keep your current career";
            pendingNightSchoolChoice.add(new ChooseableString(keepCareerMsg));

            String attendNightSchoolMsg = "Attend night school";
            pendingNightSchoolChoice.add(new ChooseableString(attendNightSchoolMsg));

			String eventMessage = "Would you like to attend night school?";

			LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
			LifeGameMessage responseMessage = new DecisionRequestMessage(pendingNightSchoolChoice, gameLogic.getCurrentPlayer().getPlayerNumber(),
					                                                    eventMessage, requestType, gameLogic.getCurrentShadowPlayer());
			gameLogic.setResponseMessage(responseMessage);
		}
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			    return parsePendingNightSchoolDecision(gameLogic, ((DecisionResponseMessage) lifeGameMessage).getChoiceIndex());
		}
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		
	}
	
	private GameState parsePendingNightSchoolDecision(GameLogic gameLogic, int choiceIndex) {
		if(choiceIndex == ATTEND_NIGHT_SCHOOL_INDEX) {
			// Player wishes to attend night school:			
			gameLogic.subtractFromCurrentPlayersBalance(GameConfig.night_school_tuition_fees);

			OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
			if (currentOccupationCard != null){ //shouldnt be null, but just in case
				gameLogic.returnOccupationCard(currentOccupationCard);
			}

			OccupationCard topCollegeCareerCard = gameLogic.getTopCollegeCareerCard();	
			
			Player currentPlayer = gameLogic.getCurrentPlayer();			
			currentPlayer.setOccupationCard(topCollegeCareerCard);
			
			BoardLocation currentLocation = currentPlayer.getCurrentLocation();
			BoardLocation nightSchoolPathChoiceLocation = gameLogic.getGameBoard().getOutboundNeighbours(currentLocation).get(ATTEND_NIGHT_SCHOOL_INDEX);
			
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(nightSchoolPathChoiceLocation);
		}
		else {
			Player currentPlayer = gameLogic.getCurrentPlayer();	
			BoardLocation currentLocation = currentPlayer.getCurrentLocation();
			BoardLocation lifePathChoiceLocation = gameLogic.getGameBoard().getOutboundNeighbours(currentLocation).get(KEEP_CAREER_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(lifePathChoiceLocation);
		}
		
		// Send same message either way
		String eventMsg = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you get to spin again.";
				
		return new HandlePlayerMoveState(eventMsg);
	}

}