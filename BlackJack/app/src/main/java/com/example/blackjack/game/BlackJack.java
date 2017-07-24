package com.example.blackjack.game;

import com.example.blackjack.model.Card;
import com.example.blackjack.model.CardDeck;
import com.example.blackjack.player.Dealer;
import com.example.blackjack.player.Player;
import com.example.blackjack.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage BlackJack games.
 *
 * Created by hiroki.kakuno on 2017/07/14.
 */

public class BlackJack {

    private static final String TAG = "BlackJack.Main";

    /** BlackJack score 21 */
    private static final int BLACKJACK_SCORE = 21;
    /** Pure BlackJack number 2*/
    private static final int PURE_BLACKJACK_NUMBER = 2;
    /** Dealer limit score */
    private static final int DEALER_LIMIT_SCORE = 16;

    private static BlackJack sInstance;

    private CardDeck mCardDeck;
    private Player mPlayer;
    private Dealer mDealer;

    private BlackJack() {}

    public List<Card> getDealerHands() {
        return mDealer.getHands();
    }
    public List<Card> getPlayerHands() {
        return mPlayer.getHands();
    }

    public int getPlayerScore() {
        return mPlayer.getScore();
    }
    public int getDealerScore() {
        return mDealer.getScore();
    }

    /**
     * return BlackJack game instance.
     * @return instance.
     */
    public static BlackJack getInstance() {
        if (sInstance == null) {
            sInstance = new BlackJack();
        }
        return sInstance;
    }

    /**
     * if return true, can draw card.
     *
     * @return can draw.
     */
    public boolean canDrawCard() {
        return mCardDeck.canDrawCard();
    }

    /**
     * initialize Card Deck
     */
    public void initCardDeck() {
        if (mCardDeck == null) {
            mCardDeck = new CardDeck();
        }
        mCardDeck.init();
        mCardDeck.shuffle();
    }

    /**
     * initialize Player and Dealer
     * @param playerName
     */
    public void initPlayers(String playerName) {
        mPlayer = new Player(playerName);
        mDealer = new Dealer();
    }

    /**
     * Player and Dealer draw a card alternately.
     */
    public void dealCards() {
        mPlayer.drawCard(mCardDeck);
        mDealer.drawCard(mCardDeck);
        mPlayer.drawCard(mCardDeck);
        mDealer.drawCard(mCardDeck);

        mPlayer.setScore(calculateScore(mPlayer.getHands()));
        mDealer.setScore(calculateScore(mDealer.getHands()));
    }

    /**
     * draw card Player.
     * @return if return true, score is exceeds 21.
     */
    public boolean drawCardPlayer() {
        Logger.d(TAG, "draw card Player.");
        mPlayer.drawCard(mCardDeck);
        int score = calculateScore(mPlayer.getHands());
        mPlayer.setScore(score);
        return score >= BLACKJACK_SCORE;
    }

    /**
     * draw card Dealer.
     * @return if return true, score is exceeds 16.
     */
    public boolean drawCardDealer() {
        Logger.d(TAG, "draw card Dealer.");
        mDealer.drawCard(mCardDeck);

        int score = calculateScore(mDealer.getHands());
        boolean result = overLimitScore(score);
        mDealer.setScore(score);
        return result;
    }

    /**
     * check dealer's score.
     * @return if return true, the dealer's score exceeds 16.
     */
    public boolean checkDealerScore() {
        int score = calculateScore(mDealer.getHands());
        return overLimitScore(score);
    }

    /**
     * Check whether the limit point is exceeded.
     * @param score dealer score
     * @return if return true, the dealer's score exceeds 16.
     */
    private boolean overLimitScore(int score) {
        return score >= DEALER_LIMIT_SCORE;
    }

    /**
     * If hand contains an ACE, returns a number close to 21.
     * @param hands player hand.
     * @return score
     */
    private int calculateScore(List<Card> hands) {
        int score = 0; // ace == 11 case score.
        List<Card> aceCards = new ArrayList<>();
        for (Card card : hands) {
            score += card.score;
            if (card.number == Card.ACE) {
                aceCards.add(card);
            }
        }

        // count ace number.
        int aceSize = aceCards.size();
        Logger.d(TAG, "The number of ACE holding is " + aceSize + ".");
        if (aceSize < 1) {
            Logger.d(TAG, "calculateScore : " + score);
            return score;
        }

        int workScore = score - (aceSize * Card.ACE_SCORE);

        int floor = workScore + (Card.ACE * aceSize);
        Logger.d(TAG, "floor value : " + floor);
        int ceiling = workScore + Card.ACE_SCORE + (Card.ACE * (aceSize - 1));
        Logger.d(TAG, "ceiling value : " + ceiling);

        score = closedBlackJack(floor, ceiling);
        Logger.d(TAG, "The number close to 21 is " + score + ".");
        return score;
    }

    /**
     * Returns a value close to 21.
     * @param a value a
     * @param b value b
     * @return Returns a value close to 21.
     */
    private int closedBlackJack(int a, int b) {
        return Math.abs(a - BLACKJACK_SCORE) <= Math.abs(b - BLACKJACK_SCORE) ? a : b;
    }

    /**
     * if return true, Player won.
     * @return result game.
     */
    public Result resultGame() {
        int playerScore = mPlayer.getScore();
        int dealerScore = mDealer.getScore();
        Logger.d(TAG, "Player result score : " + playerScore + ".");
        Logger.d(TAG, "Dealer result score : " + dealerScore + ".");

        // case PlayerHand is burst.
        if (playerScore > BLACKJACK_SCORE) {
            return Result.LOSE;
        }

        // case DealerScore is burst.
        if (dealerScore > BLACKJACK_SCORE) {
            return Result.WIN;
        }

        // case PlayerHand is BLACKJACK.
        if (playerScore == BLACKJACK_SCORE) {
            if (dealerScore == BLACKJACK_SCORE) {
                // check hand number.
                int playerHand = mPlayer.getHands().size();
                int dealerHand = mDealer.getHands().size();

                // if playerHand number is 2, player is won.
                // if playerHand and dealerHand are 2, game is draw...
                if (playerHand == PURE_BLACKJACK_NUMBER) {
                    if (dealerHand == PURE_BLACKJACK_NUMBER) {
                        return Result.DRAW;
                    } else {
                        return Result.WIN;
                    }
                } else if (dealerHand == PURE_BLACKJACK_NUMBER) {
                    return Result.LOSE;
                } else {
                    return Result.DRAW;
                }
            } else {
                return Result.WIN;
            }
        }

        // case PlayerScore and DealerScore same.
        if (playerScore == dealerScore) {
            return Result.DRAW;
        }

        // case PlayerScore and DealerScore are not BLACKJACK.
        int result = closedBlackJack(playerScore, dealerScore);
        return playerScore == result ? Result.WIN : Result.LOSE;
    }
}
