package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The data for a complete checker board state.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class CheckerBoard implements Serializable {

    /**
     * The created checker board.
     */
    private Piece board[][];

    /**
     * Returns whether a coordinate is within 8x8 dimensions and is an odd-square.
     * @param x the coordinate's row
     * @param y the coordinate's column
     * @return whether a coordinate is within 8x8 dimensions and is an odd-square.
     */
    public boolean isGameSquare(int x, int y) {
        return (x >= 0 && y >= 0 && x < 8 && y < 8 && (x + y) % 2 > 0);
    }

    /**
     * Returns whether a position is within 8x8 dimensions and is an odd-square.
     *
     * @param pos a position on the checkers board.
     * @return whether a position is within 8x8 dimensions and is an odd-square.
     */
    public boolean isGameSquare(Position pos) {
        return isGameSquare(pos.x, pos.y);
    }


    /**
     * A Position for Red Checkers.
     */
    private Position[] RED_DIRECTIONS = new Position[]{new Position(-1, 1), new Position(1, 1)};

    /**
     * A Position for Black Checkers.
     */
    private Position[] BLACK_DIRECTIONS = new Position[]{new Position(-1, -1), new Position(1, -1)};

    /**
     * Positions for Kinged Checker Pieces.
     */
    private Position[] BOTH_DIRECTIONS = new Position[]{new Position(-1, 1), new Position(1, 1), new Position(-1, -1), new Position(1, -1)};

    /**
     * Return the neighbouring available moves for the selected checker.
     *
     * @param color the selected checker's colour.
     * @param king whether the selected checker is a king.
     * @return the neighbouring available moves for the selected checker.
     */
    private Position[] getNeighbors(int color, boolean king) {
        if (king) {
            return BOTH_DIRECTIONS;
        } else if (color == CheckersGame.RED) {
            return RED_DIRECTIONS;
        } else if (color == CheckersGame.BLACK) {
            return BLACK_DIRECTIONS;
        } else {
            return BOTH_DIRECTIONS;
        }
    }

    /**
     * Creates a new checker board.
     *
     * @param checkersGame a new checker game.
     */
    public CheckerBoard(CheckersGame checkersGame) {
        board = new Piece[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int side = (y < 3) ? CheckersGame.RED : (y > 4) ? CheckersGame.BLACK : 0;
                boolean validSquare = this.isGameSquare(x, y);
                if (side != CheckersGame.NONE && validSquare) {
                    board[x][y] = new Piece(side, false);
                } else {
                    board[x][y] = null;
                }
            }
        }
    }

    /**
     * Creates a checker board with existing positions.
     * @param positions an existing position.
     */
    public CheckerBoard(Integer [][] positions) {
        board = new Piece[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (positions[x][y] > CheckersGame.NONE) {
                    int side = positions[x][y] % CheckersGame.KINGED;
                    boolean kinged = positions[x][y] > CheckersGame.KINGED;
                    board[x][y] = new Piece(side, kinged);
                } else {
                    board[x][y] = null;
                }
            }
        }
    }

    /**
     * Returns positions as an int[][] to be saved.
     *
     * @return positions as an int[][] to be saved.
     */
    public Integer[][] saveBoard() {
        Integer result[][] = new Integer[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null) {
                    Piece piece = board[x][y];
                    result[x][y] = piece.getColor();
                    if (piece.isKing()) {
                        result[x][y] += CheckersGame.KINGED;
                    }
                } else {
                    result[x][y] = CheckersGame.NONE;
                }
            }
        }
        return result;
    }

    /**
     * Returns a piece on the board.
     *
     * @param x the piece's row coordinate
     * @param y the piece's column coordinate.
     * @return a piece on the board.
     */
    public Piece getPiece(int x, int y) {
        return (isGameSquare(x, y) ? board[x][y] : null);
    }

    /**
     * Returns the piece with a given position.
     *
     * @param pos a position on the checkers board.
     * @return the piece with a given position.
     */
    public Piece getPiece(Position pos) {
        return getPiece(pos.x, pos.y);
    }

    /**
     * Returns immediate possible captures for a piece in play.
     *
     * @param start the piece's starting position.
     * @param allowAnyMove whether any move is permitted for this checker board game.
     * @return possible captures for a piece in play.
     */
    public ArrayList<Move> getCaptures(Position start, boolean allowAnyMove)
    {
        ArrayList<Move> base = new ArrayList<>();
        Piece piece = getPiece(start);
        int color = piece.getColor();
        boolean isKing = piece.isKing();

        Position[] directions = getNeighbors(color, isKing);
        for (Position dir : directions) {
            Position target = start.plus(dir);
            Position dest = target.plus(dir);
            Piece targetPiece = getPiece(target);
            Piece destPiece = getPiece(dest);

            if (isGameSquare(dest) && destPiece == null &&
                    targetPiece != null &&
                    targetPiece.getColor() != color)
            {
                Move newMove = new Move(start);
                newMove.add(dest);
                base.add(newMove);
            }
        }

        return getCaptures(start, base, allowAnyMove);
    }

    /**
     * Returns longer possible captures for a piece in play.
     *
     * @param start the piece's starting position.
     * @param expand a piece's potential moves and further moves.
     * @param allowAnyMove whether any move is permitted for this checker board game.
     * @return longer possible captures for a piece in play.
     */
    public ArrayList<Move> getCaptures(Position start, ArrayList<Move> expand, boolean allowAnyMove)
    {
        ArrayList<Move> finalCaptures = new ArrayList<>();
        ArrayList<Move> furtherCaptures = new ArrayList<>();

        Piece piece = getPiece(start);
        int color = piece.getColor();
        boolean isKing = piece.isKing();

        for (Move move : expand) {
            Position[] directions = getNeighbors(color, isKing || move.kings);
            Position current = move.end();
            boolean continues = false;
            for (Position dir : directions)
            {
                Position target = current.plus(dir);
                Position dest = target.plus(dir);
                Piece targetPiece = getPiece(target);
                Piece destPiece = getPiece(dest);

                if (isGameSquare(dest) && destPiece == null &&
                        targetPiece != null &&
                        targetPiece.getColor() != color) {
                    boolean valid = true;
                    for (Position captured : move.captures) {
                        if (captured.equals(target)) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        Move newMove = new Move(move);
                        newMove.add(dest);
                        furtherCaptures.add(newMove);
                        continues = true;
                    }
                }
            }

            if (!continues || allowAnyMove) {
                finalCaptures.add(move);
            }
        }

        if (furtherCaptures.size() > 0) {
            furtherCaptures = getCaptures(start, furtherCaptures, allowAnyMove);
        }
        finalCaptures.addAll(furtherCaptures);

        return finalCaptures;
    }

    /**
     * Returns a set of possible moves for a checker piece.
     *
     * @param start a checker piece's start position on a board.
     * @param allowAnyMove whether any move is permitted for this checker board game.
     * @return a set of possible moves for a checker piece.
     */
    public ArrayList<Move> getMoves(Position start, boolean allowAnyMove) {
        Piece piece = getPiece(start);

        ArrayList<Move> immediateMoves = new ArrayList<>();

        Position[] neighbors = getNeighbors(piece.getColor(), piece.isKing());
        for (Position pos : neighbors) {
            Position dest = start.plus(pos);
            Piece destPiece = getPiece(dest);

            if (isGameSquare(dest) && destPiece == null) {
                Move newMove = new Move(start);
                newMove.add(dest);
                immediateMoves.add(newMove);
            }
        }

        ArrayList<Move> captures = getCaptures(start, allowAnyMove);
        immediateMoves.addAll(captures);
        return immediateMoves;
    }

    /**
     * Return the possible moves for the current player
     *
     * @param turn the current player's turn.
     * @param allowAnyMove whether any move is permitted for this checker board game.
     * @return
     */
    public Move[] getMoves(int turn, boolean allowAnyMove) {
        ArrayList<Move> finalMoves;
        ArrayList<Move> potentialMoves = new ArrayList<>();
        ArrayList<Position> startingPositions = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getPiece(x, y);
                if (piece != null && piece.getColor() == turn) {
                    Position start = new Position(x, y);
                    potentialMoves.addAll(
                            getMoves(start, allowAnyMove)
                    );
                }
            }
        }

        finalMoves = potentialMoves;
        if (allowAnyMove == false) {
            boolean areCaptures = false;
            for (Move sequence : potentialMoves) {
                if (sequence.captures.size() > 0) {
                    areCaptures = true;
                    break;
                }
            }
            if (areCaptures) {
                finalMoves = new ArrayList<>();
                for (Move sequence : potentialMoves) {
                    if (sequence.captures.size() > 0) {
                        finalMoves.add(sequence);
                    }
                }
            }
        }

        return finalMoves.toArray(new Move[finalMoves.size()]);
    }

    /**
     * Executes a move sequence onto the checkers board.
     * @param move a given move for a checker.
     */
    public void makeMove(Move move) {
        Position start = move.start();
        Position end = move.end();
        Piece piece = getPiece(start);
        int otherColor = (piece.getColor() == CheckersGame.RED) ? CheckersGame.BLACK : CheckersGame.RED;
        for (Position pos : move.positions) {
            board[pos.x][pos.y] = null;
        }
        for (Position cap : move.captures) {
            board[cap.x][cap.y] = null;
        }
        board[end.x][end.y] = piece;
        if (move.kings) {
            piece.makeKing();
        }
    }

    /**
     * Returns a score value for a player in the checker game.
     *
     * @return a score value for a player in the checker game.
     */
    public int pseudoScore() {
        int score = 0;
        int blackPieces = 0;
        int redPieces = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board[x][y];
                if (piece != null) {
                    int weight = piece.isKing() ? 5 : 2;
                    if (piece.getColor() == CheckersGame.RED) {
                        weight *= -1;
                        redPieces++;
                    } else {
                        blackPieces++;
                    }
                    score += weight;
                }
            }
        }
        if (blackPieces == 0) {
            score = -1000;
        } else if (redPieces == 0) {
            score = 1000;
        }

        return score;
    }
}
