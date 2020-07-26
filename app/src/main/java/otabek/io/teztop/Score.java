package otabek.io.teztop;

import java.io.Serializable;

public class Score implements Serializable, Comparable {
    String username, level;
    int score;

    public Score() {

    }

    public Score(String username, String level, int score) {
        this.username = username;
        this.level = level;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public int compareTo(Object o) {
        int compareScore = ((Score) o).getScore();

        return compareScore - this.score;
    }
}
