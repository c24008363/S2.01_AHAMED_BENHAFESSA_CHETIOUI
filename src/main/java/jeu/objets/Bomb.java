package jeu.objets;

import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.awt.*;

public class Bomb {
    private final Circle view;

    public Bomb(double x, double y, double radius, Pane parent){
        view = new Circle(radius);
        view.setFill(Color.BLUE);
        view.setCenterX(x);
        view.setCenterY(y);

        parent.getChildren().add(view);

        PauseTransition timer = new PauseTransition(Duration.seconds(2));
        timer.setOnFinished(event -> parent.getChildren().remove(view));
        timer.play();
    }

    public Circle getView(){
        return view;
    }
}
