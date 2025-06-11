package jeu.personnages;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;

public class Player2 extends Character{
    private Image image;
    public Player2(Image image, double x, double y, double width, double height, double speed){
        super(x, y, width, height, speed);
        this.image = image;
        getRectangle().setFill(new ImagePattern(image));
    }

    @Override public KeyCode getKeyUp() {return KeyCode.I;}
    @Override public KeyCode getKeyDown() {return KeyCode.K;}
    @Override public KeyCode getKeyLeft() {return KeyCode.J;}
    @Override public KeyCode getKeyRight() {return KeyCode.L;}
    @Override public KeyCode getKeyBomb(){return KeyCode.O;}
}