package ie.ucd.engac.lifegamelogic.cards;

import java.util.LinkedList;

public abstract class CardDeck<T extends Card> {
	// Want to be able to: take from the top, and add to the bottom of the deck

	protected LinkedList<T> cards;

	public CardDeck() {
		cards = new LinkedList<T>();
	}

	public T popTopCard() {
		return cards.pop();
	}

	public void addCardToBottom(T card) {
		cards.addLast(card);
	}

	public void shuffle() {
		int deckSize = cards.size();
		
		for (int i = 0; i < deckSize; i++) {
			int card = (int) (Math.random() * (deckSize - i));
			cards.addLast(cards.remove(card));
		}
	}

	public int getRemainingCards() {
		return cards.size();
	}
}
