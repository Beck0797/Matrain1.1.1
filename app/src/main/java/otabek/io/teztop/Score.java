package otabek.io.teztop;

public class Score {
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


}
