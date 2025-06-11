package jeu.items;

import javafx.scene.image.Image;
import jeu.personnages.Character;

public class BombUp extends Gatherable {
    public BombUp(int row, int col, int tileSize) {
        super(row, col, tileSize, new Image(BombUp.class.getResourceAsStream("/UI/013-bonus_bomb.png")));
    }

    @Override
    public void applyEffect(Character character) {
        character.setBombCount(character.getBombCount() + 1);
    }
}
