package jeu.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.personnages.Character;

public abstract class Gatherable {
    protected int row;
    protected int col;
    protected ImageView imageView;
    protected boolean isBuff = true;

    public Gatherable(int row, int col, int tileSize, Image image) {
        this.row = row;
        this.col = col;
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(tileSize);
        this.imageView.setFitHeight(tileSize);
        this.imageView.setLayoutX(col * tileSize);
        this.imageView.setLayoutY(row * tileSize);
    }


    public ImageView getImageView() {
        return imageView;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isBuff() {
        return isBuff;
    }

    public abstract void applyEffect(Character character);

    private Gatherable getRandomItem(int row, int col, int tileSize) {
        // Add more item types later
        return new BombUp(row, col, tileSize);
    }
}


