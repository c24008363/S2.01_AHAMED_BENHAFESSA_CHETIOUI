package jeu.objets;

import UI.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.items.BombUp;
import jeu.personnages.Character;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents a bomb placed by a character. Handles timing (fuse) and explosion state.
 */
public class Bomb {
    private int x;
    private int y;
    private Image image;
    private ImageView imageView;
    private Character character;
    private boolean exists = false;

    private final Instant placedAt;
    private final long fuseSeconds = 2; // Duration before bomb explodes
    private boolean exploded = false;

    /**
     * Constructs a Bomb object.
     *
     * @param x         the X (column) coordinate in the game grid
     * @param y         the Y (row) coordinate in the game grid
     * @param path      image path relative to the theme folder
     * @param character the character who placed the bomb
     * @param range     explosion range (currently unused but reserved for future logic)
     */
    public Bomb(int x, int y, String path, Character character, int range) {
        this.x = x;
        this.y = y;
        try {
            image = new Image(BombUp.class.getResourceAsStream(MainMenu.getTheme() + path));
        } catch (Exception e) {
            System.err.println("Bomb image not found. Using default.");
            image = new Image(BombUp.class.getResourceAsStream("/UI/themes/default/" + path));
        }

        this.imageView = new ImageView(image);
        this.character = character;
        this.placedAt = Instant.now();
    }

    /**
     * Checks whether the bomb has exploded, based on its fuse timer.
     *
     * @return true if the bomb has exploded
     */
    public boolean countDown() {
        if (exploded) return true;

        Duration elapsed = Duration.between(placedAt, Instant.now());
        if (elapsed.getSeconds() >= fuseSeconds) {
            exploded = true;
            character.setBombCount(character.getBombCount() + 1); // Reset character bomb count
            return true;
        }
        return false;
    }

    /**
     * @return the ImageView of the bomb
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Sets the visual node (optional override).
     *
     * @param imageView the ImageView to assign
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * @return the X position of the bomb
     */
    public int getX() {
        return x;
    }

    /**
     * @return the Y position of the bomb
     */
    public int getY() {
        return y;
    }

    /**
     * @return true if the bomb is still active on the map
     */
    public boolean isExists() {
        return exists;
    }

    /**
     * Marks whether the bomb still exists in the game world.
     *
     * @param exists flag to set
     */
    public void setExists(boolean exists) {
        this.exists = exists;
    }

    /**
     * @return the character that placed the bomb
     */
    public Character getCharacter() {
        return character;
    }
}
