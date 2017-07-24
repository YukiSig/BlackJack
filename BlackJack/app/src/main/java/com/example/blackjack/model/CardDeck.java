package com.example.blackjack.model;

import com.example.blackjack.utils.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * CardDeck class.
 *
 * Created by hiroki.kakuno on 2017/07/13.
 */

public class CardDeck {

    private static final String TAG = "BlackJack.CardDeck";

    /** Card number type */
    public static final int NUMBER_TYPE = 13;

    /** CardDeck  */
    private Deque<Card> mCardDeck;

    public void init() {
        Logger.d(TAG, "initialize CardDeck.");
        if (mCardDeck == null) {
            mCardDeck = new ArrayDeque<>();
        }
        mCardDeck.clear();

        List<Card> cardList = new ArrayList<>();
        // Initialize to  CardDeck(1 ~ 13)
        for (Suit face : Suit.values()) {
            for (int i = 0; i < NUMBER_TYPE; i++) {
                mCardDeck.push(new Card(i+1, face));
            }
        }
    }

    /**
     * shuffle Card deck.
     */
    public void shuffle() {
        Logger.d(TAG, "shuffle CardDeck.");

        List<Card> cardList = new ArrayList<>();
        if (mCardDeck == null) {
            init();
        }

        // copy card
        for (Card card : mCardDeck) {
            cardList.add(card);
        }

        // shuffle card.
        Collections.shuffle(cardList);
        Collections.shuffle(cardList);
        Collections.shuffle(cardList);
        Collections.shuffle(cardList);

        // reset to CardDeck
        mCardDeck.clear();
        for (Card card : cardList) {
            mCardDeck.push(card);
        }
    }

    /**
     * get next card.
     * @return draw card
     */
    public Card drawCard() {
        int size = mCardDeck.size();
        Logger.d(TAG, "draw card remaining number : " + size);
        if (size < 0) {
            return null;
        }
        Card card = mCardDeck.poll();
        Logger.d(TAG, "draw card : " + card);

        return card;
    }

    /**
     * if return true, can draw card.
     *
     * @return can draw.
     */
    public boolean canDrawCard() {
        return !mCardDeck.isEmpty();
    }
}
