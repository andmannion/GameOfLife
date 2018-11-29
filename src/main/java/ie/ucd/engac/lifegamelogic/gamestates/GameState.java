package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.gameboard.CareerPath;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public abstract class GameState {

    private ArrayList<Chooseable> pendingCardChoices;

	public abstract void enter(GameLogic gameLogic);

	public abstract GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);

    protected void storePendingChoiceCards(ArrayList<Chooseable> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    protected ArrayList<Chooseable> getPendingCardChoices(){
        return pendingCardChoices;
    }

	protected LifeGameMessage setupChoiceAndMessage(int relatedPlayerIndex, Chooseable firstOption,
													Chooseable secondOption, String eventMessage,
													ShadowPlayer shadowPlayer) {
        // Need to store both choices so that we can assign the chosen one to the
        // correct player, and push the unchosen one to the bottom of the correct deck.

		ArrayList<Chooseable> validOptions = new ArrayList<>();

        validOptions.add(firstOption);
        validOptions.add(secondOption);

        storePendingChoiceCards(validOptions);

		LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
		return new DecisionRequestMessage(validOptions, relatedPlayerIndex, eventMessage, requestType, shadowPlayer);
	}

	protected void actOnOccupationCardChoice(GameLogic gameLogic, int choiceIndex){
		// Need to assign the chosen card to the relevant player
		OccupationCard chosenCareerCard = (OccupationCard) getPendingCardChoices().get(choiceIndex);
		gameLogic.getCurrentPlayer().setOccupationCard(chosenCareerCard);

		// Only two cards at the moment, return unchosen
		OccupationCard unchosenCareerCard = (OccupationCard) getPendingCardChoices().get((choiceIndex + 1) % 2);
		gameLogic.returnOccupationCard(unchosenCareerCard);
	}

	static LifeGameMessage constructPathChoiceMessage(int relatedPlayerNumber, ShadowPlayer shadowPlayer) {
		ArrayList<Chooseable> validPathChoices = new ArrayList<>();
		validPathChoices.add(new CareerPath(OccupationCardTypes.Career));
		validPathChoices.add(new CareerPath(OccupationCardTypes.CollegeCareer));

		String eventMessage = "Choose either a college or standard career path.";

		LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
		return new DecisionRequestMessage(validPathChoices, relatedPlayerNumber, eventMessage, requestType, shadowPlayer);
	}
}
