package jeu.personnages;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import jeu.items.Gatherable;
import jeu.objets.Bomb;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Abstract base class representing a character in the Bomberman-style game.
 * Manages position, movement, image, bomb placement, and interactions with gatherable items.
 */
public abstract class Character {
    protected int row;
    protected int col;
    private int x;
    private int y;
    protected int[][] gameMatrix;
    protected int id;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private int bombCount = 1;
    private boolean isInBomb = false;
    private int range = 1;

    /**
     * Constructs a Character instance.
     *
     * @param startRow   starting row in the matrix
     * @param startCol   starting column in the matrix
     * @param gameMatrix reference to the game grid matrix
     * @param id         character ID
     * @param image      image representing the character
     * @param tileSize   size of a tile in pixels
     */
    public Character(int startRow, int startCol, int[][] gameMatrix, int id, Image image, int tileSize) {
        this.row = startRow;
        this.col = startCol;
        this.gameMatrix = gameMatrix;
        this.id = id;
        this.imageProperty.set(image);
        this.x = startRow*tileSize;
        this.y = startCol*tileSize;
    }

    // Getters and setters for row/column and pixel positions
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    // Range (bomb explosion range)
    public int getRange() { return range; }
    public void setRange(int range) { this.range = range; }

    // Bomb status
    public boolean isInBomb() { return isInBomb; }
    public void setInBomb(boolean inBomb) { isInBomb = inBomb; }

    // Image access
    public Image getImage() { return imageProperty.get(); }
    public void setImage(Image image) { this.imageProperty.set(image); }


    /**
     * Moves the character up by 1 pixel if allowed.
     *
     * @param tileSize the size of one tile in pixels
     */
    public void moveUp(int tileSize) {
        if (canMoveToRework(y-2, x, tileSize )) {
            setY(getY() - 1);
            row = (getY()+(tileSize-5)/2)/tileSize;
            updateIsInBomb(tileSize);
        }
    }

    /**
     * Moves the character down by 1 pixel if allowed.
     *
     * @param tileSize the size of one tile in pixels
     */
    public void moveDown(int tileSize) {
        if (canMoveToRework(y+2, x, tileSize )) {
            setY(getY() + 1);
            row = (getY()+(tileSize-5)/2)/tileSize;
            updateIsInBomb(tileSize);
        }

    }

    /**
     * Moves the character left by 1 pixel if allowed.
     *
     * @param tileSize the size of one tile in pixels
     */
    public void moveLeft(int tileSize) {
        if (canMoveToRework(y, (x-2), tileSize )) {
            setX(getX() - 1);
            col = (getX()+(tileSize-5)/2)/tileSize;
            updateIsInBomb(tileSize);

        }
    }

    /**
     * Moves the character right by 1 pixel if allowed.
     *
     * @param tileSize the size of one tile in pixels
     */
    public void moveRight(int tileSize) {
        if (canMoveToRework(y, (x+2), tileSize )){
            setX(getX() + 1);
            col = (getX()+(tileSize-5)/2)/tileSize;
            updateIsInBomb(tileSize);
        }
    }


    /**
     * Checks if the character can move to the given pixel coordinates.
     * Collision is checked at all four corners of the character's bounding box.
     *
     * @param x        target pixel X position
     * @param y        target pixel Y position
     * @param TileSize the size of one tile in pixels
     * @return true if movement is allowed
     */
    protected boolean canMoveToRework(int x, int y, int TileSize) {
        if (x / TileSize <= 0 || y / TileSize <= 0 || x / TileSize >= gameMatrix.length || y / TileSize >= gameMatrix[0].length)
            return false;

        if (!isInBomb) {
            //The player cannot move through bombs, checks the collision between the player and: bombs, durable walls, breakable walls.
            return (gameMatrix[(x) / TileSize][(y) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y) / TileSize] == 4) // haut gauche
                    &&
                    (gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 0 || gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 4) // bas droite
                    &&
                    (gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 0 || gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 4) // haut droite
                    &&
                    (gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 4); // bas gauche
        } else {

            //The player just placed a bomb, he is allowed to move through them.
            return (gameMatrix[(x) / TileSize][(y) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y) / TileSize] >= 3) // haut gauche
                    &&
                    (gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 0 || gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] >= 3) // bas droite
                    &&
                    (gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 0 || gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] >= 3) // haut droite
                    &&
                    (gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] >= 3); // bas gauche
        }



    }



    /**
     * Updates the `isInBomb` flag based on the character's current tile.
     * A character is considered inside a bomb if any corner is on a bomb tile.
     *
     * @param tileSize the size of one tile in pixels
     */
    public void updateIsInBomb(int tileSize) {
        int currentRow = getY() / tileSize;
        int currentCol = getX() / tileSize;

        // Check all 4 corners of the player
        boolean topLeft = gameMatrix[currentRow][currentCol] == 3;
        boolean topRight = gameMatrix[currentRow][(getX() + tileSize - 5) / tileSize] == 3;
        boolean bottomLeft = gameMatrix[(getY() + tileSize - 5) / tileSize][currentCol] == 3;
        boolean bottomRight = gameMatrix[(getY() + tileSize - 5) / tileSize][(getX() + tileSize - 5) / tileSize] == 3;

        // Player is considered "in bomb" if ANY corner is on a bomb tile
        // This allows smoother transition when moving off bombs
        isInBomb = topLeft || topRight || bottomLeft || bottomRight;
    }

    /**
     * Places a bomb at the character's current position.
     * Decrements the available bomb count.
     *
     * @param TileSize the size of one tile (not used here)
     * @return a new {@link Bomb} object
     */
    public Bomb placeBomb(int TileSize, Image image){
        bombCount = bombCount -1;
        return new Bomb(this.row, this.col, image, this, 1);

    }

    /**
     * Checks whether the character is currently on a {@link Gatherable} item.
     *
     * @param gatherables list of gatherables in the game
     * @param TileSize    the size of one tile in pixels
     * @return the gatherable under the character, or null if none found
     */
    public Gatherable isOnGatherable(List<Gatherable> gatherables, int TileSize) {
        int topLeftRow = getY() / TileSize;
        int topLeftCol = getX() / TileSize;

        int topRightRow = getY() / TileSize;
        int topRightCol = (getX() + TileSize - 5) / TileSize;

        int bottomLeftRow = (getY() + TileSize - 5) / TileSize;
        int bottomLeftCol = getX() / TileSize;

        int bottomRightRow = (getY() + TileSize - 5) / TileSize;
        int bottomRightCol = (getX() + TileSize - 5) / TileSize;

        // Helper function to find gatherable at given tile
        BiPredicate<Integer, Integer> isGatherableAt = (r, c) -> {
            for (Gatherable g : gatherables) {
                if (g.getRow() == r && g.getCol() == c) return true;
            }
            return false;
        };

        if (gameMatrix[topLeftRow][topLeftCol] == 4 && isGatherableAt.test(topLeftRow, topLeftCol)) {
            for (Gatherable g : gatherables) {
                if (g.getRow() == topLeftRow && g.getCol() == topLeftCol) return g;
            }
        }

        if (gameMatrix[topRightRow][topRightCol] == 4 && isGatherableAt.test(topRightRow, topRightCol)) {
            for (Gatherable g : gatherables) {
                if (g.getRow() == topRightRow && g.getCol() == topRightCol) return g;
            }
        }

        if (gameMatrix[bottomLeftRow][bottomLeftCol] == 4 && isGatherableAt.test(bottomLeftRow, bottomLeftCol)) {
            for (Gatherable g : gatherables) {
                if (g.getRow() == bottomLeftRow && g.getCol() == bottomLeftCol) return g;
            }
        }

        if (gameMatrix[bottomRightRow][bottomRightCol] == 4 && isGatherableAt.test(bottomRightRow, bottomRightCol)) {
            for (Gatherable g : gatherables) {
                if (g.getRow() == bottomRightRow && g.getCol() == bottomRightCol) return g;
            }
        }

        return null;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }
}
