package fall2018.csc2017.slidingtiles.draughts;

import android.os.AsyncTask;

import java.io.Serializable;
import java.util.ArrayList;

import fall2018.csc2017.slidingtiles.draughts.game.CheckerBoard;
import fall2018.csc2017.slidingtiles.draughts.game.CheckersGame;
import fall2018.csc2017.slidingtiles.draughts.game.Move;
import fall2018.csc2017.slidingtiles.draughts.game.Piece;

/**
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class ComputerTurn extends AsyncTask<String, String, String> implements Serializable
{
    private MyCheckersActivity myActivity;
    private CheckersGame myGame;
    private String myDifficulty;
    private Move selectedMove;
    private boolean allowAnyMove;
    private boolean wonStatus ;

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
                // MIN
                score = (moveScore < score) ? moveScore : score;
            } else if (turn == CheckersGame.BLACK) {
                // MAX
                score = (moveScore > score) ? moveScore : score;
            }
        }
        return score;
    }

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
            // easy CPU chooses move randomly
            int num = (int)(moves.length * Math.random());
            selectedMove = moves[num];
        }
        else if (difficulty == 1)
        {
            // medium CPU looks for most captures or kings
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
                    //selectedMove = option;
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
            // sleep on easy/medium
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
                // player wins
                myActivity.statusText.setText("You won!");
                wonStatus = true;

                myActivity.setWinStatus(wonStatus, myDifficulty);
            }
        }
    }
}
