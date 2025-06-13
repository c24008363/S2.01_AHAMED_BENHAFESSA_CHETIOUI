package jeu.objets;

/**
 * Represents the location of an explosion on the game grid.
 */
public class Explosion {
    private int x;
    private int y;

    /**
     * Constructs an Explosion with the given tile coordinates.
     *
     * @param x the X coordinate (column) of the explosion
     * @param y the Y coordinate (row) of the explosion
     */
    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X coordinate of the explosion
     */
    public int getX() {
        return x;
    }

    /**
     * @return the Y coordinate of the explosion
     */
    public int getY() {
        return y;
    }


}
