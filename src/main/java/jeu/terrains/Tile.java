package jeu.terrains;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tile {
    private final TileType type;
    private final Rectangle view;

    private Image imageDestructibleWall = new Image(getClass().getResourceAsStream("/UI/002-destructible_wall.png"));
    private Image imageWall = new Image(getClass().getResourceAsStream(("/UI/001-durable_wall.png")));

    public Tile(TileType type, double size, double x, double y) {
        this.type = type;
        this.view = new Rectangle(size, size);
        this.view.setX(x);
        this.view.setY(y);

        switch (type){
            case WALL -> view.setFill(new ImagePattern(imageWall));
            case DESTRUCTIBLE -> view.setFill(new ImagePattern(imageDestructibleWall));
            case EMPTY -> view.setFill(Color.LIGHTGREEN);
        }
    }

    public TileType getType() {
        return type;
    }

    public Rectangle getView() {
        return view;
    }

    public boolean isSolid(){
        return type == TileType.WALL || type == TileType.DESTRUCTIBLE;
    }
}
