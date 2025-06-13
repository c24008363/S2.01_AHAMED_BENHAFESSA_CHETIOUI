package jeu.objets;

import javafx.scene.image.ImageView;

/**
 * Represents an explosion event in the game with timing information.
 * This is used to track how long an explosion has been active.
 */
public class TimedExplosion {
    private Explosion explosion;
    private ImageView imageView;
    private long createdTimeNano;

    /**
     * Constructs a TimedExplosion object.
     *
     * @param explosion        the explosion logic (position)
     * @param imageView        the visual representation of the explosion
     * @param createdTimeNano  the timestamp (in nanoseconds) when the explosion was created
     */
    public TimedExplosion(Explosion explosion, ImageView imageView, long createdTimeNano) {
        this.explosion = explosion;
        this.imageView = imageView;
        this.createdTimeNano = createdTimeNano;
    }

    /**
     * @return the Explosion object
     */
    public Explosion getExplosion() {
        return explosion;
    }

    /**
     * @return the ImageView associated with this explosion
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @return the time (in nanoseconds) when the explosion was created
     */
    public long getCreatedTimeNano() {
        return createdTimeNano;
    }
}
