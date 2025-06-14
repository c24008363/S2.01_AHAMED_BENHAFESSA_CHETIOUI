package jeu.objets;

import javafx.scene.image.Image;
import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Bomb} class.
 * Verifies the countdown behavior and character bomb count reset after explosion.
 */
class BombTest {

    /**
     * A minimal implementation of the abstract {@link Character} class
     * for testing purposes. Tracks bomb count internally.
     */
    static class TestCharacter extends Character {
        private int bombCount = 1;

        /**
         * Constructs a TestCharacter at position (0,0) with a 1x1 matrix,
         * movement speed of 1, and tile size of 32.
         */
        public TestCharacter() {
            super(0, 0, new int[1][1], 1, 32);
        }

        /**
         * Gets the current bomb count.
         *
         * @return the bomb count
         */
        @Override
        public int getBombCount() {
            return bombCount;
        }

        /**
         * Sets the bomb count.
         *
         * @param bombCount the new bomb count
         */
        @Override
        public void setBombCount(int bombCount) {
            this.bombCount = bombCount;
        }
    }

    /**
     * Tests that a bomb explodes after its countdown duration (2 seconds)
     * and the character's bomb count is reset correctly.
     *
     * @throws InterruptedException if the thread sleep is interrupted
     */
    @Test
    void countDown_explodesAfterTime() throws InterruptedException {
        TestCharacter character = new TestCharacter();
        character.setBombCount(0);

        Bomb bomb = new Bomb(0, 0,
                new Image(BombTest.class.getResourceAsStream("/UI/themes/default/bomb.png")),
                character,
                1);
        bomb.setExists(true);

        // Wait slightly longer than the 2-second fuse
        Thread.sleep(2200);

        assertTrue(bomb.countDown(), "Bomb should have exploded");
        assertEquals(1, character.getBombCount(), "Bomb count should be incremented after explosion");
    }

    /**
     * Tests that the bomb does not explode immediately after being placed.
     * Ensures the countdown requires waiting for the full fuse duration.
     */
    @Test
    void countDown_notExplodedYet() {
        TestCharacter character = new TestCharacter();
        character.setBombCount(0);

        Bomb bomb = new Bomb(0, 0,
                new Image(BombTest.class.getResourceAsStream("/UI/themes/default/bomb.png")),
                character,
                1);

        assertFalse(bomb.countDown(), "Bomb should not explode immediately");
        assertEquals(0, character.getBombCount(), "Bomb count should remain unchanged before explosion");
    }
}
