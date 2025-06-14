package jeu.objets;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link TimedExplosion} class.
 * Verifies that explosion creation time is correctly recorded and retrievable.
 */
class TimedExplosionTest {

    /**
     * Tests that {@link TimedExplosion#getCreatedTimeNano()} returns the exact timestamp
     * provided at construction time.
     */
    @Test
    void getCreatedTimeNano() {
        // Create a dummy Explosion instance at a known position
        Explosion explosion = new Explosion(1, 1);
        ImageView imageView = new ImageView();

        // Simulate a known creation time (you may consider mocking System.nanoTime() if needed)
        long expectedNanoTime = System.nanoTime();

        TimedExplosion timedExplosion = new TimedExplosion(explosion, imageView, expectedNanoTime);

        assertEquals(expectedNanoTime, timedExplosion.getCreatedTimeNano(),
                "TimedExplosion should return the exact creation timestamp provided");
    }
}
