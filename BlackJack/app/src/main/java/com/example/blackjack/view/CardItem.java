package com.example.blackjack.view;

import com.example.blackjack.model.Card;
import com.example.blackjack.model.Suit;

/**
 * Card item class for ListAdapter.
 *
 * Created by hiroki.kakuno on 2017/07/19.
 */

public class CardItem {

    /** Card */
    public Card card;

    /** Card open display flag */
    public boolean isClose;

    /**
     * Constructor
     * @param card   Card
     * @param close  isClose flag
     */
    public CardItem(Card card, boolean close) {
        this.card = card;
        this.isClose = close;
    }
}
