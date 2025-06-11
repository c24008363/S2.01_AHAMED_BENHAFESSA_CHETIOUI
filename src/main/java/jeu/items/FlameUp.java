package jeu.items;

import javafx.scene.image.Image;
import jeu.personnages.Character;

public class FlameUp extends Gatherable {
    public FlameUp(int row, int col, int tileSize) {
        super(row, col, tileSize, "bonus_flame.png");
    }

    @Override
    public void applyEffect(Character character) {
        character.setRange(character.getRange()+1);}
}