package fall2018.csc2017.slidingtiles.draughts;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.DisplayMetrics;

import java.io.Serializable;

import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.draughts.game.CheckerBoard;
import fall2018.csc2017.slidingtiles.draughts.game.CheckersGame;
import fall2018.csc2017.slidingtiles.draughts.game.Piece;
import fall2018.csc2017.slidingtiles.draughts.game.Position;

/**
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class CheckersLayout extends TableLayout implements Serializable {

    public class CheckerImageView extends AppCompatImageView {
        public int x;
        public int y;
        public CheckerImageView(Activity activity) {
            super(activity);
        }
    }

    protected MyCheckersActivity myActivity;
    protected CheckersGame myGame;
    protected CheckerImageView cells[][];

    private final OnClickListener CellClick = new OnClickListener() {
        @Override
        public void onClick(View _view) {
            CheckerImageView view = (CheckerImageView)_view;
            myActivity.onClick(view.x, view.y);
        }
    };

    /**
     *  refresh the position/layout of every checker piece
     */
    public void refresh() {
        CheckerBoard myBoard = myGame.getBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++)
                if (myBoard.isGameSquare(x, y)) {
                    CheckerImageView cell = cells[x][y];
                    Piece piece = myBoard.getPiece(x, y);
                    if (piece != null) {
                        int color = piece.getColor();
                        boolean king = piece.isKing();
                        // set the correct image
                        if (color == CheckersGame.RED) {
                            if (king) {
                                cell.setImageResource(R.drawable.maroonupgraded);
                            } else {
                                cell.setImageResource(R.drawable.maroon);
                            }
                        } else if (color == CheckersGame.BLACK) {
                            if (king) {
                                cell.setImageResource(R.drawable.blackupgraded);
                            } else {
                                cell.setImageResource(R.drawable.black);
                            }
                        }
                        // set the background color
                        if (myActivity.isSelected(piece)) {
                            cell.setBackgroundColor(getResources().getColor(R.color.cellSelect));
                        } else {
                            cell.setBackgroundColor(getResources().getColor(R.color.blackSquare));
                        }
                    } else {
                        // clear the image
                        cell.setImageDrawable(null);
                        Position curPos = new Position(x, y);
                        if (myActivity.isOption(curPos) /* && highlightsEnabled */) {
                            cell.setBackgroundColor(getResources().getColor(R.color.cellOption));
                        } else {
                            cell.setBackgroundColor(getResources().getColor(R.color.blackSquare));
                        }
                    }
                }
        }
    }

    /**
     * display the board layout
     *
     * @param game the existing CheckersGame object
     * @param activity the existing MyCheckersActivity object
     */
    public CheckersLayout(CheckersGame game, MyCheckersActivity activity) {
        super(activity);
        myActivity = activity;

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dimension = displayMetrics.widthPixels;
        if (displayMetrics.heightPixels < dimension) {
            dimension = displayMetrics.heightPixels;
        }
        int cellDimension = ((dimension - 2) / 8) - 2;

        LayoutParams params;

        myGame = game;
        CheckerBoard myBoard = myGame.getBoard();

        params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 1, 1, 1);
        setLayoutParams(params);
        setBackgroundColor(Color.rgb(48, 48, 48));

        // add table of image views
        cells = new CheckerImageView[8][8];
        for (int y = 0; y < 8; y++) {
            TableRow row = new TableRow(activity);
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tableParams.setMargins(0, 0, 0, 0);
            row.setLayoutParams(tableParams);

            for (int x = 0; x < 8; x++) {
                CheckerImageView cell;
                cells[x][y] = cell = new CheckerImageView(activity);
                cell.x = x;
                cell.y = y;

                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                rowParams.setMargins(1, 1, 1, 1);
                rowParams.width = cellDimension;
                rowParams.height = cellDimension;
                cell.setLayoutParams(rowParams);

                int bgColor;
                if (myBoard.isGameSquare(x,y)) {
                    // add click handler
                    cell.setOnClickListener(CellClick);
                    bgColor = getResources().getColor(R.color.blackSquare);
                }
                else {
                    bgColor = getResources().getColor(R.color.whiteSquare);
                }

                cell.setBackgroundColor(bgColor);
                row.addView(cell);
            }
            addView(row);
        }
    }
}
