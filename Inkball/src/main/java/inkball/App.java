package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.
    PImage ball0;
    PImage ball1;
    PImage ball2;
    PImage ball3;
    PImage ball4;
    PImage entrypoint;
    PImage hole0;
    PImage hole1;
    PImage hole2;
    PImage hole3;
    PImage hole4;
    PImage tile;
    PImage wall0;
    PImage wall1;
    PImage wall2;
    PImage wall3;
    PImage wall4;
    JSONObject config;
    Level currentLevel;
    ArrayList<Level> levels;
    ArrayList<Color> colors;
    public static float currentLevelScore = 0;
    public static float totalSavedScore = 0;
    public static int frameCount = 0;
    public static float spawn_interval = 0;
    public static float time_remaining = 0;
    //some game status flags
    Boolean isPaused;
    public static boolean timeUp = false;
    public static boolean gameEnded = false;




    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }


    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		// the image is loaded from relative path: "src/main/resources/inkball/..."
            ball0 = loadImage("./src/main/resources/inkball/ball0.png");
            ball1 = loadImage("./src/main/resources/inkball/ball1.png");
            ball2 = loadImage("./src/main/resources/inkball/ball2.png");
            ball3 = loadImage("./src/main/resources/inkball/ball3.png");
            ball4 = loadImage("./src/main/resources/inkball/ball4.png");
            entrypoint = loadImage("./src/main/resources/inkball/entrypoint.png");
            hole0 = loadImage("./src/main/resources/inkball/hole0.png");
            hole1 = loadImage("./src/main/resources/inkball/hole1.png");
            hole2 = loadImage("./src/main/resources/inkball/hole2.png");
            hole3 = loadImage("./src/main/resources/inkball/hole3.png");
            hole4 = loadImage("./src/main/resources/inkball/hole4.png");
            tile = loadImage("./src/main/resources/inkball/tile.png");
            wall0 = loadImage("./src/main/resources/inkball/wall0.png");
            wall1 = loadImage("./src/main/resources/inkball/wall1.png");
            wall2 = loadImage("./src/main/resources/inkball/wall2.png");
            wall3 = loadImage("./src/main/resources/inkball/wall3.png");
            wall4 = loadImage("./src/main/resources/inkball/wall4.png");
            isPaused = false;

        //read config.json
        config = loadJSONObject("./config.json");
        JSONArray levelArray = config.getJSONArray("levels");


        //read levels from file
        levels = new ArrayList<>();
        for(int i=1; i <= levelArray.size(); i++){
            Level level = new Level(i);
            level.readConfig(config,i);
            level.readGameBoardFromFile(level.getLayout());
            levels.add(level);
        }

        //initialize colors
        JSONObject scoreIncrease = config.getJSONObject("score_increase_from_hole_capture");
        JSONObject scoreDecrease = config.getJSONObject("score_decrease_from_wrong_hole");
        colors = new ArrayList<>(); //colors
        Color grey = new Color("grey",scoreIncrease.getInt("grey"),scoreDecrease.getInt("grey"));
        colors.add(grey);
        Color orange = new Color("orange",scoreIncrease.getInt("orange"),scoreDecrease.getInt("orange"));
        colors.add(orange);
        Color blue = new Color("blue",scoreIncrease.getInt("blue"),scoreDecrease.getInt("blue"));
        colors.add(blue);
        Color green = new Color("green",scoreIncrease.getInt("green"),scoreDecrease.getInt("green"));
        colors.add(green);
        Color yellow = new Color("yellow",scoreIncrease.getInt("yellow"),scoreDecrease.getInt("yellow"));
        colors.add(yellow);
    }

    public void refreshLevel(int levelid){
        frameCount = 0;
        spawn_interval = 0;
        time_remaining = 0;
        timeUp = false;
        currentLevelScore = 0;
        //read a level from config.json again
        config = loadJSONObject("./config.json");
        Level newLevel = new Level(levelid);
        newLevel.readConfig(config,levelid);
        newLevel.readGameBoardFromFile(newLevel.getLayout());
        levels.set(levelid-1,newLevel);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if(!timeUp&&!gameEnded){
            if (event.getKeyCode() == ' '){
                isPaused = !isPaused;
            }
        }
        if (event.getKeyCode() == 'r' || event.getKeyCode() == 'R'){
            if(!gameEnded){
                refreshLevel(currentLevel.getId());
            }
            else{ //if game ended, refresh all levels to start over
                for(int i=1; i <= levels.size(); i++){
                    refreshLevel(i);
                }
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // create a new player-drawn line object
    }

	@Override
    public void mouseDragged(MouseEvent e) {
        if(!timeUp){
            // add line segments to player-drawn line object if left mouse button is held
            currentLevel.getCurrentLine().add(new PVector(mouseX, mouseY));
            // remove player-drawn line object if right mouse button is held
            // and mouse position collides with the line
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!timeUp){
            if (!currentLevel.getCurrentLine().isEmpty()) {
                currentLevel.getLines().add(currentLevel.getCurrentLine());
                currentLevel.setCurrentLine(new ArrayList<PVector>()); // Clear current line
            }
        }
    }

    public int randomVelocity(){
        return Math.random() < 0.5 ? 2 : -2;
    }

    public void drawGameBoard(Level currentLevel){
        char[][] board = currentLevel.getGameBoard();
        for(int y = 2; y < BOARD_HEIGHT; y++) {
            for(int x = 0; x < BOARD_WIDTH; x++) {
                if(board[y-2][x] == 'X') {
                    image(wall0,x*CELLSIZE,y*CELLSIZE);
                }
                else if(board[y-2][x] == 'S') {
                    image(entrypoint,x*CELLSIZE,y*CELLSIZE);
                    PVector entry = new PVector(x*CELLSIZE,y*CELLSIZE);
                    currentLevel.getSpawnPoints().add(entry);
                    board[y-2][x] = ' ';
                }
                else if(board[y-2][x] == 'H') {
                    currentLevel.getHoles().add(new Hole(x*CELLSIZE,y*CELLSIZE,board[y-2][x+1]));
                    image(tile,x*CELLSIZE,y*CELLSIZE);
                    board[y-2][x] = ' ';
                    board[y-2][x+1] = ' ';
                }
                else if(board[y-2][x] == 'B') {
                    currentLevel.getBalls().add(new Ball(x*CELLSIZE,y*CELLSIZE,randomVelocity(),randomVelocity(),board[y-2][x+1]));
                    image(tile,x*CELLSIZE,y*CELLSIZE);
                    board[y-2][x] = ' ';
                    board[y-2][x+1] = ' ';
                }
                else if(board[y-2][x] == '0') {
                    if(x == 0){
                        image(wall0, x * CELLSIZE, y * CELLSIZE);
                    }
                    else if(board[y-2][x-1] != 'H') {
                        if (board[y-2][x-1] == 'B') {
                            image(tile, x * CELLSIZE, y * CELLSIZE);
                        } else {
                            image(wall0, x * CELLSIZE, y * CELLSIZE);
                        }
                    }
                }
                else if(board[y-2][x] == '1') {
                    if(x == 0){
                        image(wall1, x * CELLSIZE, y * CELLSIZE);
                    }
                    else if(board[y-2][x-1] != 'H') {
                        if (board[y-2][x-1] == 'B') {
                            image(tile, x * CELLSIZE, y * CELLSIZE);
                        } else {
                            image(wall1, x * CELLSIZE, y * CELLSIZE);
                        }
                    }
                }
                else if(board[y-2][x] == '2') {
                    if(x == 0){
                        image(wall2, x * CELLSIZE, y * CELLSIZE);
                    }
                    else if(board[y-2][x-1] != 'H') {
                        if (board[y-2][x-1] == 'B') {
                            image(tile, x * CELLSIZE, y * CELLSIZE);
                        } else {
                            image(wall2, x * CELLSIZE, y * CELLSIZE);
                        }
                    }
                }
                else if(board[y-2][x] == '3') {
                    if(x == 0){
                        image(wall3, x * CELLSIZE, y * CELLSIZE);
                    }
                    else if(board[y-2][x-1] != 'H') {
                        if (board[y-2][x-1] == 'B') {
                            image(tile, x * CELLSIZE, y * CELLSIZE);
                        } else {
                            image(wall3, x * CELLSIZE, y * CELLSIZE);
                        }
                    }
                }
                else if(board[y-2][x] == '4') {
                    if(x == 0){
                        image(wall4, x * CELLSIZE, y * CELLSIZE);
                    }
                    else if(board[y-2][x-1] != 'H') {
                        if (board[y-2][x-1] == 'B') {
                            image(tile, x * CELLSIZE, y * CELLSIZE);
                        } else {
                            image(wall4, x * CELLSIZE, y * CELLSIZE);
                        }
                    }
                }
                else if(board[y-2][x] == ' ' && board[y-3][x] != 'H' && board[y-3][x-1] != 'H') {
                    image(tile,x*CELLSIZE,y*CELLSIZE);
                }
            }
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        background(205); //clean up the background

        //display level
        for(Level level : levels) {
            if(!level.isGameOver()){
                currentLevel = level;
                break;
            }
        }

        //check game ended (if all levels gameOver = true)
         if(!gameEnded){
            boolean gameOver = true;
            for(Level level : levels) {
                gameOver = gameOver&&level.isGameOver();
            }
            if(gameOver){
                gameEnded = true;
            }
        }
         else{
             currentLevel = levels.get(levels.size()-1);
         }


        //check game over (set this level gameOver = true, next frame will load the next level)
        if(currentLevel.getBalls().size() == 0 && currentLevel.getIncomingBalls().size() == 0){
            currentLevelScore += time_remaining/0.067;
            totalSavedScore += currentLevelScore;
            refreshLevel(currentLevel.getId());
            levels.get(currentLevel.getId()-1).setGameOver(true);
            return;
        }

        //display incoming balls
        fill(0);
        rect(20,20,30*5,25);
        int ballCount = 0;
        if (currentLevel.getIncomingBalls().size() < 5){
            ballCount = currentLevel.getIncomingBalls().size();
        }
        else{
            ballCount = 5;
        }
        for(int i = 0; i < ballCount; i++){
            Ball b = currentLevel.getIncomingBalls().get(i);
            if(b.getColor() == '0'){
                image(ball0,b.getX(),b.getY());
            }
            else if(b.getColor() == '1'){
                image(ball1,b.getX(),b.getY());
            }
            else if(b.getColor() == '2'){
                image(ball2,b.getX(),b.getY());
            }
            else if(b.getColor() == '3'){
                image(ball3,b.getX(),b.getY());
            }
            else if(b.getColor() == '4'){
                image(ball4,b.getX(),b.getY());
            }
        }

        //draw game board
        drawGameBoard(currentLevel);

        //count spawn_interval and time_remaining by frame
        if(!isPaused&&!timeUp&&!gameEnded){
            if(!currentLevel.getIncomingBalls().isEmpty()) {
                if(frameCount%3 == 0){ //update every 3 frames/every 0.1 second (30fps)
                    if(frameCount == 0){
                        spawn_interval  = currentLevel.getSpawn_interval();
                    }
                    else if(spawn_interval - 0.1 > 0){
                        spawn_interval -= 0.1;
                    }
                    else{
                        spawn_interval  = currentLevel.getSpawn_interval();
                        //spawn a ball
                        int spawnIndex = new Random().nextInt(currentLevel.getSpawnPoints().size());
                        PVector spawnPoint = currentLevel.getSpawnPoints().get(spawnIndex);
                        Ball b = currentLevel.getIncomingBalls().get(0);
                        currentLevel.getIncomingBalls().remove(0);
                        currentLevel.getBalls().add(new Ball(spawnPoint.x,spawnPoint.y,randomVelocity(),randomVelocity(),b.getColor()));
                        for(Ball ib:currentLevel.getIncomingBalls()){
                            ib.setX(ib.getX()-32);
                        }
                    }
                }
            }
            else{
                spawn_interval = 0;
            }
            if(frameCount % 30 == 0){ //update every 30 frames/every 1 second (30fps)
                if(frameCount == 0){
                    time_remaining = currentLevel.getTime();
                }
                else if(time_remaining > 1){
                    time_remaining -= 1;
                }
                else if(time_remaining == 1){ //time's up
                    time_remaining -= 1;
                    timeUp = true;
                }
            }
            frameCount++;
        }
        //display spawn interval
        textSize(24);
        text(nf(spawn_interval,0,1), 180, 45);

        //display currentLevelScore and timer
        fill(0,0,0); //black
        textSize(24);
        text("Score:", CELLSIZE*13-5, 30);
        text((int) (totalSavedScore+currentLevelScore), CELLSIZE*15+10, 30);
        text("Time:", CELLSIZE*13, 55);
        text(nf(time_remaining,0,0),CELLSIZE*15+10,55);

        //draw currentLevel.getHoles()
        for (Hole hole: currentLevel.getHoles()){
            if(hole.getColor() == '0'){
                image(hole0,hole.getX(),hole.getY());
            }
            else if(hole.getColor() == '1'){
                image(hole1,hole.getX(),hole.getY());
            }
            else if(hole.getColor() == '2'){
                image(hole2,hole.getX(),hole.getY());
            }
            else if(hole.getColor() == '3'){
                image(hole3,hole.getX(),hole.getY());
            }
            else if(hole.getColor() == '4'){
                image(hole4,hole.getX(),hole.getY());
            }
        }

        //draw entry points
        for(PVector s: currentLevel.getSpawnPoints()){
            image(entrypoint,s.x,s.y);
        }

        //draw balls
        for (Ball b: currentLevel.getBalls()){
            if(b.getColor() == '0'){
                image(ball0,b.getX(),b.getY());
            }
            else if(b.getColor() == '1'){
                image(ball1,b.getX(),b.getY());
            }
            else if(b.getColor() == '2'){
                image(ball2,b.getX(),b.getY());
            }
            else if(b.getColor() == '3'){
                image(ball3,b.getX(),b.getY());
            }
            else if(b.getColor() == '4'){
                image(ball4,b.getX(),b.getY());
            }
            if(!isPaused&&!timeUp&!gameEnded) {
                b.move(currentLevel);
            }
        }

        //check if balls are in hole
        ArrayList<Integer> ballRemove = new ArrayList<>();
        for(int i=0; i < currentLevel.getBalls().size(); i++){
            Ball ball = currentLevel.getBalls().get(i);
            PVector ballCenter = new PVector(ball.getX()+ball.getRadius(),ball.getY()+ball.getRadius());
            for (Hole hole: currentLevel.getHoles()) {
                char holeColor = hole.getColor();
                PVector holeCenter = new PVector(hole.getX()+CELLSIZE,hole.getY()+CELLSIZE);
                if (ballCenter.dist(holeCenter) <= CELLSIZE) {
                    //ball get sucked in
                    if (ball.getColor() == '0') { //grey ball can go in any hole
                        currentLevelScore += colors.get(0).getScoreIncrease() * currentLevel.getScore_increase_modifier();
                        System.out.println(currentLevelScore); //test
                        ballRemove.add(i);
                    }
                    else{
                        if(holeColor == ball.getColor()||holeColor == '0'){ //color match, count as success
                            currentLevelScore += colors.get(Character.getNumericValue(ball.getColor())).getScoreIncrease() * currentLevel.getScore_increase_modifier();
                            System.out.println(currentLevelScore); //test
                            ballRemove.add(i);
                        }
                        else{
                            currentLevelScore -= colors.get(Character.getNumericValue(ball.getColor())).getScoreDecrease() * currentLevel.getScore_decrease_modifier();
                            System.out.println(currentLevelScore); //test
                            ballRemove.add(i);
                            if(currentLevel.getIncomingBalls().size() > 0){
                                Ball lastIncomingBall = currentLevel.getIncomingBalls().get(currentLevel.getIncomingBalls().size() - 1);
                                currentLevel.getIncomingBalls().add(new Ball(lastIncomingBall.getX()+CELLSIZE,lastIncomingBall.getY(),0,0,ball.getColor()));
                            }
                            else{
                                currentLevel.getIncomingBalls().add(new Ball(20,20,0,0,ball.getColor()));
                            }

                        }
                    }
                    break;
                }
            }
        }
        if(ballRemove.size() > 0){
            Collections.sort(ballRemove);
            Collections.reverse(ballRemove);
            //remove balls that get sucked into currentLevel.getHoles()
            for(int i=ballRemove.size()-1; i >= 0; i--){
                System.out.println(currentLevel.getBalls().size()); //test
                currentLevel.getBalls().remove(ballRemove.get(i).intValue());
            }
        }

        //draw existing currentLevel.getLines()
        stroke(0); // Set stroke color to black
        strokeWeight(5);
        for (ArrayList<PVector> line : currentLevel.getLines()) {
            noFill();
            beginShape();
            for (PVector p : line) {
                vertex(p.x, p.y);
            }
            endShape();
        }
        //draw current line as it is being drawn
        if (!currentLevel.getCurrentLine().isEmpty()) {
            stroke(0);  // Black stroke for the line being drawn
            noFill();
            beginShape();
            for (PVector p : currentLevel.getCurrentLine()) {
                vertex(p.x, p.y);
            }
            endShape();
        }

        //special game states
        if(isPaused){
            fill(0,0,255);
            textSize(20);
            text("***PAUSED***", 230, 40);
        }
        else if(timeUp){
            fill(255,0,0);
            textSize(20);
            text("==TIME'S UP==", 230, 40);
        }
        else if(gameEnded){
            fill(0,255,0);
            textSize(20);
            text("===ENDED===", 230, 40);
        }
        
		//----------------------------------
        //----------------------------------
		//display game end message

    }


    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}
