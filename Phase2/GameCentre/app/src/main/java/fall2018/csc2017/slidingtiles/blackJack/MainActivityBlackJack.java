package fall2018.csc2017.slidingtiles.blackJack;

import android.content.DialogInterface;
import fall2018.csc2017.slidingtiles.R;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

/**
 * this is the main class that all of Black Jacks activities are run from
 */
public class MainActivityBlackJack extends AppCompatActivity {
    /**
     * this is an array array of the cards and their corresponding black jack values
     */
    int[][] cards = {{R.drawable.card_1, 2}, {R.drawable.card_14, 2}, {R.drawable.card_27, 2}, {R.drawable.card_40, 2},
            {R.drawable.card_2, 3}, {R.drawable.card_15, 3}, {R.drawable.card_28, 3}, {R.drawable.card_41, 3},
            {R.drawable.card_3, 4}, {R.drawable.card_16, 4}, {R.drawable.card_29, 4}, {R.drawable.card_42, 4},
            {R.drawable.card_4, 5}, {R.drawable.card_17, 5}, {R.drawable.card_30, 5}, {R.drawable.card_43, 5},
            {R.drawable.card_5, 6}, {R.drawable.card_18, 6}, {R.drawable.card_31, 6}, {R.drawable.card_44, 6},
            {R.drawable.card_6, 7}, {R.drawable.card_19, 7}, {R.drawable.card_32, 7}, {R.drawable.card_45, 7},
            {R.drawable.card_7, 8}, {R.drawable.card_20, 8}, {R.drawable.card_33, 8}, {R.drawable.card_46, 8},
            {R.drawable.card_8, 9}, {R.drawable.card_21, 9}, {R.drawable.card_34, 9}, {R.drawable.card_47, 9},
            {R.drawable.card_9, 10}, {R.drawable.card_22, 10}, {R.drawable.card_35, 10}, {R.drawable.card_48, 10},
            {R.drawable.card_10, 10}, {R.drawable.card_23, 10}, {R.drawable.card_36, 10}, {R.drawable.card_49, 10},
            {R.drawable.card_11, 10}, {R.drawable.card_24, 10}, {R.drawable.card_37, 10}, {R.drawable.card_50, 10},
            {R.drawable.card_12, 10}, {R.drawable.card_25, 10}, {R.drawable.card_38, 10}, {R.drawable.card_51, 10},
            {R.drawable.card_13, 11}, {R.drawable.card_26, 11}, {R.drawable.card_39, 11}, {R.drawable.card_52, 11}};

    /**
     * this is the method that creates the the game
     * @param savedInstanceState this is the referrence to the bundle that is passed to all create methods
     */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blackjack);
        ImageView playerCard1 = findViewById(R.id.myCard1);
        ImageView playerCard2 = findViewById(R.id.myCard2);


        ImageView houseCard1 = findViewById(R.id.dealerCard1);
        ImageView houseCard2 = findViewById(R.id.dealerCard2);


        //this is creating the template that starts the betting values and collects them

        //sets up the first two cards of player and dealer and then returns player total
        int playerHandTotal = setInitialCards(playerCard1, houseCard1, playerCard2, houseCard2);
        //displays the player score subtly to them
        Snackbar playerMessage = displayHand(playerHandTotal);
        determineBet(playerMessage);

    }

    /**
     * this method determines the bet using a alertdialog to prompt the user on their bet this round
     */
    private void determineBet(final Snackbar currentHand) {
        final String[] betValues = {"50", "100", "200", "250", "300", "400", "500"};
        AlertDialog.Builder diaologBuilder = new AlertDialog.Builder(MainActivityBlackJack.this);
        TextView accountBalance = (findViewById(R.id.availableBalance));
        diaologBuilder.setTitle("Your currently have $" + Integer.parseInt((String) accountBalance.getText()));
        final String[] validBetValues = verifyBet(betValues);
        diaologBuilder.setSingleChoiceItems(validBetValues, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView playerHand = findViewById(R.id.handValue);
                playerHand.setText(validBetValues[i]);
                currentHand.show();
                dialogInterface.dismiss();

            }
        });
        AlertDialog readyDialog = diaologBuilder.create();
        readyDialog.setCancelable(false);
        readyDialog.show();
    }

    /**
     * this method returns a string of the list items to be displaed on how much you
     * can bet, the reason this is needed is so that you cant bet more money than you have
     * @param betValues this is the original string that needs to be checked for removing elements
     * @return this is the returned string array that accomadates the players current balance
     * so they cant bet more money than they have
     */
    private String[] verifyBet(String[] betValues) {
        int[] betQuantities = new int[betValues.length];
        TextView gameBalance = findViewById(R.id.availableBalance);
        int currentFunds = Integer.parseInt((String) gameBalance.getText());
        ArrayList<String> passingValues = new ArrayList<>();
        for (int x = 0; x < betValues.length; x++) {
            betQuantities[x] = Integer.parseInt(betValues[x]);
            if (betQuantities[x] <= currentFunds) {
                passingValues.add(Integer.toString(betQuantities[x]));
            }
        }
        String[] betReturns = new String[passingValues.size()];
        for (int x = 0; x < passingValues.size(); x++) {
            betReturns[x] = passingValues.get(x);
        }
        String[] lastCase = {"0"};

        if (betReturns.length == 0) {
            return lastCase;
        }
        return betReturns;
    }

    /**
     * this methods is used to display to the player the current total value in their hand
     * @param playersTotal this takes the value of their hand which was given by an earlier method
     * @return this returns an object that is capapble of displaying the total hand value to the
     * player
     */
    private Snackbar displayHand(int playersTotal) {
        //if this part isnt working its prob because of the new statement
        //implements SDK compile 27.0 in the build.grade file
        //under dependencies, this was the only way to make it work on current SDK 28
        Snackbar currentHand;
        currentHand = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                "You current hand is " + playersTotal, Snackbar.LENGTH_LONG);

        return currentHand;
    }

    /**
     *  this returns back the total value fo the the players Hand after setting up the first two
     *  cards of the player and the dealer
     * @param playerCard1 this is the first card of the player and receives all the information of
     *                    the random draw a player would make
     * @param houseCard1 this is the first card of the dealer and receives all the information of the
     *                   random draw given to the dealer
     * @param playerCard2 this is the second draw of the player and receives all the information of
     *                    the random draw a player would make
     * @param houseCard2 this is the second draw of the dealer and receives all the information of the
     *                   random draw given to the dealer
     * @return this returns the total value of the players hand
     */
    private int setInitialCards(ImageView playerCard1, ImageView houseCard1,
                                ImageView playerCard2, ImageView houseCard2) {

        Collections.shuffle(Arrays.asList(cards));
        //the player takes the first card, reveals it and has it's value
        int draw = 0;
        playerCard1.setImageResource(cards[draw][0]);
        playerCard1.setTag(Integer.toString(cards[draw][1]));
        playerCard1.setVisibility(View.VISIBLE);
        int playerHandTotal = cards[draw][1];
        cards = reduceDeck(cards, draw);

        //the house takes their first card by the process above
        int draw2 = 0;
        houseCard1.setImageResource(cards[draw2][0]);
        houseCard1.setTag(Integer.toString(cards[draw2][1]));
        houseCard1.setVisibility(View.VISIBLE);

        cards = reduceDeck(cards, draw2);

        // the player takes their second card as per same method
        int draw3 = 0;
        playerCard2.setImageResource(cards[draw3][0]);
        playerCard2.setTag(Integer.toString(cards[draw3][1]));
        playerCard2.setVisibility(View.VISIBLE);
        playerHandTotal = playerHandTotal + cards[draw3][1];
        cards = reduceDeck(cards, draw3);

        //the house now draws a card but it is face down till later
        int draw4 = 0;
        TextView tore = findViewById(R.id.storageText);
        //tore.setText(Integer.toString());
        tore.setText(String.format(Locale.getDefault(), "%d", cards[draw4][0]));
        houseCard2.setTag(Integer.toString(cards[draw4][1]));
        houseCard2.setVisibility(View.VISIBLE);
        cards = reduceDeck(cards, draw4);

        //as per Black Jack rules if the total of the players hand is between 9 and
        //11 then they can double down on their bet provided they have the money to
        //do so
        if (9 <= playerHandTotal && playerHandTotal <= 11) {
            Button doubler = findViewById(R.id.doubleButton);
            doubler.setVisibility(View.VISIBLE);
        }

        return playerHandTotal;
    }

    /**
     * this is a method that takes the full possible hand that a player can achieve and
     * retrieves the values of each hand, face down cards besides the dealers second card
     * all have a value of 0 and thus it returns the active hand's amount
     * @param card1 the first card of whichever player
     * @param card2 the second card of whichever player
     * @param card3 the third card of whichever player
     * @param card4 the fourth card of whiechever player
     * @param card5 the fifth card of whichever player
     * @param card6 the sixth card of whichever player
     * @param card7 the seventh card of whichever player
     * @return the total of the full hand for that player, counts only the face-up cards
     */
    private int evaluateHand(ImageView card1, ImageView card2, ImageView card3,
                             ImageView card4, ImageView card5, ImageView card6,
                             ImageView card7) {
        int totalHand;
        totalHand = Integer.parseInt((String) card1.getTag()) +
                Integer.parseInt((String) card2.getTag()) +
                Integer.parseInt((String) card3.getTag()) +
                Integer.parseInt((String) card4.getTag()) +
                Integer.parseInt((String) card5.getTag()) +
                Integer.parseInt((String) card6.getTag()) +
                Integer.parseInt((String) card7.getTag());

        return totalHand;
    }

    /**
     * this method takes the deck of cards and reduces it by one in the position of the
     * card that was last drawn at its index, this is to mimic a limited card deck so
     * the chance of repeat cards is unlikely
     * @param allcards this is the array array that the cards are stored in with their values
     * @param position this is the position of the last drawn card
     * @return this is the deck returned without that one card
     */
    private int[][] reduceDeck(int[][] allcards, int position) {
        int[][] newDeck = new int[allcards.length - 1][2];
        int count = 0;
        for (int x = 0; x < allcards.length; x++) {
            if (x != position) {
                newDeck[count] = allcards[x];
                count++;
            }
        }
        return newDeck;
    }

    /**
     * this is the stand option that is available in every game of standard
     * Black Jack. Stand is when the player is done with their cards and the dealer
     * draws till they have 17 or above. during this time the player can either gain
     * money, lose money or lose nothing depending on dealers outcomes. neither player
     * can get above 21 but a result fo exactly 21 is preferred.  the one with the higher
     * number wins while taking the above into account. if the player won through
     * blackjack then they get 1.5 times the amount
     * @param v this containes the listener for the stand button click event
     */
    public void standOnClick(View v) {
        ImageView playerCard1 = findViewById(R.id.myCard1);
        ImageView playerCard2 = findViewById(R.id.myCard2);
        ImageView playerCard3 = findViewById(R.id.myCard3);
        ImageView playerCard4 = findViewById(R.id.myCard4);
        ImageView playerCard5 = findViewById(R.id.myCard5);
        ImageView playerCard6 = findViewById(R.id.myCard6);
        ImageView playerCard7 = findViewById(R.id.myCard7);

        ImageView houseCard1 = findViewById(R.id.dealerCard1);
        ImageView houseCard2 = findViewById(R.id.dealerCard2);
        ImageView houseCard3 = findViewById(R.id.dealerCard3);
        ImageView houseCard4 = findViewById(R.id.dealerCard4);
        ImageView houseCard5 = findViewById(R.id.dealerCard5);
        ImageView houseCard6 = findViewById(R.id.dealerCard6);
        ImageView houseCard7 = findViewById(R.id.dealerCard7);


        int playersHand = evaluateHand(playerCard1, playerCard2, playerCard3,
                playerCard4, playerCard5, playerCard6, playerCard7);
        int dealersHand = evaluateHand(houseCard1, houseCard2, houseCard3,
                houseCard4, houseCard5, houseCard6, houseCard7);

        TextView storage = findViewById(R.id.storageText);
        houseCard2.setImageResource(Integer.parseInt((String) storage.getText()));
        ImageView[] dealersBlankCards = {houseCard1, houseCard2, houseCard3, houseCard4, houseCard5,
                houseCard6, houseCard7};


        while (dealersHand < 17) {
            int blankTracker = 0;
            int pos = 0;
            while (blankTracker == 0) {
                if (Integer.parseInt((String) dealersBlankCards[pos].getTag()) == 0) {
                    Collections.shuffle(Arrays.asList(cards));
                    dealersBlankCards[pos].setImageResource(cards[0][0]);
                    dealersBlankCards[pos].setTag(Integer.toString(cards[0][1]));
                    dealersBlankCards[pos].setVisibility(View.VISIBLE);
                    cards = reduceDeck(cards, 0);
                    blankTracker++;
                }
                pos++;
            }
            dealersHand = evaluateHand(houseCard1, houseCard2, houseCard3,
                    houseCard4, houseCard5, houseCard6, houseCard7);
        }

        if (dealersHand > 21) {
            if (playersHand == 21) {
                resetBoard(4, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            } else {
                resetBoard(1, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            }
        } else if (dealersHand == 21) {
            if (playersHand == 21) {
                resetBoard(0, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            } else if (playersHand < 21) {
                resetBoard(2, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            }
        } else if (dealersHand < 21) {
            if (playersHand == 21) {
                resetBoard(4, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            } else if (playersHand > dealersHand) {
                resetBoard(3, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            } else if (playersHand == dealersHand) {
                resetBoard(0, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            } else if (playersHand < dealersHand) {
                resetBoard(2, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                        playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                        houseCard5, houseCard6, houseCard7);
            }
        }

    }

    /**
     * this is the hit option available at every standard Black Jack game. This option
     * signifies that the player would like to draw another card. if the value of his
     * hand exceed 21 then it is a bust with all hsi money lost, if it is below 21 then
     * he simply requires the dealer to get below that.
     * @param v this is the listener set for the HIt button
     */
    public void hitOnClick(View v) {
        ImageView playerCard1 = findViewById(R.id.myCard1);
        ImageView playerCard2 = findViewById(R.id.myCard2);
        ImageView playerCard3 = findViewById(R.id.myCard3);
        ImageView playerCard4 = findViewById(R.id.myCard4);
        ImageView playerCard5 = findViewById(R.id.myCard5);
        ImageView playerCard6 = findViewById(R.id.myCard6);
        ImageView playerCard7 = findViewById(R.id.myCard7);

        ImageView houseCard1 = findViewById(R.id.dealerCard1);
        ImageView houseCard2 = findViewById(R.id.dealerCard2);
        ImageView houseCard3 = findViewById(R.id.dealerCard3);
        ImageView houseCard4 = findViewById(R.id.dealerCard4);
        ImageView houseCard5 = findViewById(R.id.dealerCard5);
        ImageView houseCard6 = findViewById(R.id.dealerCard6);
        ImageView houseCard7 = findViewById(R.id.dealerCard7);

        int playersHand;


        ImageView[] playersBlankCards = {playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                playerCard6, playerCard7};

        int blankTracker = 0;
        int position = 0;
        while (blankTracker == 0) {
            if (Integer.parseInt((String) playersBlankCards[position].getTag()) == 0) {
                Collections.shuffle(Arrays.asList(cards));
                playersBlankCards[position].setImageResource(cards[0][0]);
                playersBlankCards[position].setTag(Integer.toString(cards[0][1]));
                playersBlankCards[position].setVisibility(View.VISIBLE);
                cards = reduceDeck(cards, 0);
                blankTracker++;
            }
            position++;
        }
        playersHand = evaluateHand(playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                playerCard6, playerCard7);
        if (playersHand > 21) {
            resetBoard(5, playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                    playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                    houseCard5, houseCard6, houseCard7);
        } else {
            Snackbar newHand = displayHand(playersHand);
            newHand.show();

        }

    }

    /**
     * this is the double option that is available to use in standard blackJack but is not
     * entirely crucial for the progress of the game. if the player has between 9 to 11 on the
     * value of his first 2 cards then he can choose to accept one card alone to double his bet
     * under the reasoning that they have that money available.
     * @param v this is the listener for the double button.
     */
    public void doubleOnClick(View v) {
        ImageView playerCard1 = findViewById(R.id.myCard1);
        ImageView playerCard2 = findViewById(R.id.myCard2);
        ImageView playerCard3 = findViewById(R.id.myCard3);
        ImageView playerCard4 = findViewById(R.id.myCard4);
        ImageView playerCard5 = findViewById(R.id.myCard5);
        ImageView playerCard6 = findViewById(R.id.myCard6);
        ImageView playerCard7 = findViewById(R.id.myCard7);

        TextView balance = findViewById(R.id.availableBalance);
        TextView bet = findViewById(R.id.handValue);
        Button doubleButton = findViewById(R.id.doubleButton);
        int balanceValue = Integer.parseInt((String) balance.getText());
        int betValue = Integer.parseInt((String) bet.getText());

        if (balanceValue >= 2 * betValue) {
            Collections.shuffle(Arrays.asList(cards));
            playerCard3.setImageResource(cards[0][0]);
            playerCard3.setTag(Integer.toString(cards[0][1]));
            playerCard3.setVisibility(View.VISIBLE);
            bet.setText(String.format(Locale.getDefault(), "%d", 2 * betValue));

            int playersHand = evaluateHand(playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                    playerCard6, playerCard7);
            Snackbar newHand = displayHand(playersHand);
            newHand.show();
            Button StandButton = findViewById(R.id.standButton);
            StandButton.performClick();
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivityBlackJack.this);
            dialogBuilder.setMessage("You do not have the balance to perform that action. Please select " +
                    "another option.");
            dialogBuilder.setPositiveButton("Alright", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            AlertDialog messageDialog = dialogBuilder.create();
            messageDialog.setCancelable(false);
            messageDialog.show();
        }
        doubleButton.setVisibility(View.GONE);
    }

    /**
     *  this is the method that reset the entire board after a result has come whether it is the
     *  player or the dealers win or a draw
     * @param state this is the "state" under which the game ended, depending on how the
     *              game ended the state can have different numbers and these modify the
     *              display message on the game. the method then
     * @param playerCard1 this is the first card of the player, it is reset and put back face-down
     * @param playerCard2 this is the second card of the player, it is reset and put back face-down
     * @param playerCard3 this is the third card of the player, it is reset and put back face-down
     * @param playerCard4 this is the fourth card of the player, it is reset and put back face-down
     * @param playerCard5 this is the fifth card of the player, it is reset and put back face-down
     * @param playerCard6 this is the sixth card of the player, it is reset and put back face-down
     * @param playerCard7 this is the seventh card of the player, it is reset and put back face-down
     * @param houseCard1 this is the first card of the dealer, it is reset and put back face-down
     * @param houseCard2 this is the second card of the dealer, it is reset and put back face-down
     * @param houseCard3 this is the third card of the dealer, it is reset and put back face-down
     * @param houseCard4 this is the fourth card of the dealer, it is reset and put back face-down
     * @param houseCard5 this is the fifth card of the dealer, it is reset and put back face-down
     * @param houseCard6 this is the sixth card of the dealer, it is reset and put back face-down
     * @param houseCard7 this is the seventh card of the dealer, it is reset and put back face-down
     */
    private void resetBoard(int state, final ImageView playerCard1, final ImageView playerCard2,
                            ImageView playerCard3, ImageView playerCard4, ImageView playerCard5,
                            ImageView playerCard6, ImageView playerCard7, final ImageView houseCard1,
                            final ImageView houseCard2, ImageView houseCard3, ImageView houseCard4,
                            ImageView houseCard5, ImageView houseCard6, ImageView houseCard7) {
        final ImageView[] allPlayersCards = {playerCard1, playerCard2, playerCard3, playerCard4, playerCard5,
                playerCard6, playerCard7, houseCard1, houseCard2, houseCard3, houseCard4,
                houseCard5, houseCard6, houseCard7};
        TextView accountBalance = findViewById(R.id.availableBalance);
        TextView lastBet = findViewById(R.id.handValue);

        String response = "";
        int newBalance = 0;
        if (state == 1) {

            response = "You have won by the Dealer's Bust, on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText()) +
                    Integer.parseInt((String) lastBet.getText());
        } else if (state == 0) {
            response = "You have tied with the Dealer, on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText());
        } else if (state == 2) {
            response = "You have lost this round, on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText()) -
                    Integer.parseInt((String) lastBet.getText());
        } else if (state == 3) {

            response = "You have won this round, on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText()) +
                    Integer.parseInt((String) lastBet.getText());

        } else if (state == 4) {

            response = "Congratulations, you got an unmatched 'BlackJack', on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText()) +
                    (int) Math.round(1.5 * Integer.parseInt((String) lastBet.getText()));
        } else if (state == 5) {
            response = "You got a Bust, on to the next round.";

            newBalance = Integer.parseInt((String) accountBalance.getText()) -
                    Integer.parseInt((String) lastBet.getText());
        }

        accountBalance.setText(String.format(Locale.getDefault(), "%d", newBalance));
        final int postRoundBalance = Integer.parseInt((String) accountBalance.getText());
        lastBet.setText(R.string.zero);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivityBlackJack.this);
        dialogBuilder.setMessage(response);
        dialogBuilder.setPositiveButton("Alright", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (postRoundBalance <= 0) {
                    lackBalanceAction();
                }

                for (ImageView item : allPlayersCards) {
                    item.setTag(Integer.toString(0));
                    item.setImageResource(R.drawable.card_0);
                    item.setVisibility(View.INVISIBLE);
                }

                Button doubler = findViewById(R.id.doubleButton);
                doubler.setVisibility(View.GONE);
                int playerHandTotal = setInitialCards(playerCard1, houseCard1, playerCard2, houseCard2);
                Snackbar newHand = displayHand(playerHandTotal);
                determineBet(newHand);
            }
        });

        AlertDialog messageDialog = dialogBuilder.create();
        messageDialog.setCancelable(false);
        messageDialog.show();

    }

    private void lackBalanceAction() {
        AlertDialog.Builder dialogBuiler = new AlertDialog.Builder(MainActivityBlackJack.this);
        dialogBuiler.setMessage("You appear to be out of money, unfortunately the game can" +
                " no longer continue. Come again soon.");
        dialogBuiler.setPositiveButton("Alright", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        AlertDialog messageDialog = dialogBuiler.create();
        messageDialog.setCancelable(false);
        messageDialog.show();
    }

}
