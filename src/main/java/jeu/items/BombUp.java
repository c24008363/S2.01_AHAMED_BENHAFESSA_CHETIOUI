package jeu.items;

import javafx.scene.image.Image;
import jeu.personnages.Character;

/**
 * A Gatherable that increases the bomb count a player can carry.
 */
public class BombUp extends Gatherable {

    /**
     * Constructs a BombUp item at the specified grid position.
     *
     * @param row       the row coordinate
     * @param col       the column coordinate
     * @param tileSize  the size of a tile in pixels
     */
    public BombUp(int row, int col, int tileSize) {
        super(row, col, tileSize, "bonus_bomb.png");
    }

    /**
     * Increases the bomb count of the character by 1.
     *
     * @param character the character who picked up the item
     */
    @Override
    public void applyEffect(Character character) {
        character.setBombCount(character.getBombCount() + 1);
    }
}
