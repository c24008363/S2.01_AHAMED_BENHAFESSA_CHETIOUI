package jeu.personnages;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import jeu.terrains.Terrain;
import jeu.terrains.Tile;

import java.util.Set;

public abstract class Character2 {
    private Rectangle rectangle;
    private double x;
    private double y;
    private double width;
    private double height;
    private double speed;

    public Character2(double x, double y, double width, double height, double speed){
        this.rectangle = new Rectangle(x, y, width, height);
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public void update(double deltaTime, Set<KeyCode> activeKeys, Terrain map){
        double dx = 0;
        double dy = 0;

        if (activeKeys.contains(getKeyUp())) dy -= speed * deltaTime;
        if (activeKeys.contains(getKeyDown())) dy += speed * deltaTime;
        if (activeKeys.contains(getKeyRight())) dx += speed * deltaTime;
        if (activeKeys.contains(getKeyLeft())) dx -= speed * deltaTime;

        double newX = x + dx;
        double newY = y + dy;

        // Rectangle temporaire pour tester la collision
        Rectangle future = new Rectangle(newX, newY, getRectangle().getWidth(), getRectangle().getHeight());

        if (!collidesWithSolidTile(future, map)) {
            x = newX;
            y = newY;
            updateRectangle();
        }
    }

    private boolean collidesWithSolidTile(Rectangle futureRect, Terrain terrain) {
        for (Tile[] row : terrain.getGrid()) {
            for (Tile tile : row) {
                if (tile.isSolid() && futureRect.getBoundsInParent().intersects(tile.getView().getBoundsInParent())) {
                    return true;
                }
            }
        }
        return false;
    }


    private void updateRectangle(){
        rectangle.setX(x);
        rectangle.setY(y);
    }

    public abstract KeyCode getKeyUp();
    public abstract KeyCode getKeyDown();
    public abstract KeyCode getKeyRight();
    public abstract KeyCode getKeyLeft();
}
