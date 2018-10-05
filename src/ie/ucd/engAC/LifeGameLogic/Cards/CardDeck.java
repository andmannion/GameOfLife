package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.LinkedList;

public abstract class CardDeck {
	// Want to be able to: take from the top, and add to the bottom of the deck

	protected LinkedList<Card> cards;

	public CardDeck() {
		cards = new LinkedList<Card>();
	}

	public Card PopTopCard() {
		return cards.pop();
	}

	public void AddCardToBottom(Card card) {
		cards.addLast(card);
	}

	@SuppressWarnings("unused")
	private void Shuffle() {
		for (int i = 0; i < 52; i++) {
			int card = (int) (Math.random() * (52 - i));
			cards.addLast(cards.remove(card));
		}
	}

	public int GetRemainingCards() {
		return cards.size();
	}
}
