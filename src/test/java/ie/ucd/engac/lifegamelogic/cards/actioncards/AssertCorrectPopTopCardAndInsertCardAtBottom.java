package ie.ucd.engac.lifegamelogic.cards.actioncards;

import static org.junit.jupiter.api.Assertions.*;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.Test;

import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;


class AssertCorrectPopTopCardAndInsertCardAtBottom {

	@Test
	void assertDeckCreated() {
		TestHelpers.importGameConfig();
		
		CardDeck<ActionCard> actionCardDeck = new CardDeck<ActionCard>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));
		
		assertEquals(55, actionCardDeck.getNumberOfRemainingCards());
		
		ActionCard actionCard = actionCardDeck.popTopCard();
		
		assertEquals(54, actionCardDeck.getNumberOfRemainingCards());
		
		actionCardDeck.addCardToBottom(actionCard);
		
		assertEquals(55, actionCardDeck.getNumberOfRemainingCards());
	}

}
