package jeu.items;

import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link FlameUp} item.
 * Verifies that the {@link FlameUp#applyEffect(Character)} method correctly increases a character's bomb explosion range.
 */
class FlameUpTest {

    /**
     * A minimal implementation of the abstract {@link Character} class
     * for testing purposes. It tracks explosion range internally.
     */
    static class TestCharacter extends Character {
        private int range = 0;

        /**
         * Constructs a TestCharacter at position (0,0) with a 1x1 game matrix,
         * a movement speed of 1, and tile size of 32.
         */
        public TestCharacter() {
            super(0, 0, new int[1][1], 1, 32);
        }

        /**
         * Gets the current explosion range.
         *
         * @return the current explosion range
         */
        @Override
        public int getRange() {
            return range;
        }

        /**
         * Sets the explosion range.
         *
         * @param range the new explosion range
         */
        @Override
        public void setRange(int range) {
            this.range = range;
        }
    }

    /**
     * Tests that applying the {@link FlameUp} effect increases the explosion range of a character by 1.
     */
    @Test
    void applyEffect() {
        TestCharacter character = new TestCharacter();
        character.setRange(1);

        FlameUp flameUp = new FlameUp(0, 0, 32);
        flameUp.applyEffect(character);

        assertEquals(2, character.getRange(), "Explosion range should increase by 1 after applying FlameUp");
    }
}
