package ie.ucd.engac.lifegamelogic.cards;

import java.util.LinkedList;

public abstract class Deck<T> {
	LinkedList<T> items;

	public Deck() {
		items = new LinkedList<>();
	}

	public T popTopCard() {
		if(items.size() > 0){
			return items.pop();
		}
		return null;
	}

	public void addCardToBottom(T item) {
		items.addLast(item);
	}

	public void shuffle() {
		int itemsSize = items.size();
		
		for (int i = 0; i < itemsSize; i++) {
			int item = (int) (Math.random() * (itemsSize - i));
			items.addLast(items.remove(item));
		}
	}

	public int getNumberOfRemainingCards() {
		return items.size();
	}
}
