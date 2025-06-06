package jeu.personnages;

public class Player extends Character {

    public Player(int startRow, int startCol, int[][] gameMatrix, int id) {
        super(startRow, startCol, gameMatrix, id);
    }

    @Override
    public void update() {
        // Optional logic to update player each frame, e.g. from input
    }
}
