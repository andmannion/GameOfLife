package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class GraduationState implements GameState {
	private ArrayList<OccupationCard> pendingCollegeCareerCardChoices;
	
	@Override
	public void enter(GameLogic gameLogic) {
		// Take the two top college career cards off the top the deck
		pendingCollegeCareerCardChoices = new ArrayList<>();
		
		// Give choice of top college cards			
		OccupationCard firstCollegeCareerCard = gameLogic.getTopCollegeCareerCard();
        OccupationCard secondCollegeCareerCard = gameLogic.getTopCollegeCareerCard();

        pendingCollegeCareerCardChoices.add(firstCollegeCareerCard);
        pendingCollegeCareerCardChoices.add(secondCollegeCareerCard);

        // Construct a message with these choices
        LifeGameMessage replyMessage = NightSchoolState.constructCardChoiceMessage( // TODO: find a better home for this reused logic
                gameLogic.getCurrentPlayer().getPlayerNumber(),
                (Chooseable) firstCollegeCareerCard,
                (Chooseable) secondCollegeCareerCard);

        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.setResponseMessage(replyMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			int choiceIndex = ((DecisionResponseMessage) lifeGameMessage).getChoiceIndex();
			
			OccupationCard chosenCollegeCareerCard = pendingCollegeCareerCardChoices.get(choiceIndex);			
			gameLogic.getCurrentPlayer().setOccupationCard(chosenCollegeCareerCard);
			
			OccupationCard unchosenCollegeCareerCard = pendingCollegeCareerCardChoices.get((choiceIndex + 1) % 1);			
			gameLogic.returnOccupationCard(unchosenCollegeCareerCard);
			
			String graduationStateEndMessage = "You chose the " + ((CollegeCareerCard) chosenCollegeCareerCard).getOccupationCardType() + " card.";
			
			return new EndTurnState(graduationStateEndMessage);
		}		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {}
}
