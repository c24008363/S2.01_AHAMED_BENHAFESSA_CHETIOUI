package jeu.terrains;

import javafx.scene.layout.Pane;

public class Terrain {
    private final Tile[][] grid;
    private final int cols;
    private final int rows;
    private final double tileSize;

    private Pane pane;

    public Terrain(int cols, int rows, double tileSize){
        this.cols = cols;
        this.rows = rows;
        this.tileSize = tileSize;
        this.grid = new Tile[rows][cols];
        generateTerrain();
    }

    private void generateTerrain() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                TileType type;

                // Laisse toujours les murs autour
                if (y == 0 || y == rows - 1 || x == 0 || x == cols - 1) {
                    type = TileType.WALL;

                    // Zone de spawn du joueur haut-gauche : (1,1), (1,2), (2,1)
                } else if ((x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)) {
                    type = TileType.EMPTY;

                    // Zone de spawn du joueur bas-droite : (cols-2, rows-2), (cols-2, rows-3), (cols-3, rows-2)
                } else if ((x == cols - 2 && y == rows - 2) ||
                        (x == cols - 2 && y == rows - 3) ||
                        (x == cols - 3 && y == rows - 2)) {
                    type = TileType.EMPTY;

                    // Murs fixes aux croisements (style Bomberman)
                } else if (x % 2 == 0 && y % 2 == 0) {
                    type = TileType.WALL;

                    // Le reste : 50% destructible, 50% vide
                } else {
                    type = Math.random() < 0.5 ? TileType.DESTRUCTIBLE : TileType.EMPTY;
                }

                grid[y][x] = new Tile(type, tileSize, x * tileSize, y * tileSize);
            }
        }
    }

    public double getTileSize() {
        return tileSize;
    }

    public Tile[][] getGrid(){
        return grid;
    }

    public Tile getTileAt(double x, double y){
        int col = (int) (x / tileSize);
        int row = (int) (y / tileSize);
        if (col < 0 || col >= cols || row < 0 || row >= rows) return null;
        return grid[row][col];
    }

    public void setPane(Pane pane){
        this.pane = pane;
    }

    public Pane getPane(){
        return pane;
    }
}