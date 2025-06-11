package jeu.items;

import UI.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jeu.personnages.Character;

public abstract class Gatherable {
    protected int row;
    protected int col;
    protected ImageView imageView;
    protected boolean isBuff = true;
    protected Image sprite;

    public Gatherable(int row, int col, int tileSize, String sprite) {
        this.row = row;
        this.col = col;
        this.imageView = new ImageView(setSprite(sprite));
        this.imageView.setFitWidth(tileSize);
        this.imageView.setFitHeight(tileSize);
        this.imageView.setLayoutX(col * tileSize);
        this.imageView.setLayoutY(row * tileSize);
    }

    public Image setSprite(String path) {
        try{
            sprite = new Image(BombUp.class.getResourceAsStream(MainMenu.getTheme()+path));
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            sprite = new Image(BombUp.class.getResourceAsStream("/UI/themes/default/"+path));
        }
        return sprite;
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


