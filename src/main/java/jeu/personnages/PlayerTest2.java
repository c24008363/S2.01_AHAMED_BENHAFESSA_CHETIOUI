package jeu.personnages;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class PlayerTest2 extends Character2{
    public PlayerTest2(ImageView imageWiew, double x, double y, double width, double height, double speed){
        super(imageWiew, x, y, width, height, speed);
    }

    @Override public KeyCode getKeyUp() {return KeyCode.UP;}
    @Override public KeyCode getKeyDown() {return KeyCode.DOWN;}
    @Override public KeyCode getKeyRight() {return KeyCode.RIGHT;}
    @Override public KeyCode getKeyLeft() {return KeyCode.LEFT;}
    @Override public KeyCode getKeyBomb() {return KeyCode.SHIFT;}
}
