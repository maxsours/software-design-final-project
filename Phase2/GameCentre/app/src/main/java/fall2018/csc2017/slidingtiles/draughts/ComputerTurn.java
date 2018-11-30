package fall2018.csc2017.slidingtiles.draughts;

import android.os.AsyncTask;

import java.io.Serializable;
import java.util.ArrayList;

import fall2018.csc2017.slidingtiles.draughts.game.CheckerBoard;
import fall2018.csc2017.slidingtiles.draughts.game.CheckersGame;
import fall2018.csc2017.slidingtiles.draughts.game.Move;
import fall2018.csc2017.slidingtiles.draughts.game.Piece;

/**
 * Responds to the Users move with an automated opponent checker move.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class ComputerTurn extends AsyncTask<String, String, String> implements Serializable
{
    /**
     * The currently played checker game's activity.
     */
    private MyCheckersActivity myActivity;

    /**
     * The current played checker game.
     */
    private CheckersGame myGame;

    /**
     * The currently played checker game's difficulty.
     */
    private String myDifficulty;

    /**
     * The selected move by ComputerTurn.
     */
    private Move selectedMove;

    /**
     * Whether any move is permitted in the currently played checker game.
     */
    private boolean allowAnyMove;

    /**
     * Whether the game has been won.
     */
    private boolean wonStatus ;

    /**
     * The constructor for ComputerTurn
     *
     * @param activity the new/current MyCheckersActivity
     * @param game the new/current CheckersGame
     * @param difficulty the difficulty of the game
     * @param allowAny whether any move is permitted in the game
     */
    public ComputerTurn(MyCheckersActivity activity,
                        CheckersGame game,
                        String difficulty,
                        boolean allowAny)
    {
        super();

        myActivity = activity;
        myGame = game;
        myDifficulty = difficulty;
        allowAnyMove = allowAny;
        selectedMove = null;
        wonStatus = false;
    }

    /**
     *The algorithm for the the AI to play the checkers
     *
     * @param base the currently played CheckerBoard
     * @param turn whether it is the AI's turn
     * @param depth the current depth of search for the algorithm.
     * @return the algorithm's decided move
     */
    protected int minimax(CheckerBoard base, int turn, int depth)
    {
        int oppositeTurn = (turn == CheckersGame.RED ? CheckersGame.BLACK : CheckersGame.RED);
        Move[] baseMoves = base.getMoves(turn, allowAnyMove);

        int score = 999;

        Integer [][] data = base.saveBoard();
        for (Move move : baseMoves)
        {
            CheckerBoard specificBoard = new CheckerBoard(data);
            specificBoard.makeMove(move);

            int moveScore;
            if (depth > 0) {
                moveScore = minimax(specificBoard, oppositeTurn, depth - 1);
            } else {
                moveScore = specificBoard.pseudoScore();
            }

            if (score == 999) {
                score = moveScore;
            }

            if (turn == CheckersGame.RED) {
                score = (moveScore < score) ? moveScore : score;
            } else if (turn == CheckersGame.BLACK) {
                score = (moveScore > score) ? moveScore : score;
            }
        }
        return score;
    }

    /**
     * Determines whether the AI should use a random move; plays random move if deemed appropriate.
     * @param depth the current depth of search for the algorithm.
     * @return random move
     */
    protected Move Minimax(int depth)
    {
        CheckerBoard realBoard = myGame.getBoard();
        Move moves[] = myGame.getMoves();

        Integer[][] data = realBoard.saveBoard();

        ArrayList<Move> bestMoves = new ArrayList<>();
        int bestScore = 1000;
        for (Move move : moves) {
            CheckerBoard moveBoard = new CheckerBoard(data);
            moveBoard.makeMove(move);
            int score = minimax(moveBoard, CheckersGame.BLACK, depth);
            if (score < bestScore) {
                bestMoves.clear();
                bestScore = score;
            }
            if (score == bestScore) {
                bestMoves.add(move);
            }
        }
        int randomIndex = (int)(Math.random() * bestMoves.size());
        Move randomMove = bestMoves.get(randomIndex);
        return randomMove;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (myGame.whoseTurn() != CheckersGame.RED) return null;
        Move moves[] = myGame.getMoves();

        if (moves.length == 0) return null;

        int difficulty = 0;
        if (myDifficulty.equals("Easy")) {
            difficulty = 0;
        } else if (myDifficulty.equals("Medium")) {
            difficulty = 1;
        } else if (myDifficulty.equals("Hard")) {
            difficulty = 2;
        } else if (myDifficulty.equals("Very Hard")) {
            difficulty = 3;
        }

        if (difficulty == 0)
        {
            int num = (int)(moves.length * Math.random());
            selectedMove = moves[num];
        }
        else if (difficulty == 1)
        {
            selectedMove = moves[0];

            ArrayList<Move> selectedMoves = new ArrayList<>();

            int curScore = -1;
            for (Move option : moves) {
                int score = option.captures.size();
                Piece startPiece = myGame.getBoard().getPiece(option.start());
                if (option.kings && !startPiece.isKing())
                {
                    score += 2;
                }
                if (score > curScore) {
                    selectedMoves.clear();
                    selectedMoves.add(option);
                    curScore = score;
                } else if (score == curScore) {
                    selectedMoves.add(option);
                }
            }

            selectedMove = selectedMoves.get((int)(selectedMoves.size() * Math.random()));
        }

        if (difficulty > 1)
        {
            int depth = (difficulty == 2) ? 4 : 7;
            selectedMove = Minimax(depth);
        }
        else
        {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (myGame.whoseTurn() == CheckersGame.RED) {
            if (selectedMove != null) {
                myGame.makeMove(selectedMove);
                myActivity.prepTurn();
            } else {
                myActivity.statusText.setText("You won!");
                wonStatus = true;

                myActivity.setWinStatus(wonStatus, myDifficulty);
            }
        }
    }
}
