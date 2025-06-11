package jeu.personnages;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import jeu.items.Gatherable;
import jeu.objets.Bomb;

import java.util.List;

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



    public Character(int startRow, int startCol, int[][] gameMatrix, int id, Image image, int tileSize) {
        this.row = startRow;
        this.col = startCol;
        this.gameMatrix = gameMatrix;
        this.id = id;
        this.imageProperty.set(image);
        this.x = startRow*tileSize;
        this.y = startCol*tileSize;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRange() { return range; }

    public void setRange(int range) { this.range = range; }

    public boolean isInBomb() { return isInBomb;}

    public void setInBomb(boolean inBomb) { isInBomb = inBomb;}

    public Image getImage() {return imageProperty.get();}
    public void setImage(Image image) { this.imageProperty.set(image);}

    public void moveUp(int tileSize) {
        if (canMoveToRework(y-2, x, tileSize )) {
            setY(getY() - 1);
            row = (getY()+(tileSize-5)/2)/tileSize;
            System.out.print("y="+y);
            System.out.print(" row="+row);
            System.out.println(" "+isInBomb);
            updateIsInBomb(tileSize);
        }
    }

    public void moveDown(int tileSize) {
        if (canMoveToRework(y+2, x, tileSize )) {
            setY(getY() + 1);
            row = (getY()+(tileSize-5)/2)/tileSize;
            System.out.print("y="+y);
            System.out.print("row="+row);
            System.out.println(" "+isInBomb);
            updateIsInBomb(tileSize);
        }

    }

    public void moveLeft(int tileSize) {
        if (canMoveToRework(y, (x-2), tileSize )) {
            setX(getX() - 1);
            col = (getX()+(tileSize-5)/2)/tileSize;
            System.out.print("x="+x);
            System.out.print("col="+col);
            System.out.println(" "+isInBomb);
            updateIsInBomb(tileSize);

        }
    }

    public void moveRight(int tileSize) {
        if (canMoveToRework(y, (x+2), tileSize )){
            setX(getX() + 1);
            col = (getX()+(tileSize-5)/2)/tileSize;
            System.out.print("x="+x);
            System.out.print("col="+col);
            System.out.println(" "+isInBomb);
            updateIsInBomb(tileSize);
        }
    }


    protected boolean canMoveToRework(int x, int y, int TileSize) {
        if (x / TileSize <= 0 || y / TileSize <= 0 || x / TileSize >= gameMatrix.length || y / TileSize >= gameMatrix[0].length)
            return false;

        if (!isInBomb) {
            return (gameMatrix[(x) / TileSize][(y) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y) / TileSize] == 4) // haut gauche
                    &&
                    (gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 0 || gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 4) // bas droite
                    &&
                    (gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 0 || gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 4) // haut droite
                    &&
                    (gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 4); // bas gauche
        } else {
            return (gameMatrix[(x) / TileSize][(y) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y) / TileSize] >= 3) // haut gauche
                    &&
                    (gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] == 0 || gameMatrix[(x + (TileSize - 5)) / TileSize][(y + (TileSize - 5)) / TileSize] >= 3) // bas droite
                    &&
                    (gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] == 0 || gameMatrix[((x) + TileSize - 5) / TileSize][(y) / TileSize] >= 3) // haut droite
                    &&
                    (gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] == 0 || gameMatrix[(x) / TileSize][(y + TileSize - 5) / TileSize] >= 3); // bas gauche
        }



    }



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

    public Bomb placeBomb(int TileSize){
        bombCount = bombCount -1;
        return new Bomb(this.row, this.col, new Image(getClass().getResourceAsStream("/UI/bombFace.png"),
                        TileSize, TileSize, false, true), this, 1);

    }

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
        java.util.function.BiPredicate<Integer, Integer> isGatherableAt = (r, c) -> {
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

    public abstract void update(); // let subclasses define their behavior per frame
}
