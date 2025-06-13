package jeu.items;

import javafx.scene.image.Image;
import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BombUpTest {

    static class TestCharacter extends Character {
        private int bombCount = 0;

        public TestCharacter() {
            super(0, 0, new int[1][1], 1, new Image(""), 32);
        }

        @Override
        public int getBombCount() {
            return bombCount;
        }

        @Override
        public void setBombCount(int bombCount) {
            this.bombCount = bombCount;
        }
    }

    @Test
    void applyEffect() {
        TestCharacter character = new TestCharacter();

        BombUp bombUp = new BombUp(0, 0, 32);
        bombUp.applyEffect(character);

        assertEquals(2, character.getBombCount());
    }
}
