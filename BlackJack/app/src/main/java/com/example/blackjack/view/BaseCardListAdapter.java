package com.example.blackjack.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.blackjack.R;
import com.example.blackjack.model.Card;
import com.example.blackjack.model.Suit;
import com.example.blackjack.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Card list Adapter class.
 *
 * Created by hiroki.kakuno on 2017/07/19.
 */

public class BaseCardListAdapter extends BaseAdapter {

    protected static final String TAG = "BlackJack.BaseCardListAdapter";

    protected Bitmap mImageTrump;
    protected List<CardItem> mCardItems;
    protected LayoutInflater mInflater;

    public BaseCardListAdapter(LayoutInflater inflater, Resources resources, List<Card> cards) {
        mInflater = inflater;
        mCardItems = new ArrayList<>();

        // create trump bitmap src.
        mImageTrump = BitmapFactory.decodeResource(resources, R.drawable.trump);
        Logger.d(TAG, "Bitmap width: " + mImageTrump.getWidth());
        Logger.d(TAG, "Bitmap height: " + mImageTrump.getHeight());
    }

    @Override
    public int getCount() {
        return mCardItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mCardItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Logger.d(TAG, "getView position : " + position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_item, parent, false);
        }

        CardItem item = (CardItem) getItem(position);
        ImageView imageView = convertView.findViewById(R.id.image_card);

        // create display trump image.
        Bitmap bmCard;
        CardBitmapBuilder builder = new CardBitmapBuilder(mImageTrump);
        if (item.isClose) {
            // build close card bitmap.
            bmCard = builder.isClose(true).build();
        } else {
            // build open card bitmap.
            bmCard = builder.setCard(item.card).build();
        }
        imageView.setImageBitmap(bmCard);
        return convertView;
    }

    /**
     * add Card item.
     * @param card card
     * @param close close flag.
     */
    public void addItem(Card card, boolean close) {
        mCardItems.add(new CardItem(card, close));
        notifyDataSetChanged();
    }

    /**
     * open all card.
     */
    public void openAllCard() {
        for (CardItem cardItem : mCardItems) {
            cardItem.isClose = false;
        }
        notifyDataSetChanged();
    }

    /**
     * Trump card Bitmap builder.
     * Generate Bitmap according to "trump.gif".
     */
    private class CardBitmapBuilder {

        /** Card information */
        Card card;

        /** Card open display flag */
        boolean isClose;

        Bitmap srcBitmap;
        int height;
        private int width;

        static final int ROW = 8;
        static final int COLUMN = 7;
        static final int THRESHOLD = 8;
        static final int SPADE_POSITION = 1;
        static final int DIAMOND_POSITION = 2;
        static final int CLUB_POSITION = 3;
        static final int CARD_POSITION = 4;

        CardBitmapBuilder(Bitmap src) {
            srcBitmap = src;

            width  = src.getWidth() / ROW;
            height = src.getHeight() / COLUMN;
        }

        CardBitmapBuilder isClose(boolean close) {
            isClose = close;
            return this;
        }
        CardBitmapBuilder setCard(Card card) {
            this.card = card;
            return this;
        }

        /**
         * build trump image.
         * @return trump bitmap.
         */
        Bitmap build() {
            Point point = new Point();
            if (isClose) {
                // designate close card.
                point.x = (ROW - 1) * width;
                point.y = (COLUMN - 1) * height;
            } else {
                point = getCardPoint(card.number, card.suit);
            }

            Logger.d(TAG, "Point : " + point);
            return Bitmap.createBitmap(srcBitmap, point.x, point.y, width, height, null, true);
        }

        /**
         * Get the position of the card to display for number and suit.
         * @param number card number
         * @param suit   card suit
         * @return position.
         */
        Point getCardPoint(int number, Suit suit) {
            Point point = new Point();

            // calculate column.
            int column = number / THRESHOLD;
            column *= CARD_POSITION;
            switch (suit) {
                case HEART:
                    // 0 or 4
                    break;
                case SPADE:
                    column += SPADE_POSITION;   // 1 or 5
                    break;
                case DIAMOND:
                    column += DIAMOND_POSITION; // 2 or 6
                    break;
                case CLUB:
                    column += CLUB_POSITION;    // 3 or 7
                    break;
                default:
                    break;
            }

            // calculate row.
            int row;
            if (number >= THRESHOLD) {
                row = (number % THRESHOLD) + 1;
            } else {
                row = number % THRESHOLD;
            }

            point.x = column * width;
            point.y = (row - 1) * height;

            Logger.d(TAG, "number: " + number);
            Logger.d(TAG, "suit : " + suit);
            Logger.d(TAG, "row : " + row);
            Logger.d(TAG, "column : " + column);
            return point;
        }
    }

}
