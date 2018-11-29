package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HouseChoiceState extends GameState {
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

        // Construct a message with these choices
        LifeGameMessage replyMessage = setupChoiceAndMessage(gameLogic.getCurrentPlayer().getPlayerNumber(),
                firstCardChoice, secondCardChoice, "Choose a house to purchase", gameLogic.getCurrentShadowPlayer());

        gameLogic.setResponseMessage(replyMessage);

	}

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null; //TODO
        Player player = gameLogic.getCurrentPlayer();
        if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse && !loanRequired) {
            DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

            // Only two cards at the moment, return unchosen
            HouseCard unchosenHouseCard = (HouseCard) getPendingCardChoices().get((choiceIndex + 1) % 2);
            gameLogic.returnHouseCard(unchosenHouseCard);

            //store the unchosen as we need to see if it can be afforded
            chosenCard = (HouseCard) getPendingCardChoices().get(choiceIndex);


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

	private GameState purchaseHouse(@NotNull Player player, int housePrice, GameLogic gameLogic, HouseCard chosenCard){
	    GameState nextState;
        if(player.getCurrentMoney() < housePrice){ //player cannot afford and is prompted for a loan
            ArrayList<String> decisionStrings = new ArrayList<>();
            decisionStrings.add("Don't buy.");

            double loanTotal = (double)(housePrice - player.getCurrentMoney());
            int numLoans = (int)Math.ceil(loanTotal/GameConfig.loan_amount);
            decisionStrings.add("Take out " + numLoans + " loan(s) worth " + numLoans*GameConfig.loan_amount + ".");
            String eventMessage = "You cannot afford this house right now.";

            LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
            ShadowPlayer shadowPlayer = gameLogic.getCurrentShadowPlayer();
            LifeGameMessage responseMessage = new DecisionRequestMessage(ChooseableString.convertToChooseableArray(decisionStrings),
                    gameLogic.getCurrentPlayerIndex(), eventMessage, requestType, shadowPlayer);

            gameLogic.setResponseMessage(responseMessage);
            loanRequired = true;
            nextState = null;
        }
        else {
            gameLogic.subtractFromCurrentPlayersBalance(housePrice);

            gameLogic.getCurrentPlayer().addHouseCard(chosenCard);

            nextState = new EndTurnState();
        }
        return nextState;
    }
}