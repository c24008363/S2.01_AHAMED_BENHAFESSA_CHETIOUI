package jeu.items;

import UI.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.personnages.Character;

/**
 * Abstract class representing an item that can be gathered by a character.
 * Examples include power-ups that increase bomb count or explosion range.
 */
public abstract class Gatherable {
    protected int row;
    protected int col;
    protected ImageView imageView;
    protected boolean isBuff = true;
    protected Image sprite;

    /**
     * Constructs a Gatherable object placed on the game board.
     *
     * @param row       the row position of the item
     * @param col       the column position of the item
     * @param tileSize  the size of a tile in pixels
     * @param sprite    the image path (relative to theme folder)
     */
    public Gatherable(int row, int col, int tileSize, String sprite) {
        this.row = row;
        this.col = col;
        this.imageView = new ImageView(setSprite(sprite));
        this.imageView.setFitWidth(tileSize);
        this.imageView.setFitHeight(tileSize);
        this.imageView.setLayoutX(col * tileSize);
        this.imageView.setLayoutY(row * tileSize);
    }

    /**
     * Loads the sprite image for this item based on current theme.
     *
     * @param path relative path to the sprite image
     * @return loaded Image object
     */
    public Image setSprite(String path) {
        try {
            sprite = new Image(BombUp.class.getResourceAsStream(MainMenu.getTheme() + path));
        } catch (Exception e) {
            System.err.println("Power up image not found. Using default.");
            sprite = new Image(BombUp.class.getResourceAsStream("/UI/themes/default/" + path));
        }
        return sprite;
    }

    /**
     * @return the visual representation of the gatherable
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @return row coordinate of the item
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column coordinate of the item
     */
    public int getCol() {
        return col;
    }

    /**
     * @return true if this item is a buff, false if it is a debuff
     */
    public boolean isBuff() {
        return isBuff;
    }

    /**
     * Defines how the item affects the character who it will target (self for buffes, other for debuffs).
     *
     * @param character the player character affected by the item
     */
    public abstract void applyEffect(Character character);

    // Unused but reserved for potential item generation
    @SuppressWarnings("unused")
    private Gatherable getRandomItem(int row, int col, int tileSize) {
        return new BombUp(row, col, tileSize);
    }
}
