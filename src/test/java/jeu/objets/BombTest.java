package jeu.objets;

import javafx.scene.image.Image;
import jeu.personnages.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BombTest {

    static class TestCharacter extends Character {
        private int bombCount = 1;

        public TestCharacter() {
            super(0, 0, new int[1][1], 1, 32);
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
    void countDown_explodesAfterTime() throws InterruptedException {
        TestCharacter character = new TestCharacter();
        character.setBombCount(0);

        Bomb bomb = new Bomb(0, 0, new Image(BombTest.class.getResourceAsStream("/UI/themes/default/bomb.png")) , character, 1);
        bomb.setExists(true);

        // Wait a bit more than the fuse time (2s) to ensure explosion
        Thread.sleep(2200);

        assertTrue(bomb.countDown(), "Bomb should have exploded");
        assertEquals(1, character.getBombCount(), "Bomb count should be reset after explosion");
    }

    @Test
    void countDown_notExplodedYet() {
        TestCharacter character = new TestCharacter();
        character.setBombCount(0);

        Bomb bomb = new Bomb(0, 0, new Image(BombTest.class.getResourceAsStream("/UI/themes/default/bomb.png")), character, 1);

        assertFalse(bomb.countDown(), "Bomb should not explode immediately");
        assertEquals(0, character.getBombCount(), "Bomb count should remain unchanged");
    }
}
