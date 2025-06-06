package jeu.personnages;

import javafx.scene.image.Image;
import jeu.objets.Bomb;

public abstract class Character {
    protected int row;
    protected int col;
    private int x;
    private int y;
    protected int[][] gameMatrix;
    protected int id;
    protected Image image;
    private int bombCount = 1;

    public Character(int startRow, int startCol, int[][] gameMatrix, int id, Image image, int tileSize) {
        this.row = startRow;
        this.col = startCol;
        this.gameMatrix = gameMatrix;
        this.id = id;
        this.image = image;
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

    public void moveUp(int tileSize) {
        if (canMoveToRework(y-2, x, tileSize )) {
            setY(getY() - 1);
            row = (getY())/tileSize;
            System.out.print("y="+y);
            System.out.println("row="+row);
        }
    }

    public void moveDown(int tileSize) {
        if (canMoveToRework(y+2, x, tileSize )) {
            setY(getY() + 1);
            row = (getY()+(tileSize-5)/2)/tileSize;
            System.out.print("y="+y);
            System.out.println("row="+row);
        }

    }

    public void moveLeft(int tileSize) {
        if (canMoveToRework(y, (x-2), tileSize )) {
            setX(getX() - 1);
            col = (getX())/tileSize;
            System.out.print("x="+x);
            System.out.println("col="+col);

        }
    }

    public void moveRight(int tileSize) {
        if (canMoveToRework(y, (x+2), tileSize )){
            setX(getX() + 1);
            col = (getX()+(tileSize-5)/2)/tileSize;
            System.out.print("x="+x);
            System.out.println("col="+col);

        }
    }


    protected boolean canMoveToRework(int x, int y, int TileSize) {
        if (x / TileSize <= 0 || y / TileSize <= 0 || x / TileSize >= gameMatrix.length || y / TileSize >= gameMatrix[0].length)
            return false;

        return gameMatrix[(x) / TileSize][(y) / TileSize] == 0 //haut gauche
                &&
                gameMatrix[(x+(TileSize-5)) / TileSize][(y+(TileSize-5)) / TileSize] == 0 //bas droite
                &&
                gameMatrix[((x)+TileSize-5) / TileSize][(y) / TileSize] == 0 //haut droite
                &&
                gameMatrix[(x) / TileSize][(y+TileSize-5) / TileSize] == 0; //bas gauche

    }

    public Bomb placeBomb(int TileSize){
        bombCount = bombCount -1;
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
