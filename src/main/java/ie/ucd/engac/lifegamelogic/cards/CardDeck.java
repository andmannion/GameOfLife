package ie.ucd.engac.lifegamelogic.cards;

import java.util.ArrayList;

public class CardDeck<T> extends Deck<T> {
    private CardConfigHandler<T> cardConfigHandler;

    public CardDeck(CardConfigHandler<T> cardConfigHandler) {
        this.cardConfigHandler = cardConfigHandler;
        initialiseCards();
    }

    private void initialiseCards(){
        ArrayList<T> cards = cardConfigHandler.initialiseCards();
        items.addAll(cards);
    }
}