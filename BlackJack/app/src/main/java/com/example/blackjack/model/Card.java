package com.example.blackjack.model;

/**
 * Card information class
 *
 * Created by hiroki.kakuno on 2017/07/13.
 */
public class Card {

    public static final int ACE = 1;
    public static final int ACE_SCORE = 11;
    public static final int FACE_CARD = 10;
    public static final int FACE_CARD_SCORE = 10;

    /** Card of number */
    public int number;

    /** Card of face */
    public Suit suit;

    /** Card of score */
    public int score;

    /**
     * Constructor
     * @param number card number
     * @param suit   card suit
     */
    public Card(int number, Suit suit) {
        this.number = number;
        this.suit = suit;

        // if number is 1, score is treated as 11.
        // else if number is over 10, score is treated as 10.
        if (number == ACE) {
            this.score = ACE_SCORE;
        } else if (number > FACE_CARD) {
            this.score = FACE_CARD_SCORE;
        } else {
            this.score = number;
        }
    }

    @Override
    public String toString() {
        return "number :" + number + ", Suit : " + suit + ", score : " + score;
    }
}
