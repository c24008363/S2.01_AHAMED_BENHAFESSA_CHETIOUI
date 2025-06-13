package jeu.items;

import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlameUpTest {

    static class TestCharacter extends Character {
        private int range = 0;

        public TestCharacter() {
            super(0, 0, new int[1][1], 1, 32);
        }

        @Override
        public int getRange() {
            return range;
        }

        @Override
        public void setRange(int range) {
            this.range = range;
        }
    }

    @Test
    void applyEffect() {
        TestCharacter character = new TestCharacter();
        character.setRange(1);

        FlameUp flameUp = new FlameUp(0, 0, 32);
        flameUp.applyEffect(character);

        assertEquals(2, character.getRange());
    }
}
