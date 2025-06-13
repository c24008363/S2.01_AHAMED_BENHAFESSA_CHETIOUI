package jeu.objets;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimedExplosionTest {

    @Test
    void getCreatedTimeNano() {
        // Dummy Explosion instance (you can create a minimal subclass if Explosion is abstract or complex)
        Explosion explosion = new Explosion(1, 1); // Example constructor
        ImageView imageView = new ImageView();
        long expectedNanoTime = System.nanoTime();

        TimedExplosion timedExplosion = new TimedExplosion(explosion, imageView, expectedNanoTime);

        assertEquals(expectedNanoTime, timedExplosion.getCreatedTimeNano());
    }
}
