package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HouseChoiceState implements GameState {
    private static final int LOAN_AMOUNT = 60000;
    private boolean loanRequired;
    private HouseCard chosenCard;

    HouseChoiceState(){
        this.loanRequired = false;
    }

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
                gameLogic.getCurrentPlayer().getPlayerNumber(), firstCardChoice, secondCardChoice);

        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.storePendingChoiceCards(pendingCardChoices);
        gameLogic.setResponseMessage(replyMessage);

	}

    @Override
    @SuppressWarnings("Duplicates")
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null; //TODO
        Player player = gameLogic.getCurrentPlayer();
        if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse && !loanRequired) {
            DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

            // Need to assign the chosen card to the relevant player
            ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();
            chosenCard = (HouseCard) pendingCardChoices.get(choiceIndex);

            HouseCard unchosenCard = (HouseCard) pendingCardChoices.get((choiceIndex + 1) % 2);
            gameLogic.returnHouseCard(unchosenCard);

            int housePrice = chosenCard.getPurchasePrice();

            nextState = purchaseHouse(player, housePrice, gameLogic, chosenCard);
        }
        else if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
            DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();
            if(choiceIndex == 0){ //do not take out loan
                gameLogic.returnHouseCard(chosenCard);
                nextState = new EndTurnState();
            }
            else{
                int housePrice = chosenCard.getPurchasePrice();

                int currentMoney = player.getCurrentMoney();
                while (currentMoney - housePrice < 0){ //user has to take out loans or else they go bankrupt
                    player.addToBalance(gameLogic.takeOutALoan(player.getPlayerNumber()));
                    currentMoney = player.getCurrentMoney();
                }
                nextState = purchaseHouse(player, housePrice, gameLogic, chosenCard);
            }
        }
        return nextState;
    }

	@Override
	public void exit(GameLogic gameLogic) {
		// Must clear the sent message?
	}

	@NotNull
    @Contract("_, _, _ -> new")
    private LifeGameMessage constructCardChoiceMessage(int relatedPlayerIndex, Chooseable firstOptionCard,
                                                       Chooseable secondOptionCard) {

		ArrayList<Chooseable> validStandardCareerCardOptions = new ArrayList<>();

		validStandardCareerCardOptions.add(firstOptionCard);
		validStandardCareerCardOptions.add(secondOptionCard);

		return new DecisionRequestMessage(validStandardCareerCardOptions, relatedPlayerIndex);
	}

	private GameState purchaseHouse(@NotNull Player player, int housePrice, GameLogic gameLogic, HouseCard chosenCard){
	    GameState nextState;
        if(player.getCurrentMoney() < housePrice){ //player cannot afford and is prompted for a loan
            ArrayList<String> decisionStrings = new ArrayList<>();
            decisionStrings.add("Don't buy.");
            double loanTotal = (double)(housePrice - player.getCurrentMoney());
            int numLoans = (int)Math.ceil(loanTotal/LOAN_AMOUNT);
            decisionStrings.add("Take out " + numLoans + " loan(s) worth " + numLoans*LOAN_AMOUNT + ".");
            LifeGameMessage responseMessage = new DecisionRequestMessage(ChooseableString.convertToChooseableArray(decisionStrings),
                    gameLogic.getCurrentPlayerIndex());

            gameLogic.setResponseMessage(responseMessage);
            loanRequired = true;
            nextState = null;
        }
        else {
            player.subtractFromBalance(housePrice, gameLogic);

            gameLogic.getCurrentPlayer().addHouseCard(chosenCard);

            nextState = new EndTurnState();
        }
        return nextState;
    }
}