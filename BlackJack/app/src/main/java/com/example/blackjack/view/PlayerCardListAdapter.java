package com.example.blackjack.view;

import android.content.res.Resources;
import android.view.LayoutInflater;

import com.example.blackjack.model.Card;

import java.util.List;

/**
 * PlayerCardListAdapter class.
 *
 * Created by hiroki.kakuno on 2017/07/19.
 */
public class PlayerCardListAdapter extends BaseCardListAdapter {

    public PlayerCardListAdapter(LayoutInflater inflater, Resources resources, List<Card> cards) {
        super(inflater, resources, cards);

        // Player cards are opened.
        for (Card card : cards) {
            CardItem item = new CardItem(card, false);
            mCardItems.add(item);
        }
    }
}
