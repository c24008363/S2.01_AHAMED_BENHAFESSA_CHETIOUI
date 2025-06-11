package jeu.objets;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import jeu.terrains.Terrain;
import jeu.terrains.Tile;
import jeu.terrains.TileType;

public class Bomb {
    private final Circle view;
    private final int explosionRange = 2;
    private final double tileSize = 40;

    public Bomb(double x, double y, double radius, Pane parent, Terrain map){
        view = new Circle(radius);
        view.setCenterX(x);
        view.setCenterY(y);

        // chargé l'image de la bombe
        Image imageBomb = new Image(getClass().getResourceAsStream("/UI/Bomb-player1.png"));
        view.setFill(new ImagePattern(imageBomb));

        //parent.getChildren().add(view);

        PauseTransition timer = new PauseTransition(Duration.seconds(2));
        timer.setOnFinished(event -> {
            parent.getChildren().remove(view);
            explode(parent, x, y, map);
        });
        timer.play();
    }

    public Circle getView(){
        return view;
    }

    private void explode(Pane parent, double centerX, double centerY, Terrain map){
        int col = (int) (centerX / tileSize);
        int row = (int) (centerY / tileSize);

        addExplosionTile(parent, col, row, map);

        // direction -> haut, bas, gauche, droite
        spreadExplosion(parent, col, row, map, 1, 0); //droite
        spreadExplosion(parent, col, row, map, -1, 0); // gauche
        spreadExplosion(parent, col, row, map, 0, -1); // haut
        spreadExplosion(parent, col, row, map, 0, 1); // bas
    }

    private void spreadExplosion(Pane parent, int col, int row, Terrain map, int dx, int dy){
        for (int i = 1; i <= explosionRange; i++){
            int newCol = col + dx * i;
            int newRow = row + dy * i;

            if (newCol < 0 || newRow < 0 || newCol >= map.getGrid()[0].length || newRow >= map.getGrid().length){
                break;
            }

            Tile tile = map.getGrid()[newRow][newCol];

            if (tile.getType() == TileType.WALL){
                break;
            }

            addExplosionTile(parent, newCol, newRow, map);

            int counterRangeExplosion = 0;
            while (tile.getType() == TileType.DESTRUCTIBLE && counterRangeExplosion <= explosionRange){
                map.getGrid()[newRow][newCol] = new Tile(TileType.EMPTY, tileSize, newCol * tileSize, newRow * tileSize);
                parent.getChildren().remove(tile.getView());
                parent.getChildren().add(0, map.getGrid()[newRow][newCol].getView());
                counterRangeExplosion++;
                break;
            }
        }
    }

    private void addExplosionTile(Pane parent, int col, int row, Terrain map){

        double x = col * tileSize;
        double y = row * tileSize;

        Image imageExplosion = new Image(getClass().getResourceAsStream("/UI/explosion.png"));
        Rectangle flame = new Rectangle(tileSize, tileSize);
        flame.setX(x);
        flame.setY(y);
        flame.setFill(new ImagePattern(imageExplosion));//mettre une image ici

        parent.getChildren().add(flame);

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> parent.getChildren().remove(flame));
        delay.play();
    }
}
