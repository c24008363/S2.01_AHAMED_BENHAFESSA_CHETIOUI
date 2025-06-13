package jeu.items;

import javafx.scene.image.Image;
import jeu.personnages.Character;

/**
 * A Gatherable that increases the explosion range of a character's bombs.
 */
public class FlameUp extends Gatherable {

    /**
     * Constructs a FlameUp item at the specified grid position.
     *
     * @param row       the row coordinate
     * @param col       the column coordinate
     * @param tileSize  the size of a tile in pixels
     */
    public FlameUp(int row, int col, int tileSize) {
        super(row, col, tileSize, "bonus_flame.png");
    }

    /**
     * Increases the explosion range of the character by 1.
     *
     * @param character the character who picked up the item
     */
    @Override
    public void applyEffect(Character character) {
        character.setRange(character.getRange() + 1);
    }
}
