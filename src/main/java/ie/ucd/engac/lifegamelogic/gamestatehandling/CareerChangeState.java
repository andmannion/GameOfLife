package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.*;

public class CareerChangeState implements GameState {
    CareerPathTypes careerPathType;

    @Override
    public void enter(GameLogic gameLogic) {
        // Must send a message to transition to processStandardCareer
        careerPathType = gameLogic.getCurrentPlayer().getCareerPath();
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

        LifeGameMessage replyMessage = constructStandardCareerCardChoiceMessage(
                gameLogic.getCurrentPlayer().getPlayerNumber(),
                (Chooseable) firstCareerCardChoice,
                (Chooseable) secondCareerCardChoice);

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

            //TODO from here
            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

            // Need to assign the chosen card to the relevant player
            ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();
            CareerCard chosenCareerCard = (CareerCard) pendingCardChoices.get(choiceIndex);
            gameLogic.getCurrentPlayer().setOccupationCard(chosenCareerCard);

            // Only two cards at the moment, return unchosen
            CareerCard unchosenCareerCard = (CareerCard) pendingCardChoices.get((choiceIndex + 1) % 2);
            //TODO into function in superclass?
            gameLogic.returnCareerCard(unchosenCareerCard, careerPathType);

            return new EndTurnState();
        }

        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // TODO Auto-generated method stub

    }

    private LifeGameMessage constructStandardCareerCardChoiceMessage(int relatedPlayerIndex, Chooseable firstOptionCard,
                                                                     Chooseable secondOptionCard) {

        ArrayList<Chooseable> validStandardCareerCardOptions = new ArrayList<>();

        validStandardCareerCardOptions.add(firstOptionCard);
        validStandardCareerCardOptions.add(secondOptionCard);

        return new DecisionRequestMessage(validStandardCareerCardOptions, relatedPlayerIndex);
    }
}
