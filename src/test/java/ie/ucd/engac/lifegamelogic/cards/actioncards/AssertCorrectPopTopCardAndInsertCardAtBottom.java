package ie.ucd.engac.lifegamelogic.cards.actioncards;

import static org.junit.jupiter.api.Assertions.*;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.Test;

import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCardDeck;


class AssertCorrectPopTopCardAndInsertCardAtBottom {

	@Test
	void assertDeckCreated() {
		TestHelpers.importGameConfig();
		
		ActionCardDeck actionCardDeck = new ActionCardDeck(GameConfig.action_card_deck_config_file_location);
		
		assertEquals(55, actionCardDeck.getNumberOfRemainingCards());
		
		ActionCard actionCard = (ActionCard) actionCardDeck.popTopCard();
		
		assertEquals(54, actionCardDeck.getNumberOfRemainingCards());
		
		actionCardDeck.addCardToBottom(actionCard);
		
		assertEquals(55, actionCardDeck.getNumberOfRemainingCards());
	}

}
