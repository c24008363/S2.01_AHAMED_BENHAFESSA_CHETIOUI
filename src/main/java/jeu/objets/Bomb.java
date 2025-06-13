package jeu.objets;

import UI.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.items.BombUp;
import jeu.personnages.Character;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Bomb {
    private int x;
    private int y;
    private Image image;
    private Character character;
    private boolean exists = false;
    private ImageView imageView;

    private final Instant placedAt;
    private final long fuseSeconds = 2; // Bomb fuse time in seconds
    private boolean exploded = false;

    public Bomb(int x, int y, String path, Character character, int range) {
        this.x = x;
        this.y = y;

        try{
            image = new Image(BombUp.class.getResourceAsStream(MainMenu.getTheme()+path));
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            image = new Image(BombUp.class.getResourceAsStream("/UI/themes/default/"+path));
        }

        this.imageView = new ImageView(image);
        this.character = character;
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

    public void setExists(boolean exists) {this.exists = exists;}

    public boolean isExists() {return exists;}

    public int getY() {
        return y;
    }

    public Character getCharacter() {return character;}
}
