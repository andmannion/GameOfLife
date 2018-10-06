package test.TestLifeGameLogic.TestCards.TestActionCards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCard;
import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCardDeck;


class AssertCorrectPopTopCardAndInsertCardAtBottom {

	@Test
	void assertDeckCreated() {
		
		ActionCardDeck actionCardDeck = new ActionCardDeck();
		
		assertEquals(55, actionCardDeck.getRemainingCards());	
		
		ActionCard actionCard = (ActionCard) actionCardDeck.popTopCard();
		
		assertEquals(54, actionCardDeck.getRemainingCards());
		
		actionCardDeck.addCardToBottom(actionCard);
		
		assertEquals(55, actionCardDeck.getRemainingCards());
	}

}
