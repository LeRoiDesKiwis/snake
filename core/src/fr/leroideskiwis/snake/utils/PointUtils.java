package fr.leroideskiwis.snake.utils;

import java.awt.Point;
import java.util.List;

public class PointUtils {

    public static Point copyAndMove(Point point, int x, int y){
        Point tmpPoint = point.getLocation();
        tmpPoint.move(point.x+x, point.y+y);
        return tmpPoint;
    }

    public static boolean isInBorder(Point point, int maxX, int maxY){

        return point.x < maxX && point.y < maxY && point.y >= 0 && point.x >= 0;

    }

    public static Point getRandomPosition(int maxX, int maxY){
        return new Point((int)(Math.random()*maxX), (int)(Math.random()*maxY));
    }

    public static Point getRandomPositionExclude(int maxX, int maxY, List<Point> excluded){

        Point point = getRandomPosition(maxX, maxY);

        while(excluded.contains(point)){
            point = getRandomPosition(maxX, maxY);
        }

        return point;

    }

}
