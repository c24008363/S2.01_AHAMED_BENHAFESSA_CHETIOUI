package jeu.objets;

import javafx.scene.image.ImageView;

public class TimedExplosion {
    private Explosion explosion;
    private ImageView imageView;
    private long createdTimeNano;

    public TimedExplosion(Explosion explosion, ImageView imageView, long createdTimeNano) {
        this.explosion = explosion;
        this.imageView = imageView;
        this.createdTimeNano = createdTimeNano;
    }

    public Explosion getExplosion() {
        return explosion;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public long getCreatedTimeNano() {
        return createdTimeNano;
    }
}
