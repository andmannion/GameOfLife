package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.CareerPath;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class HouseChoiceState implements GameState {

	@Override
	public void enter(GameLogic gameLogic) {
        // Get the two top CareerCards
        HouseCard firstCardChoice = gameLogic.getTopHouseCard();
        HouseCard secondCardChoice = gameLogic.getTopHouseCard();

        // Create a list of the choices
        ArrayList<Card> pendingCardChoices = new ArrayList<>();
        pendingCardChoices.add(firstCardChoice);
        pendingCardChoices.add(secondCardChoice);

        // Construct a message with these choices
        LifeGameMessage replyMessage = constructCardChoiceMessage(
                gameLogic.getCurrentPlayer().getPlayerNumber(),
                (Chooseable) firstCardChoice,
                (Chooseable) secondCardChoice);

        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.storePendingChoiceCards(pendingCardChoices);
        gameLogic.setResponseMessage(replyMessage);

	}

    @Override
    @SuppressWarnings("Duplicates")
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
            DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

            // Need to assign the chosen card to the relevant player
            ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();
            HouseCard chosenCard = (HouseCard) pendingCardChoices.get(choiceIndex);
            gameLogic.getCurrentPlayer().addHouseCard(chosenCard);

            // Only two cards at the moment, return unchosen
            HouseCard unchosenCard = (HouseCard) pendingCardChoices.get((choiceIndex + 1) % 2);
            gameLogic.returnHouseCard(unchosenCard);


            gameLogic.setNextPlayerToCurrent(); //turn is now over for this player

            if (gameLogic.getNumberOfUninitialisedPlayers() > 0) { //if there are un init players
                // Must send a message to choose a career path, etc.
                System.out.println("Still player left to initialise");
                LifeGameMessage replyMessage = PathChoiceState.constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber());
                gameLogic.setResponseMessage(replyMessage);

                return new PathChoiceState();
            }
            else { //otherwise as normal
                gameLogic.setResponseMessage(new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()), gameLogic.getCurrentPlayer().getPlayerNumber()));
                return new HandlePlayerMoveState();
            }
        }

        return null;
    }

	@Override
	public void exit(GameLogic gameLogic) {
		// Must clear the sent message?
	}

	private LifeGameMessage constructCardChoiceMessage(int relatedPlayerIndex, Chooseable firstOptionCard,
			Chooseable secondOptionCard) {

		ArrayList<Chooseable> validStandardCareerCardOptions = new ArrayList<>();

		validStandardCareerCardOptions.add(firstOptionCard);
		validStandardCareerCardOptions.add(secondOptionCard);

		return new DecisionRequestMessage(validStandardCareerCardOptions, relatedPlayerIndex);
	}
}