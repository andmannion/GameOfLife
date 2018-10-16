package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;

public class GameLogicInterface implements MessageReceiverAndResponder<LifeGameMessage> {
	private GameLogic gameLogic;
	
	public GameLogicInterface(LogicGameBoard gameBoard, int numPlayers) {
		System.out.println("Inside GameLogicInterface constructor");
		gameLogic = new GameLogic(gameBoard, numPlayers);
	}

	@Override
	public LifeGameMessage receiveMessage(LifeGameMessage lifeGameMessage) {
		LifeGameMessage gameLogicResponse = gameLogic.handleInput(lifeGameMessage);
		
		return gameLogicResponse;
	}
	
	
	   /* What types of user input could I be awaiting?
		* - Initial path selection choice
		* - Holiday Space:
		* 		- Request a new spin for the next turn
		* - Action Space:
		* 		- CareerChangeDecision - Choice of one career card from two
		* 		- PlayersPayDecision - Choice of one player from all players excluding the current player
		* - Spin to Win Space:
		* 		- Current player chooses two different numbers between 1 and 10
		* 		  <Other players choose a DIFFERENT number between 1 and 10, get these n numbers, spin once>
		* 		  Tell UI if the correct number has been spun, if yes, perform action on users,
		* 		  Otherwise request another spinEvent, keep doing this until the number has been matched, then send update message.
		* - House Space:
		* 		- Must choose to buy or sell a house
		* 			- Buy a house: 
		* 				Need to get a choice of the favourite of the two top cards in the HouseCard deck.
		* 				Check that the player has enough money, if not, notify of confirmation to proceed
		* 				notifying them of the amount of loans needed to purchase that house.
		* 				Add the favourite to the player, deducting price, adding loans as necessary
		* 				Add the unchosen housecard to the bottom of the deck.
		* 			- Sell a house
		* 				Request spin event
		* 				Even number - get black price, odd number - get red price
		* 				Return house card to the deck
		* - Stop Spaces:
		* 		- 	  
		*/ 		 
}
