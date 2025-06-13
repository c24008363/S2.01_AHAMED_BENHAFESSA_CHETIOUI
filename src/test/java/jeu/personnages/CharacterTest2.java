package jeu.personnages;

import javafx.scene.image.Image;
import jeu.items.BombUp;
import jeu.items.Gatherable;
import jeu.objets.Bomb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//THIS TEST CLASS IS MADE TO TEST THE UNABILITY TO MOVE OVER TAKEN SPACES

class CharacterTest2 {

    // A simple concrete subclass to instantiate Character
    static class TestCharacter extends Character {
        public TestCharacter(int startRow, int startCol, int[][] gameMatrix, int id, int tileSize) {
            super(startRow, startCol, gameMatrix, id, tileSize);
        }
    }

    private int tileSize = 40;
    private int[][] gameMatrix;
    private TestCharacter character;

    @BeforeEach
    void setup() {
        // 5x5 grid full of 1
        gameMatrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameMatrix[i][j] = 1;
            }
        }

        character = new TestCharacter(2, 2, gameMatrix, 1, tileSize);
        // Set pixel position consistent with (row=3, col=3)
        character.setX(3 * tileSize);
        character.setY(3 * tileSize);
        character.col = (3*tileSize)/tileSize;
        character.row = (3*tileSize)/tileSize;
    }

    @Test
    void moveUp() {
        System.out.println(character.getY());
        // Moving up should succeed if tile above is free
        int oldY = character.getY();
        character.moveUp(tileSize);
        assertEquals(oldY, character.getY());
        assertEquals((character.getY()+(tileSize-5)/2)/tileSize, character.getRow());
    }

    @Test
    void moveDown() {
        int oldY = character.getY();
        character.moveDown(tileSize);
        assertEquals(oldY, character.getY());
        assertEquals((character.getY()+(tileSize-5)/2)/tileSize, character.getRow());
    }

    @Test
    void moveLeft() {
        int oldX = character.getX();
        character.moveLeft(tileSize);
        assertEquals(oldX , character.getX());
        assertEquals((character.getX()+(tileSize-5)/2)/tileSize, character.getCol());
    }

    @Test
    void moveRight() {
        int oldX = character.getX();
        character.moveRight(tileSize);
        assertEquals(oldX , character.getX());
        assertEquals((character.getX()+(tileSize-5)/2)/tileSize, character.getCol());
    }

    @Test
    void canMoveToRework() {
        // Attempt to move onto a durable wall (2)
        gameMatrix[1][2] = 2;
        assertFalse(character.canMoveToRework((1 * tileSize), (2 * tileSize), tileSize));

        // Attempt to move onto bomb tile when not inBomb
        assertFalse(character.canMoveToRework(2 * tileSize, 2 * tileSize, tileSize));

        // Set inBomb true, should allow moving over bombs
        character.setInBomb(true);
        assertFalse(character.canMoveToRework(2 * tileSize, 2 * tileSize, tileSize));

        // Moving out of bounds (negative coordinates)
        assertFalse(character.canMoveToRework(-1, -1, tileSize));
        // Moving out of bounds (beyond matrix)
        assertFalse(character.canMoveToRework(10 * tileSize, 10 * tileSize, tileSize));
    }


}
