package jeu.objets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.personnages.Character;

import java.time.Duration;
import java.time.Instant;

public class Bomb {
    private int x;
    private int y;
    private Image image;
    private Character character;
    private int range; //dureeVie
    private ImageView imageView;

    private final Instant placedAt;
    private final long fuseSeconds = 2; // Bomb fuse time in seconds
    private boolean exploded = false;

    public Bomb(int x, int y, Image image, Character character, int range) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.character = character;
        this.range = range;
        this.placedAt = Instant.now();
    }

    public boolean countDown() {
        if (exploded) return true;

        Duration elapsed = Duration.between(placedAt, Instant.now());
        if (elapsed.getSeconds() >= fuseSeconds) {
            exploded = true;
            character.setBombCount(character.getBombCount() + 1);
            return true;
        }
        return false;
    }

    public ImageView getImageView() {
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
