package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.*;

public class CareerChangeState extends GameState {

    @Override
    public void enter(GameLogic gameLogic) {
        // Must send a message to transition to processStandardCareer
        CareerPathTypes careerPathType = gameLogic.getCurrentPlayer().getCareerPath();

        //must return the old career card to the bottom of the deck
        OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
        if (currentOccupationCard != null){ //shouldnt be null, but just in case
            gameLogic.returnOccupationCard(currentOccupationCard);
        }

        OccupationCard firstCareerCardChoice;
        OccupationCard secondCareerCardChoice;

        switch (careerPathType) {
            case CollegeCareer:
                firstCareerCardChoice = gameLogic.getTopCollegeCareerCard();
                secondCareerCardChoice = gameLogic.getTopCollegeCareerCard();
                break;
            case StandardCareer:
                firstCareerCardChoice = gameLogic.getTopStandardCareerCard();
                secondCareerCardChoice = gameLogic.getTopStandardCareerCard();
                break;
            default:
                firstCareerCardChoice = null;
                secondCareerCardChoice = null;
        }

        // Set the response message to "CardChoice"
        // Get the two top CareerCards
        ArrayList<Card> pendingCardChoices = new ArrayList<>();
        pendingCardChoices.add(firstCareerCardChoice);
        pendingCardChoices.add(secondCareerCardChoice);

        LifeGameMessage replyMessage = constructChoiceMessage(
                gameLogic.getCurrentPlayer().getPlayerNumber(),
                (Chooseable) firstCareerCardChoice,
                (Chooseable) secondCareerCardChoice,
                "Choose career card.");

        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.storePendingChoiceCards(pendingCardChoices);
        gameLogic.setResponseMessage(replyMessage);

    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {//TODO lots of duplicated code
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
            DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

            //call static method in superclass to set/return card
            actOnOccupationCardChoice(gameLogic, choiceIndex);

            return new EndTurnState();
        }

        return null;
    }



    @Override
    public void exit(GameLogic gameLogic) {
        // TODO Auto-generated method stub
    }
}
