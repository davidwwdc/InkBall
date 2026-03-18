package inkball;

import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level {
    private char[][] gameBoard;
    private boolean gameOver;
    private int time;
    private String layout;
    private int id;
    private float score;
    private static final int CELLSIZE = 32;
    private float spawn_interval;
    private float score_increase_modifier;
    private float score_decrease_modifier;
    private ArrayList<Ball> balls; //balls active on screen
    private ArrayList<Ball> incomingBalls; //balls about the spawn
    private ArrayList<ArrayList<PVector>> lines;
    private ArrayList<PVector> currentLine;
    private ArrayList<Hole> holes;
    private ArrayList<PVector> spawnPoints;

    public Level(int id) {
        this.gameOver = false;
        this.id = id;
        this.score = 0;
        this.incomingBalls = new ArrayList<>();
        holes = new ArrayList<>(); //Holes
        balls = new ArrayList<>(); //Balls
        spawnPoints = new ArrayList<>();
        lines = new ArrayList<>();
        currentLine = new ArrayList<>();
    }

    public ArrayList<ArrayList<PVector>> getLines() {
        return lines;
    }

    public void setLines(ArrayList<ArrayList<PVector>> lines) {
        this.lines = lines;
    }

    public float getScore_increase_modifier() {
        return score_increase_modifier;
    }

    public void setScore_increase_modifier(float score_increase_modifier) {
        this.score_increase_modifier = score_increase_modifier;
    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }

    public void setHoles(ArrayList<Hole> holes) {
        this.holes = holes;
    }

    public ArrayList<PVector> getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(ArrayList<PVector> currentLine) {
        this.currentLine = currentLine;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public ArrayList<PVector> getSpawnPoints() {
        return spawnPoints;
    }

    public void setSpawnPoints(ArrayList<PVector> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public ArrayList<Ball> getIncomingBalls() {
        return incomingBalls;
    }

    public void setIncomingBalls(ArrayList<Ball> incomingBalls) {
        this.incomingBalls = incomingBalls;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[][] getGameBoard() {
        return this.gameBoard;
    }

    public void setGameBoard(char[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public float getScore_decrease_modifier() {
        return score_decrease_modifier;
    }

    public void setScore_decrease_modifier(float score_decrease_modifier) {
        this.score_decrease_modifier = score_decrease_modifier;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getSpawn_interval() {
        return spawn_interval;
    }

    public void setSpawn_interval(float spawn_interval) {
        this.spawn_interval = spawn_interval;
    }

    public void readConfig(JSONObject config, int level){
        JSONArray levels = config.getJSONArray("levels");
        JSONObject levelObject = levels.getJSONObject(level-1);
        this.time = levelObject.getInt("time");
        this.layout = "./"+levelObject.getString("layout");
        this.spawn_interval = levelObject.getInt("spawn_interval");
        this.score_increase_modifier = levelObject.getFloat("score_increase_from_hole_capture_modifier");
        this.score_decrease_modifier = levelObject.getFloat("score_decrease_from_wrong_hole_modifier");
        JSONArray ballsArray = levelObject.getJSONArray("balls");
        for (int i = 0; i < ballsArray.size(); i++) {
            String ballColorString = ballsArray.getString(i);
            if(ballColorString.equals("grey")){
                Ball ball = new Ball(20+i*CELLSIZE,20,0,0,'0');
                incomingBalls.add(ball);
            }
            else if(ballColorString.equals("orange")){
                Ball ball = new Ball(20+i*CELLSIZE,20,0,0,'1');
                incomingBalls.add(ball);
            }
            else if(ballColorString.equals("blue")){
                Ball ball = new Ball(20+i*CELLSIZE,20,0,0,'2');
                incomingBalls.add(ball);
            }
            else if(ballColorString.equals("green")){
                Ball ball = new Ball(20+i*CELLSIZE,20,0,0,'3');
                incomingBalls.add(ball);
            }
            else if(ballColorString.equals("yellow")){
                Ball ball = new Ball(20+i*CELLSIZE,20,0,0,'4');
                incomingBalls.add(ball);
            }
        }
    }

    public void readGameBoardFromFile(String path){
        ArrayList<String> linesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                linesList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] stringArray = linesList.toArray(new String[0]); //array size = string length
        char[][] charArray = new char[stringArray.length][];

        // Iterate through the stringArray and convert each String to char[]
        for (int i = 0; i < stringArray.length; i++) {
            charArray[i] = stringArray[i].toCharArray();
        }
        this.gameBoard = charArray;
    }


}
