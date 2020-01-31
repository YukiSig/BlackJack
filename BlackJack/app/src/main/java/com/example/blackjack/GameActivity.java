package com.example.blackjack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.blackjack.game.BlackJack;
import com.example.blackjack.game.Result;
import com.example.blackjack.model.Card;
import com.example.blackjack.utils.Logger;
import com.example.blackjack.view.BaseCardListAdapter;
import com.example.blackjack.view.DealerCardListAdapter;
import com.example.blackjack.view.PlayerCardListAdapter;

import java.util.List;

/**
 * BlackJack's Game table Activity.
 *
 * Created by takeshi.okada on 2017/07/14.
 */

public class GameActivity extends Activity {

    private static final String TAG = "BlackJack.GameActivity";

    private BlackJack mBlackJack;

    private GridView mDealerCardListView;
    private GridView mPlayerCardListView;

    private BaseCardListAdapter mPlayerAdapter;
    private BaseCardListAdapter mDealerAdapter;

    private TextView mPlayerScore;
    private TextView mDealerScore;

    private Button mDrawButton;
    private Button mBattleButton;

    /** Animation delay time. */
    private static final int DELAYED_TIME = 1000;
    private MyHandler mMyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayerScore = findViewById(R.id.player_score);
        mDealerScore = findViewById(R.id.dealer_score);

        mPlayerCardListView = findViewById(R.id.card_player);
        mDealerCardListView = findViewById(R.id.card_dealer);

        mDrawButton = findViewById(R.id.btn_draw);
        mBattleButton = findViewById(R.id.btn_battle);

        mMyHandler = new MyHandler();

        initGame();
    }

    /**
     * Initialized game data.
     */
    private void initGame() {
        mBlackJack = BlackJack.getInstance();
        mBlackJack.initCardDeck();
        // TODO select Player name.
        mBlackJack.initPlayers("Player");
        mBlackJack.dealCards();

        Resources resources = getResources();
        LayoutInflater inflater = getLayoutInflater();

        mPlayerScore.setText(String.valueOf(mBlackJack.getPlayerScore()));
        mDealerScore.setText(getText(R.string.text_dealer_score));

        // to enable button.
        mDrawButton.setEnabled(true);
        mBattleButton.setEnabled(true);

        // init player game table.
        mPlayerAdapter = new PlayerCardListAdapter(inflater, resources, mBlackJack.getPlayerHands());
        mPlayerCardListView.setAdapter(mPlayerAdapter);
        mPlayerAdapter.notifyDataSetChanged();

        // init dealer game table.
        mDealerAdapter = new DealerCardListAdapter(inflater, resources, mBlackJack.getDealerHands());
        mDealerCardListView.setAdapter(mDealerAdapter);
        mDealerAdapter.notifyDataSetChanged();
    }

    /**
     * onClick DrawButton.
     * @param view view
     */
    public void onClickDraw(View view) {
        mBlackJack.drawCardPlayer();
        if (!mBlackJack.canDrawCard()) {
            view.setEnabled(false);
        }

        // update card list.
        updateCardList(mBlackJack.getPlayerHands(), mPlayerAdapter);
        mPlayerScore.setText(String.valueOf(mBlackJack.getPlayerScore()));
    }

    /**
     * onClick Battle
     * @param view view
     */
    public void onClickBattle(View view) {

        mDrawButton.setEnabled(false);
        mBattleButton.setEnabled(false);

        // display current dealer score.
        int score = mBlackJack.getDealerScore();
        mDealerScore.setText(String.valueOf(score));
        mDealerAdapter.openAllCard();

        // error case.
        // Player draw all card.
        if (!mBlackJack.canDrawCard()) {
            mMyHandler.sendEmptyMessageDelayed(MyHandler.RESULT, DELAYED_TIME);
            return;
        }

        // if checkDealerScore return true, execute game result process.
        if (mBlackJack.checkDealerScore()) {
            mMyHandler.sendEmptyMessageDelayed(MyHandler.RESULT, DELAYED_TIME);
        } else {
            mMyHandler.sendEmptyMessageDelayed(MyHandler.DRAW_DEALER, DELAYED_TIME);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * update and add Card List Item.
     * @param hands   Player hand
     * @param adapter adapter
     */
    private void updateCardList(List<Card> hands, BaseCardListAdapter adapter) {
        // add Card List view.
        if (!hands.isEmpty()) {
            int size = hands.size();
            Card card = hands.get(size - 1);
            adapter.addItem(card, false);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Game Handler class.
     */
    class MyHandler extends Handler {
        static final int DRAW_DEALER = 0;
        static final int RESULT = 1;

        @Override
        public void handleMessage(Message msg) {

            if (GameActivity.this.isFinishing()) {
                Logger.e(TAG, "GameActivity.isFinishing");
                return;
            }

            int what = msg.what;
            Logger.d(TAG, "handleMessage what : " + what);

            switch (what) {
                case DRAW_DEALER : {
                    drawDealer();
                    break;
                }
                case RESULT : {
                    result();
                    break;
                }
            }
        }

        /**
         * Dealer draw and update list view.
         */
        private void drawDealer() {
            boolean result = mBlackJack.drawCardDealer();
            updateCardList(mBlackJack.getDealerHands(), mDealerAdapter);
            mDealerScore.setText(String.valueOf(mBlackJack.getDealerScore()));

            Logger.d(TAG, "drawDealer result : " + result);
            int what = result ? RESULT : DRAW_DEALER;
            sendEmptyMessageDelayed(what, DELAYED_TIME);
        }

        /**
         * Game result process.
         */
        private void result() {
            Result result = mBlackJack.resultGame();
            Logger.d(TAG, "Game result : " + result);

            String message = getResultText(result);
            String title = getString(R.string.result_title);
            String btnRetry = getString(R.string.result_btn_retry);
            String btnEnd = getString(R.string.result_btn_end);

            // FIXME new and show.
            // display result dialog.
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(btnRetry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // OK button pressed
                            initGame();
                        }
                    })
                    .setNegativeButton(btnEnd, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel button pressed
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        /**
         * return Result tet.
         * @param result
         * @return
         */
        private String getResultText(Result result) {
            String message = "";
            switch (result) {
                case WIN:
                    // none
                    message = getString(R.string.result_win);
                    break;
                case LOSE:
                    message = getString(R.string.result_lose);
                    break;
                case DRAW:
                    message = getString(R.string.result_draw);
                    break;
                default:
                    break;
            }
            return message;
        }
    }
}
