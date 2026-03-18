package inkball;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;

public class Ball {
    private float x;
    private float y;
    private float x_velocity;
    private float y_velocity;
    private char color;
    private float radius;
    public Ball(float x, float y, int x_velocity,int y_velocity, char color) {
        this.x = x;
        this.y = y;
        this.x_velocity = x_velocity;
        this.y_velocity = y_velocity;
        this.color = color;
        this.radius = 12;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public float getY_velocity() {
        return y_velocity;
    }

    public void setY_velocity(float y_velocity) {
        this.y_velocity = y_velocity;
    }

    public float getX_velocity() {
        return x_velocity;
    }

    public void setX_velocity(float x_velocity) {
        this.x_velocity = x_velocity;
    }

    public void setColor(char color) {
        this.color = color;
    }

    private void changeColor(char color) {
        this.color = color;
    }

    private void checkCurrentLineCollision(ArrayList<PVector> line){
        PVector ballCur = new PVector(this.x+this.radius, this.y+this.radius); //ball current position
        PVector ballNext = new PVector(this.x+this.radius+this.x_velocity, this.y+this.radius+this.y_velocity); //ball next position
        for (int i = 0; i < line.size()-1; i++) {
            PVector cur_p = line.get(i);
            PVector next_p = line.get(i + 1);
            if(ballNext.dist(cur_p) + ballNext.dist(next_p) < cur_p.dist(next_p) + this.radius) {
                //calculate new velocity
                float dx = cur_p.x - next_p.x;
                float dy = cur_p.y - next_p.y;
                float n1_x = (float)(-dy/Math.sqrt(dx*dx+dy*dy));
                float n1_y = (float)(dx/Math.sqrt(dx*dx+dy*dy));
                float n2_x = (float)(dy/Math.sqrt(dx*dx+dy*dy));
                float n2_y = (float)(-dx/Math.sqrt(dx*dx+dy*dy));
                PVector p1 = new PVector((cur_p.x+next_p.x)/2+n1_x, (cur_p.y+next_p.y)/2+n1_y);
                PVector p2 = new PVector((cur_p.x+next_p.x)/2+n2_x, (cur_p.y+next_p.y)/2+n2_y);
                PVector n;
                //If ball is closer to point 1, use normal vector 1, otherwise use normal vector 2
                if(ballCur.dist(p1) < ballCur.dist(p2)) {
                    n = new PVector(n1_x, n1_y);
                }
                else{
                    n = new PVector(n2_x, n2_y);
                }
                //dot product v⋅n
                float product = this.x_velocity*n.x+this.y_velocity*n.y;
//                    System.out.println(this.x_velocity+" "+this.y_velocity+" "+n.x+" "+n.y);
                this.x_velocity -= 2*product*n.x;
                this.y_velocity -= 2*product*n.y;
//                this.x_velocity = Math.round(this.x_velocity * 1000) / 1000f;
//                this.y_velocity = Math.round(this.y_velocity * 1000) / 1000f;
//                    System.out.println("product:"+product+" x:"+this.x_velocity+" y:"+this.y_velocity+" velocity:"+(this.x_velocity*this.x_velocity+this.y_velocity*this.y_velocity)); //test
                //empty current line
                line.clear();
                break;
            }
        }
    }

    private void checkLineCollision(ArrayList<ArrayList<PVector>> lines) {
        ArrayList<Integer> removeLineIndexes = new ArrayList<>();
        PVector ballCur = new PVector(this.x+this.radius, this.y+this.radius); //ball current position
        PVector ballNext = new PVector(this.x+this.radius+this.x_velocity, this.y+this.radius+this.y_velocity); //ball next position
        for (int j = 0; j < lines.size(); j++) {
            ArrayList<PVector> line = lines.get(j);
            for (int i = 0; i < line.size()-1; i++) {
                PVector cur_p = line.get(i);
                PVector next_p = line.get(i + 1);
                if(ballNext.dist(cur_p) + ballNext.dist(next_p) < cur_p.dist(next_p) + this.radius) {
                    //calculate new velocity
                    float dx = cur_p.x - next_p.x;
                    float dy = cur_p.y - next_p.y;
                    float n1_x = (float)(-dy/Math.sqrt(dx*dx+dy*dy));
                    float n1_y = (float)(dx/Math.sqrt(dx*dx+dy*dy));
                    float n2_x = (float)(dy/Math.sqrt(dx*dx+dy*dy));
                    float n2_y = (float)(-dx/Math.sqrt(dx*dx+dy*dy));
                    PVector p1 = new PVector((cur_p.x+next_p.x)/2+n1_x, (cur_p.y+next_p.y)/2+n1_y);
                    PVector p2 = new PVector((cur_p.x+next_p.x)/2+n2_x, (cur_p.y+next_p.y)/2+n2_y);
                    PVector n;
                    //If ball is closer to point 1, use normal vector 1, otherwise use normal vector 2
                    if(ballCur.dist(p1) < ballCur.dist(p2)) {
                        n = new PVector(n1_x, n1_y);
                    }
                    else{
                        n = new PVector(n2_x, n2_y);
                    }
                    //dot product v⋅n
                    float product = this.x_velocity*n.x+this.y_velocity*n.y;
//                    System.out.println(this.x_velocity+" "+this.y_velocity+" "+n.x+" "+n.y);
                    this.x_velocity = (this.x_velocity - 2*product*n.x);
                    this.y_velocity = (this.y_velocity - 2*product*n.y);
//                    this.x_velocity = Math.round(this.x_velocity * 1000) / 1000f;
//                    this.y_velocity = Math.round(this.y_velocity * 1000) / 1000f;
//                    System.out.println(" x:"+this.x_velocity+" y:"+this.y_velocity+" velocity:"+(this.x_velocity*this.x_velocity+this.y_velocity*this.y_velocity)); //test
//                    System.out.println("product:"+product+" x:"+this.x_velocity+" y:"+this.y_velocity+" velocity:"+(this.x_velocity*this.x_velocity+this.y_velocity*this.y_velocity)); //test
                    //
                    removeLineIndexes.add(j);
                    break;
                }
            }
        }
        //remove collided lines
        if(removeLineIndexes.size() > 0) {
            for (int i = removeLineIndexes.size() - 1; i >= 0; i--) {
                int index = removeLineIndexes.get(i);
                lines.remove(index);  // Remove by index
            }
        }
    }

    private boolean checkBoardCollision(Level currentLevel){
        float center_y_co = this.y+this.radius+this.y_velocity;
        float center_x_co = this.x+this.radius+this.x_velocity;
        int left_y = (int)((center_y_co)/32) - 2;
        int left_x = (int)((center_x_co - this.radius)/32);
        int right_y = (int)((center_y_co)/32) - 2;
        int right_x = (int)((center_x_co + this.radius)/32);
        int top_y = (int)((center_y_co - this.radius)/32) - 2;
        int top_x = (int)((center_x_co)/32);
        int bottom_y = (int)((center_y_co + this.radius)/32) - 2;
        int bottom_x = (int)((center_x_co)/32);
        if(left_x >= 0 && left_x < 18 && left_y >=0 && left_y < 18){
            char board_value = currentLevel.getGameBoard()[left_y][left_x];
           if(Arrays.asList('0', '1', '2', '3', '4','X').contains(board_value)){
                if(board_value != 'X'){
                    this.changeColor(currentLevel.getGameBoard()[left_y][left_x]);
                }
                this.x_velocity = -this.x_velocity;
                //corner collision detection
                int new_left_y = (int)((this.y+this.radius+this.y_velocity)/32) - 2;
                int new_left_x = (int)((this.x+this.x_velocity)/32);
                if(currentLevel.getGameBoard()[new_left_y][new_left_x] == board_value){
                    this.y_velocity = -this.y_velocity;
                }
                return true;
            }
        }
        if(right_x >= 0 && right_x < 18 && right_y >=0 && right_y < 18){
            char board_value = currentLevel.getGameBoard()[right_y][right_x];
            if(Arrays.asList('0', '1', '2', '3', '4','X').contains(board_value)){
                if(board_value != 'X'){
                    this.changeColor(currentLevel.getGameBoard()[right_y][right_x]);
                }
                this.x_velocity = -this.x_velocity;
                //corner collision detection
                int new_right_y = (int)((this.y+this.radius+this.y_velocity)/32) - 2;
                int new_right_x = (int)((this.x+2*this.radius+this.x_velocity)/32);
                if(currentLevel.getGameBoard()[new_right_y][new_right_x] == board_value){
                    this.y_velocity = -this.y_velocity;
                }
                return true;
            }
        }
        if(top_x >= 0 && top_x < 18 && top_y >=0 && top_y < 18){
            char board_value = currentLevel.getGameBoard()[top_y][top_x];
            if(Arrays.asList('0', '1', '2', '3', '4','X').contains(board_value)){
                if(board_value != 'X'){
                    this.changeColor(currentLevel.getGameBoard()[top_y][top_x]);
                }
                this.y_velocity = -this.y_velocity;
                //corner collision detection
                int new_top_y = (int)((this.y+this.y_velocity)/32) - 2;
                int new_top_x = (int)((this.x+this.radius+this.x_velocity)/32);
                if(currentLevel.getGameBoard()[new_top_y][new_top_x] == board_value){
                    this.x_velocity = -this.x_velocity;
                }
                return true;
            }
        }
        if(bottom_x >= 0 && bottom_x < 18 && bottom_y >=0 && bottom_y < 18){
            char board_value = currentLevel.getGameBoard()[bottom_y][bottom_x];
            if(Arrays.asList('0', '1', '2', '3', '4','X').contains(board_value)){
                if(board_value != 'X'){
                    this.changeColor(currentLevel.getGameBoard()[bottom_y][bottom_x]);
                }
                this.y_velocity = -this.y_velocity;
                //corner collision detection
                int new_bottom_y = (int)((this.y+2*this.radius+this.y_velocity)/32) - 2;
                int new_bottom_x = (int)((this.x+this.radius+this.x_velocity)/32);
                if(currentLevel.getGameBoard()[new_bottom_y][new_bottom_x] == board_value){
                    this.x_velocity = -this.x_velocity;
                }
                return true;
            }
        }
        return false;
    }

    public void move(Level currentLevel) {
        ArrayList<ArrayList<PVector>> lines = currentLevel.getLines();
        ArrayList<PVector> currentLine = currentLevel.getCurrentLine();
        checkCurrentLineCollision(currentLine);
        checkLineCollision(lines);
        boolean collision = checkBoardCollision(currentLevel);
        checkCurrentLineCollision(currentLine);
            x += x_velocity;
            y += y_velocity;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public char getColor(){
        return color;
    }
}
