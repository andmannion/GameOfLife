package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class GraduationState extends GameState {
	
	@Override
	public void enter(GameLogic gameLogic) {
		// player has no card at this stage, so no reason to return the old one
		// Take the two top college career cards off the top the deck
        ArrayList<Card> pendingCardChoices = new ArrayList<>();
		
		// Give choice of top college cards			
		OccupationCard firstCollegeCareerCard = gameLogic.getTopCollegeCareerCard();
        OccupationCard secondCollegeCareerCard = gameLogic.getTopCollegeCareerCard();

        pendingCardChoices.add(firstCollegeCareerCard);
        pendingCardChoices.add(secondCollegeCareerCard);

    // Construct a message with these choices
    LifeGameMessage replyMessage = constructCardChoiceMessage(
            gameLogic.getCurrentPlayer().getPlayerNumber(),
            (Chooseable) firstCollegeCareerCard,
            (Chooseable) secondCollegeCareerCard,
            "Choose new college career card");

        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.setResponseMessage(replyMessage);
        gameLogic.storePendingChoiceCards(pendingCardChoices);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
		    int choiceIndex = ((DecisionResponseMessage) lifeGameMessage).getChoiceIndex();

            actOnOccupationCardChoice(gameLogic, choiceIndex);

			String graduationStateEndMessage = "You chose the " + gameLogic.getCurrentPlayer().getOccupationCard().getOccupationCardType() + " card."; //TODO many chained methods
			
			return new EndTurnState(graduationStateEndMessage);
		}		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {}
}
