package jeu.objets;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Bomb {
    private final Circle view;
    private final int explosionRange = 2;
    private final double tileSize = 40;

    public Bomb(double x, double y, double radius, Pane parent){
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
            explode(parent, x, y);
        });
        timer.play();
    }

    public Circle getView(){
        return view;
    }

    private void explode(Pane parent, double centerX, double centerY){
        // Centre de l'explosion
        addExplosionTile(parent, centerX, centerY);

        // direction -> haut, bas, gauche, droite
        for (int i = 0; i <= explosionRange; i++){
            addExplosionTile(parent, centerX+i * tileSize, centerY); // droite
            addExplosionTile(parent, centerX-i * tileSize, centerY); // gauche
            addExplosionTile(parent, centerX, centerY - i * tileSize); // haut
            addExplosionTile(parent, centerX, centerY + i * tileSize); // bas
        }
    }

    private void addExplosionTile(Pane parent, double x, double y){
        Rectangle flame = new Rectangle(x, y, tileSize-2, tileSize-2);
        flame.setX(x-tileSize/2);
        flame.setY(y-tileSize/2);
        flame.setFill(Color.ORANGE);//mettre une image ici

        parent.getChildren().add(flame);

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> parent.getChildren().remove(flame));
        delay.play();
    }
}
