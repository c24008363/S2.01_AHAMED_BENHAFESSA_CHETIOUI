package jeu.objets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.personnages.*;
import jeu.personnages.Character;

public class Bomb {
    private int x;
    private int y;
    private Image image;
    private Character character;
    private int range;
    private int time = 0;
    private ImageView imageView;


    public Bomb(int x, int y, Image image, Character character, int range) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.character = character;
        this.range = range;

    }

    public boolean countDown(){
        time++;
        System.out.println(time);
        if (time>300){
            character.setBombCount(character.getBombCount()+1);
            return true; }
        return false;
    }

    public ImageView getImageView() {
        if (imageView == null) {return null;}
        return imageView;
    }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }


}
