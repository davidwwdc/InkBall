package inkball;
public class Color {
    private String colorName;
    private int scoreIncrease;
    private int scoreDecrease;
    public Color(String colorName, int scoreIncrease, int scoreDecrease) {
        this.colorName = colorName;
        this.scoreIncrease = scoreIncrease;
        this.scoreDecrease = scoreDecrease;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getScoreIncrease() {
        return scoreIncrease;
    }

    public void setScoreIncrease(int scoreIncrease) {
        this.scoreIncrease = scoreIncrease;
    }

    public int getScoreDecrease() {
        return scoreDecrease;
    }

    public void setScoreDecrease(int scoreDecrease) {
        this.scoreDecrease = scoreDecrease;
    }
}
