package jeu.personnages;

import javafx.scene.image.Image;

public class Player extends Character {

    public Player(int startRow, int startCol, int[][] gameMatrix, int id, Image image, int tileSize) { super(startRow, startCol, gameMatrix, id, image, tileSize);
    }

    @Override
    public void update() {
        // Optional logic to update player each frame, e.g. from input
    }
}
