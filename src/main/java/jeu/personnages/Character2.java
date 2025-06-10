package jeu.personnages;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import jeu.terrains.Terrain;
import jeu.terrains.Tile;
import jeu.terrains.TileType;

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

        move(dx, dy, map);

    }

    public void move(double dx, double dy, Terrain terrain) {
        double newX = getRectangle().getX() + dx;
        double newY = getRectangle().getY(); // pour le test X
        if (!isBlocked(newX, newY, terrain)) {
            getRectangle().setX(newX);
        }

        newX = getRectangle().getX(); // position X après test
        newY = getRectangle().getY() + dy;
        if (!isBlocked(newX, newY, terrain)) {
            getRectangle().setY(newY);
        }
    }

    private boolean isBlocked(double x, double y, Terrain terrain) {
        double width = getRectangle().getWidth();
        double height = getRectangle().getHeight();

        // Tester les 4 coins du rectangle
        return isTileBlocked(x, y, terrain) ||                    // coin haut-gauche
                isTileBlocked(x + width, y, terrain) ||            // coin haut-droit
                isTileBlocked(x, y + height, terrain) ||           // coin bas-gauche
                isTileBlocked(x + width, y + height, terrain);     // coin bas-droit
    }

    private boolean isTileBlocked(double x, double y, Terrain terrain) {
        Tile tile = terrain.getTileAt(x, y);
        if (tile == null) return true;
        return tile.getType() == TileType.WALL || tile.getType() == TileType.DESTRUCTIBLE;
    }

//    private void updateRectangle(){
//        rectangle.setX(x);
//        rectangle.setY(y);
//    }

    public abstract KeyCode getKeyUp();
    public abstract KeyCode getKeyDown();
    public abstract KeyCode getKeyRight();
    public abstract KeyCode getKeyLeft();
}
