package com.example.blackjack.view;

import android.content.res.Resources;
import android.view.LayoutInflater;

import com.example.blackjack.model.Card;

import java.util.List;

/**
 * DealerCardListAdapter class.
 *
 * Created by hiroki.kakuno on 2017/07/19.
 */

public class DealerCardListAdapter extends BaseCardListAdapter {

    public DealerCardListAdapter(LayoutInflater inflater, Resources resources, List<Card> cards) {
        super(inflater, resources, cards);

        // Dealer card dose not open.
        for (Card card : cards) {
            boolean close = mCardItems.size() >= 1;
            CardItem item = new CardItem(card, close);
            mCardItems.add(item);
        }
    }
}
