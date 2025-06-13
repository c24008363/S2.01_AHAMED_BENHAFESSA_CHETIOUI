package jeu.personnages;

import javafx.scene.image.Image;

/**
 * We in a first place meant to do a bot, which was not created in the end.
 * I agree that the abstract character was not necessary in our case, but it was decided at the end of the project.
 * Really, this class creates a player, nothing more is made next to the abstract class
 */
public class Player extends Character {

    public Player(int startRow, int startCol, int[][] gameMatrix, int id, int tileSize) { super(startRow, startCol, gameMatrix, id, tileSize);
    }
}
