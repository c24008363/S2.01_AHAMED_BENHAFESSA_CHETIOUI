package jeu.personnages;

import javafx.scene.image.Image;
import jeu.objets.Bomb;

import java.util.Arrays;
import java.util.List;

public abstract class Character {
    protected int row;
    protected int col;
    protected int[][] gameMatrix;
    protected int id;
    protected Image image;
    private int bombCount = 1;

    public Character(int startRow, int startCol, int[][] gameMatrix, int id, Image image) {
        this.row = startRow;
        this.col = startCol;
        this.gameMatrix = gameMatrix;
        this.id = id;
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void moveUp() {
        if (canMoveTo(row - 1, col)) {
            row--;
        }
    }

    public void moveDown() {
        if (canMoveTo(row + 1, col)) {
            row++;
        }
    }

    public void moveLeft() {
        if (canMoveTo(row, col - 1)) {
            col--;
        }
    }

    public void moveRight() {
        if (canMoveTo(row, col + 1)) {
            col++;
        }
    }

    protected boolean canMoveTo(int newRow, int newCol) {
        // Check bounds
        if (newRow < 0 || newRow >= gameMatrix.length || newCol < 0 || newCol >= gameMatrix[0].length)
            return false;

        // Only allow movement onto empty tiles (value 0)
        return gameMatrix[newRow][newCol] == 0;
    }

    public Bomb placeBomb(int TileSize){
        return new Bomb(this.row, this.col, new Image(getClass().getResourceAsStream("/UI/005-bombFace.png"),
                        TileSize, TileSize, false, true), this, 1);

    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public abstract void update(); // let subclasses define their behavior per frame
}
