package fr.leroideskiwis.snake.utils;

import fr.leroideskiwis.snake.MapSize;

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

    public static Point traverseWall(Point point, MapSize max){

        point = point.getLocation();

        if(point.x < 0) point.x = max.width;
        if(point.x > max.width) point.x = 0;
        if(point.y < 0) point.y = max.height;
        if(point.y > max.height) point.y = 0;

        return point;

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
