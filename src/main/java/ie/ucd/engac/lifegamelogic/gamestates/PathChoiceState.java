package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class PathChoiceState extends GameState {
	public static final int STANDARD_CAREER_CHOICE_INDEX = 0;
	public static final int COLLEGE_CAREER_CHOICE_INDEX = 1;

	@Override
	public void enter(GameLogic gameLogic) {}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		/*
		 * Must reply with an OptionDecisionRequest for a path type to the
		 * InitialMessage, and continue to respond to any further messages with that
		 * same message until we get the desired response type.
		 */
		if (lifeGameMessage.getLifeGameMessageType() != LifeGameMessageTypes.OptionDecisionResponse) {
			LifeGameMessage replyMessage = constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber(), gameLogic.getCurrentShadowPlayer());
			gameLogic.setResponseMessage(replyMessage);
		} 
		else {
			// Have a path choice to resolve
			OccupationCardTypes pathChoiceResponse = parsePathChoiceResponse((DecisionResponseMessage) lifeGameMessage);

			// Must set the path choice for the current player based on what was returned
			if (pathChoiceResponse == OccupationCardTypes.CollegeCareer) {
				gameLogic.getCurrentPlayer().setCareerPath(CareerPathTypes.CollegeCareer);
				gameLogic.subtractFromCurrentPlayersBalance(GameConfig.college_upfront_cost);
						
				// Must move the player to the CollegeCareer path
				gameLogic.movePlayerToInitialCollegeCareerPath(gameLogic.getCurrentPlayerIndex());

				gameLogic.decrementNumberOfUninitialisedPlayers();
				
				return new HandlePlayerMoveState();
			} 
			else {
				return new ProcessStandardCareerState();
			}
		}
		return null;
	}

	private OccupationCardTypes parsePathChoiceResponse(DecisionResponseMessage pathChoiceMessage) {
		int choiceIndex = pathChoiceMessage.getChoiceIndex();

		if (0 == choiceIndex) {
			return OccupationCardTypes.Career;
		}
		if (1 == choiceIndex) {
			return OccupationCardTypes.CollegeCareer;
		}
		return null;
	}
}
