package com.example.blackjack.player;

import com.example.blackjack.model.Card;
import com.example.blackjack.model.CardDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of BlackJack Player.
 *
 * Created by hiroki.kakuno on 2017/07/13.
 */

public abstract class BasePlayer {

    /** My Hands */
    protected List<Card> mHands;
    public List<Card> getHands() {
        return mHands;
    }


    protected String mName;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * Player score
     */
    protected int mScore;
    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    /**
     * Constructor
     * @param name Player name
     */
    public BasePlayer(String name) {
        if (name == null || name.isEmpty()) {
            name = "Player"; // FIXME hard coding
        }
        mName = name;
        mHands = new ArrayList<>();
        initHands();
    }

    /** initialize to my Hand. */
    public void initHands() {
        mHands.clear();
        mScore = 0;
    }

    /**
     * draw card from CardDeck.
     * @param deck CardDeck
     * @return if return false, cannot draw cards.
     */
    public boolean drawCard(CardDeck deck) {
        Card card = deck.drawCard();
        if (card != null) {
            mHands.add(card);
            return true;
        }
        return false;
    }
}
