package jeu.personnages;

public abstract class Character {
    protected int row;
    protected int col;
    protected int[][] gameMatrix;
    protected int id;

    public Character(int startRow, int startCol, int[][] gameMatrix, int id) {
        this.row = startRow;
        this.col = startCol;
        this.gameMatrix = gameMatrix;
        this.id = id;
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

    public abstract void update(); // let subclasses define their behavior per frame
}
