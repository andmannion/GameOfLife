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

	public void Shuffle() {
		int deckSize = cards.size();
		
		for (int i = 0; i < deckSize; i++) {
			int card = (int) (Math.random() * (deckSize - i));
			cards.addLast(cards.remove(card));
		}
	}

	public int GetRemainingCards() {
		return cards.size();
	}
}
