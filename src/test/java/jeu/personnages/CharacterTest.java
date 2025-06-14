package jeu.personnages;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.items.BombUp;
import jeu.items.Gatherable;
import jeu.objets.Bomb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

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
        // 5x5 grid all free (0)
        gameMatrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameMatrix[i][j] = 0;
            }
        }
        // Place a bomb at (2,2)
        gameMatrix[2][2] = 3;

        // Place an item at (1,1)
        gameMatrix[1][1] = 4;

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
        assertEquals(oldY - 1, character.getY());
        assertEquals((character.getY()+(tileSize-5)/2)/tileSize, character.getRow());
    }

    @Test
    void moveDown() {
        int oldY = character.getY();
        character.moveDown(tileSize);
        assertEquals(oldY + 1, character.getY());
        assertEquals((character.getY()+(tileSize-5)/2)/tileSize, character.getRow());
    }

    @Test
    void moveLeft() {
        int oldX = character.getX();
        character.moveLeft(tileSize);
        assertEquals(oldX - 1, character.getX());
        assertEquals((character.getX()+(tileSize-5)/2)/tileSize, character.getCol());
    }

    @Test
    void moveRight() {
        int oldX = character.getX();
        character.moveRight(tileSize);
        assertEquals(oldX + 1, character.getX());
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
        assertTrue(character.canMoveToRework(2 * tileSize, 2 * tileSize, tileSize));

        // Moving out of bounds (negative coordinates)
        assertFalse(character.canMoveToRework(-1, -1, tileSize));
        // Moving out of bounds (beyond matrix)
        assertFalse(character.canMoveToRework(10 * tileSize, 10 * tileSize, tileSize));
    }

    @Test
    void updateIsInBomb() {
        // Place character exactly on bomb tile (2,2)
        character.setX(2 * tileSize);
        character.setY(2 * tileSize);
        character.updateIsInBomb(tileSize);
        assertTrue(character.isInBomb());

        // Place character off the bomb tile
        character.setX(0);
        character.setY(0);
        character.updateIsInBomb(tileSize);
        assertFalse(character.isInBomb());
    }

    @Test
    void placeBomb() {
        int initialBombCount = character.getBombCount();
        Image dummyImage = new Image(BombUp.class.getResourceAsStream("/UI/themes/default/bomb.png"));
        Bomb bomb = character.placeBomb(tileSize, dummyImage);
        assertNotNull(bomb);
        assertEquals(initialBombCount - 1, character.getBombCount());
        assertEquals(character, bomb.getCharacter());
        assertEquals(character.getRow(), bomb.getX());
        assertEquals(character.getCol(), bomb.getY());
    }

    @Test
    void isOnGatherable() {
        List<Gatherable> gatherables = new ArrayList<>();
        // Create a simple gatherable at (1,1)
        gatherables.add(new Gatherable(1,1,tileSize,"bonus_bomb.png") {
            @Override
            public void applyEffect(Character character) { }
        });

        // Place character exactly on item
        character.setX(1 * tileSize);
        character.setY(1 * tileSize);
        Gatherable g = character.isOnGatherable(gatherables, tileSize);
        assertNotNull(g);
        assertEquals(1, g.getRow());
        assertEquals(1, g.getCol());

        // Place character off item
        character.setX(0);
        character.setY(0);
        assertNull(character.isOnGatherable(gatherables, tileSize));
    }
}
