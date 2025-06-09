package jeu.personnages;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import jeu.objets.Bomb;

import java.util.List;
import java.util.Set;

public abstract class Character2 {
    private ImageView imageView = new ImageView();
    private double x, y;
    private double width, height;
    private double speed;
    private int bombCount = 1;

    public Character2(ImageView imageView, double x, double y, double width, double height, double speed) {
        this.imageView = imageView;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public ImageView getImageView(){
        return imageView;
    }

    public void update(double deltaTime, Set<KeyCode> activeKeys){
        double dx = 0;
        double dy = 0;
        boolean bombPlaced = false;
        if (activeKeys.contains(getKeyUp())) dy -= speed * deltaTime;
        if (activeKeys.contains(getKeyDown())) dy += speed * deltaTime;
        if (activeKeys.contains(getKeyRight())) dx += speed * deltaTime;
        if (activeKeys.contains(getKeyLeft())) dx -= speed * deltaTime;
        if (activeKeys.contains(getKeyBomb())) bombPlaced = true;

        x += dx;
        y += dy;
        updateImageView();
    }

    private void updateImageView(){
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
    }


    public abstract KeyCode getKeyUp();
    public abstract KeyCode getKeyDown();
    public abstract KeyCode getKeyRight();
    public abstract KeyCode getKeyLeft();
    public abstract KeyCode getKeyBomb();
}
