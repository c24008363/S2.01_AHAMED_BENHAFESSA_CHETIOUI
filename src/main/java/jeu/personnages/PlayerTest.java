package jeu.personnages;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class PlayerTest extends Character2{
    public PlayerTest(ImageView imageWiew, double x, double y, double width, double height, double speed){
        super(imageWiew, x, y, width, height, speed);
    }

    @Override public KeyCode getKeyUp() {return KeyCode.Z;}
    @Override public KeyCode getKeyDown() {return KeyCode.S;}
    @Override public KeyCode getKeyRight() {return KeyCode.D;}
    @Override public KeyCode getKeyLeft() {return KeyCode.Q;}
    @Override public KeyCode getKeyBomb() {return KeyCode.E;}
}

