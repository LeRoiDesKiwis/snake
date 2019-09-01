package fr.leroideskiwis.snake.utils;

import java.awt.Point;

public class BackChecker {

    private final Point oldPoint;

    public BackChecker(Point oldPoint){
        this.oldPoint = oldPoint;
    }

    public boolean check(Point newPoint){

        if(newPoint == null) return false;

        if(oldPoint.x != newPoint.x) return Math.abs(newPoint.x - oldPoint.x) != 2;
        return Math.abs(newPoint.y - oldPoint.y) != 2;

    }

}
