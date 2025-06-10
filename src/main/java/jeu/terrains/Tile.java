package jeu.terrains;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    private final TileType type;
    private final Rectangle view;

    public Tile(TileType type, double size, double x, double y) {
        this.type = type;
        this.view = new Rectangle(size, size);
        this.view.setX(x);
        this.view.setY(y);

        switch (type){
            case WALL -> view.setFill(Color.GRAY);
            case DESTRUCTIBLE -> view.setFill(Color.ORANGE);
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
