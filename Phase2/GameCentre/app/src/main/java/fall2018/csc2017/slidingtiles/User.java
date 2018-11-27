package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

import fall2018.csc2017.slidingtiles.draughts.game.CheckersGame;

/**
 * A GameCentre user
 */
public class User implements Serializable {
    private String username;
    private String password;
    private BoardManager boardManager; // a slidingtiles save
    private CheckersGame checkersGame; // a checkers save

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BoardManager getBoardManager(){
        return boardManager;
    }

    public void setBoardManager(BoardManager boardManager){
        this.boardManager = boardManager;
    }

    public CheckersGame getCheckersGame() {
        return checkersGame;
    }

    public void setCheckersGame(CheckersGame checkersGame) {
        this.checkersGame = checkersGame;
    }

    /**
     * Return true iff this and obj are equivalent
     * @param obj an object being compared to this
     * @return true iff this and obj are equivalent
     */
    @Override
    public boolean equals(Object obj){
        return (this.getClass()).equals(obj.getClass())
                && (((User)obj).getUsername()).equals(username);
    }

    /**
     * Return a string representation of this
     * @return a string representation of this
     */
    @Override
    public String toString() {
        return username;
    }
}
