package jeu.personnages;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class Player2 extends Character2{
    public Player2(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, speed);
        Image image = new Image(getClass().getResourceAsStream("/UI/hagried.jpg"));
        getRectangle().setFill(new ImagePattern(image));
    }

    @Override public KeyCode getKeyUp(){return KeyCode.Z;}
    @Override public KeyCode getKeyDown(){return KeyCode.S;}
    @Override public KeyCode getKeyLeft(){return KeyCode.Q;}
    @Override public KeyCode getKeyRight(){return KeyCode.D;}
}
