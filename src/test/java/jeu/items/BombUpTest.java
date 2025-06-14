package jeu.items;

import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link BombUp} item.
 * Verifies that the {@link BombUp#applyEffect(Character)} method correctly increases a character's bomb count.
 */
class BombUpTest {

    /**
     * A minimal implementation of the abstract {@link Character} class
     * for testing purposes. It tracks bomb count internally.
     */
    static class TestCharacter extends Character {
        private int bombCount = 0;

        /**
         * Constructs a TestCharacter at position (0,0) with a 1x1 game matrix,
         * a movement speed of 1, and tile size of 32.
         */
        public TestCharacter() {
            super(0, 0, new int[1][1], 1, 32);
        }

        /**
         * Gets the current bomb count.
         *
         * @return the current bomb count
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
     * Tests that applying the {@link BombUp} effect increases the bomb count of a character by 1.
     */
    @Test
    void applyEffect() {
        TestCharacter character = new TestCharacter();
        character.setBombCount(2);

        BombUp bombUp = new BombUp(0, 0, 32);
        bombUp.applyEffect(character);

        assertEquals(3, character.getBombCount(), "Bomb count should increase by 1 after applying BombUp");
    }
}
