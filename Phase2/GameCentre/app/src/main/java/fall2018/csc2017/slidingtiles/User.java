package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * A GameCentre user
 */
public class User implements Serializable {
    private String username;
    private String password;
    private BoardManager boardManager;

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

    @Override
    public boolean equals(Object obj){
        return (this.getClass()).equals(obj.getClass())
                && (((User)obj).getUsername()).equals(username);
    }

    @Override
    public String toString() {
        return username;
    }
}
