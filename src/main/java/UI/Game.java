package UI;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import jeu.items.BombUp;
import jeu.items.FlameUp;
import jeu.items.Gatherable;
import jeu.objets.Bomb;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import jeu.objets.Explosion;
import jeu.objets.TimedExplosion;
import jeu.personnages.Character;
import jeu.personnages.Player;

import java.util.*;

public class Game {
    private static int BoardSize = 20;
    private static int TileSize = 40;
    private int[][] gameMatrix = new int[BoardSize][BoardSize];

    private ImageView[][] tileView = new ImageView[BoardSize][BoardSize];

    private Image emptyImage;
    private Image DestructibleWallImage;
    private Image IndestructibleWallImage;
    private Image player1Image;
    private Image player2Image;

    private final Set<KeyCode> activeKeys = new HashSet<>();
    private Player player1;
    private Player player2;
    private ImageView player1View;
    private ImageView player2View;

    private Gatherable tempItem;

    private int playerSpeed = 0; //The higher the slower
    private int player2Speed = 0;

    private boolean player1BombPressed = false;
    private boolean player2BombPressed = false;

    // Game state variables
    private boolean gameOver = false;
    private String winner = "";
    private boolean player1Alive = true;
    private boolean player2Alive = true;

    private PlayerStats playerStats1;
    private PlayerStats playerStats2;

    private List<Bomb> bombs = new ArrayList<>();
    private List<Explosion> explosions = new ArrayList<>();
    private final List<TimedExplosion> timedExplosions = new ArrayList<>();
    private List<Gatherable> items = new ArrayList<>();


    public void setBombs(List<Bomb> bombs) {
        this.bombs = bombs;
    }

    private Pane root = new Pane();

    public Game() {
        initGame();
    }

    public Game(int BoardsSize, int TileSize) {
        if (BoardsSize % 2 == 0) {
            this.BoardSize = BoardsSize + 1;
            this.TileSize = TileSize;
        } else {
            this.BoardSize = BoardsSize;
            this.TileSize = TileSize;
        }
        initGame();
    }

    public int getBoardSize() {
        return BoardSize;
    }

    public int getTileSize() {
        return TileSize;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    private void initGame() {
        // Load images
        try{
            emptyImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"floor.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            emptyImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"floor.png"), TileSize, TileSize, false, true);
        }
        try{
            IndestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            IndestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            DestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            DestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            player1Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player1.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            player1Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player1.jpg"), TileSize, TileSize, false, true);
        }
        try{
            player2Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player2.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            player2Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player2.jpg"), TileSize, TileSize, false, true);
        }

        //MainMenu.getStats1().incrementGamesPlayed();
        //MainMenu.getStats2().incrementGamesPlayed();




        Random random = new Random();

        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                if (row == 0 || row == BoardSize - 1 || col == 0 || col == BoardSize - 1 || (row % 2 == 0 && col % 2 == 0)) {
                    gameMatrix[row][col] = 2;
                } else {
                    gameMatrix[row][col] = (random.nextInt(100) < 80) ? 1 : 0;
                }

                if ((row == 1 && col == 1) || (row == 1 && col == 2) || (row == 2 && col == 1) ||
                        (row == 1 && col == BoardSize - 3) || (row == 1 && col == BoardSize - 2) || (row == 2 && col == BoardSize - 2) ||
                        (row == BoardSize - 2 && col == BoardSize - 3) || (row == BoardSize - 2 && col == BoardSize - 2) || (row == BoardSize - 3 && col == BoardSize - 2) ||
                        (row == BoardSize - 2 && col == 1) || (row == BoardSize - 2 && col == 2) || (row == BoardSize - 3 && col == 1)) {
                    gameMatrix[row][col] = 0;
                }

                tileView[row][col] = new ImageView(getImageForValue(gameMatrix[row][col]));
                tileView[row][col].setFitWidth(TileSize);
                tileView[row][col].setFitHeight(TileSize);
                tileView[row][col].setLayoutX(col * TileSize);
                tileView[row][col].setLayoutY(row * TileSize);
                root.getChildren().add(tileView[row][col]);
            }
        }

        // Setup players
        player1 = new Player(1, 1, gameMatrix, 1, player1Image, TileSize);
        player2 = new Player(BoardSize - 2, BoardSize - 2, gameMatrix, 2, player2Image, TileSize);

        player1View = new ImageView(player1Image);
        player2View = new ImageView(player2Image);

        player1View.setFitWidth(TileSize - 5);
        player1View.setFitHeight(TileSize - 5);
        player2View.setFitWidth(TileSize - 5);
        player2View.setFitHeight(TileSize - 5);

        root.getChildren().addAll(player1View, player2View);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Skip game logic if game is over
                if (gameOver) {
                    explosions.removeAll(timedExplosions);
                    return;
                }

                // # -- JOUEUR 1 MOUVEMENT -- #
                if (playerSpeed == 0 && player1Alive) {
                    if (activeKeys.contains(KeyCode.Z)) player1.moveUp(TileSize);
                    if (activeKeys.contains(KeyCode.S)) player1.moveDown(TileSize);
                    if (activeKeys.contains(KeyCode.Q)) player1.moveLeft(TileSize);
                    if (activeKeys.contains(KeyCode.D)) player1.moveRight(TileSize);
                    if (activeKeys.contains(KeyCode.E)) {
                        if (!player1BombPressed && player1.getBombCount() > 0) {
                            bombs.add(player1.placeBomb(TileSize));
                            gameMatrix[player1.getRow()][player1.getCol()] = 3;
                            player1.setInBomb(true);
                            player1BombPressed = true;
                        }
                    } else {
                        player1BombPressed = false;
                    }
                    playerSpeed = 0;
                } else {
                    playerSpeed -= 1;
                }

                // # -- JOUEUR 2 MOUVEMENT -- #
                if (player2Speed == 0 && player2Alive) {
                    if (activeKeys.contains(KeyCode.I)) player2.moveUp(TileSize);
                    if (activeKeys.contains(KeyCode.K)) player2.moveDown(TileSize);
                    if (activeKeys.contains(KeyCode.J)) player2.moveLeft(TileSize);
                    if (activeKeys.contains(KeyCode.L)) player2.moveRight(TileSize);
                    if (activeKeys.contains(KeyCode.O)) {
                        if (!player2BombPressed && player2.getBombCount() > 0) {
                            bombs.add(player2.placeBomb(TileSize));
                            gameMatrix[player2.getRow()][player2.getCol()] = 3;
                            player2BombPressed = true;
                            player2.setInBomb(true);
                        }
                    } else {
                        player2BombPressed = false;
                    }
                    player2Speed = 0;
                } else {
                    player2Speed -= 1;
                }

                updatePlayerViewsSmooth();

                tempItem = player1.isOnGatherable(items, TileSize);
                if(tempItem != null){
                    useItem(tempItem, player1, player2);
                    tileView[tempItem.getRow()][tempItem.getCol()].setImage(emptyImage);
                    gameMatrix[tempItem.getRow()][tempItem.getCol()] = 0;
                    items.remove(tempItem);
                }
                tempItem = player2.isOnGatherable(items, TileSize);
                if(tempItem != null){
                    useItem(tempItem, player2, player1);
                    tileView[tempItem.getRow()][tempItem.getCol()].setImage(emptyImage);
                    gameMatrix[tempItem.getRow()][tempItem.getCol()] = 0;
                    items.remove(tempItem);
                }
                tempItem = null;



                // Handle bomb explosions
                List<Bomb> explodedBombs = new ArrayList<>();
                for (Bomb bomb : bombs) {
                    if (bomb.countDown()) {
                        root.getChildren().remove(bomb.getImageView());
                        explodedBombs.add(bomb);
                        gameMatrix[bomb.getX()][bomb.getY()] = 0;

                        List<Explosion> newExplosions = generateExplosionsFromBomb(bomb);

                        for (Explosion exp : newExplosions) {
                            ImageView explosionView = new ImageView(new Image(getClass().getResourceAsStream("/UI/flame.png"), TileSize, TileSize, false, true));
                            explosionView.setLayoutX(exp.getY() * TileSize);
                            explosionView.setLayoutY(exp.getX() * TileSize);
                            explosionView.setFitWidth(TileSize);
                            explosionView.setFitHeight(TileSize);
                            root.getChildren().add(explosionView);

                            timedExplosions.add(new TimedExplosion(exp, explosionView, now));
                        }
                    }
                }

                // Check for player hits during active explosions
                checkPlayerHits();

                // Remove expired explosions
                List<TimedExplosion> expiredExplosions = new ArrayList<>();
                for (TimedExplosion te : timedExplosions) {
                    if ((now - te.getCreatedTimeNano()) >= 500_000_000L) {
                        root.getChildren().remove(te.getImageView());
                        expiredExplosions.add(te);
                    }
                }
                timedExplosions.removeAll(expiredExplosions);

                bombs.removeAll(explodedBombs);
                updateBombView(root);
            }
        };
        timer.start();
    }

    private void checkPlayerHits() {
        // Check if either player is hit by any active explosion
        for (TimedExplosion timedExp : timedExplosions) {
            Explosion exp = timedExp.getExplosion();

            // Check Player 1
            if (player1Alive && isPlayerInExplosion(player1, exp.getX(), exp.getY(), TileSize)) {
                handlePlayerHit(1);
            }

            // Check Player 2
            if (player2Alive && isPlayerInExplosion(player2, exp.getX(), exp.getY(), TileSize)) {
                handlePlayerHit(2);
            }
        }
    }

    private void handlePlayerHit(int playerNumber) {
        if (playerNumber == 1) {
            player1Alive = false;
            // Visual feedback - make player semi-transparent or change color
            player1View.setOpacity(0.3);
            System.out.println("Player 1 has been eliminated!");

            if (player2Alive) {
                endGame("Player 2");
            } else {
                endGame("Draw");
            }
        } else if (playerNumber == 2) {
            player2Alive = false;
            // Visual feedback - make player semi-transparent or change color
            player2View.setOpacity(0.3);
            System.out.println("Player 2 has been eliminated!");

            if (player1Alive) {
                endGame("Player 1");
            } else {
                endGame("Draw");
            }
        }
    }

    public void initGameRestart(){

        try{
            emptyImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"floor.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            emptyImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"floor.png"), TileSize, TileSize, false, true);
        }
        try{
            IndestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            IndestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            DestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            DestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            player1Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player1.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            player1Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player1.jpg"), TileSize, TileSize, false, true);
        }
        try{
            player2Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player2.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Custom theme image not found. Using default.");
            player2Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player2.jpg"), TileSize, TileSize, false, true);
        }

        Random random = new Random();

        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                if (row == 0 || row == BoardSize - 1 || col == 0 || col == BoardSize - 1 || (row % 2 == 0 && col % 2 == 0)) {
                    gameMatrix[row][col] = 2;
                } else {
                    gameMatrix[row][col] = (random.nextInt(100) < 80) ? 1 : 0;
                }

                if ((row == 1 && col == 1) || (row == 1 && col == 2) || (row == 2 && col == 1) ||
                        (row == 1 && col == BoardSize - 3) || (row == 1 && col == BoardSize - 2) || (row == 2 && col == BoardSize - 2) ||
                        (row == BoardSize - 2 && col == BoardSize - 3) || (row == BoardSize - 2 && col == BoardSize - 2) || (row == BoardSize - 3 && col == BoardSize - 2) ||
                        (row == BoardSize - 2 && col == 1) || (row == BoardSize - 2 && col == 2) || (row == BoardSize - 3 && col == 1)) {
                    gameMatrix[row][col] = 0;
                }

                tileView[row][col] = new ImageView(getImageForValue(gameMatrix[row][col]));
                tileView[row][col].setFitWidth(TileSize);
                tileView[row][col].setFitHeight(TileSize);
                tileView[row][col].setLayoutX(col * TileSize);
                tileView[row][col].setLayoutY(row * TileSize);
                root.getChildren().add(tileView[row][col]);
            }
        }
        // Setup players
        player1 = new Player(1, 1, gameMatrix, 1, player1Image, TileSize);
        player2 = new Player(BoardSize - 2, BoardSize - 2, gameMatrix, 2, player2Image, TileSize);

        player1View = new ImageView(player1Image);
        player2View = new ImageView(player2Image);

        player1View.setFitWidth(TileSize - 5);
        player1View.setFitHeight(TileSize - 5);
        player2View.setFitWidth(TileSize - 5);
        player2View.setFitHeight(TileSize - 5);

        root.getChildren().addAll(player1View, player2View);

    }

    private void endGame(String winner) {
        this.gameOver = true;
        this.winner = winner;
        System.out.println("Game Over! Winner: " + winner);

        // You can add more end game logic here:
        // - Show game over screen
        // - Display restart button
        // - Save high scores
        // - etc.
    }

    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!gameOver) {
                activeKeys.add(e.getCode());
            }
            // Add restart functionality
            if (gameOver && e.getCode() == KeyCode.R) {
                restartGame();
            }
        });
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    private void restartGame() {
        // Reset game state
        initGameRestart();
        gameOver = false;
        winner = "";
        player1Alive = true;
        player2Alive = true;

        // Clear all bombs and explosions
        bombs.clear();
        timedExplosions.clear();

        // Reset player positions and states
        player1 = new Player(1, 1, gameMatrix, 1, player1Image, TileSize);
        player2 = new Player(BoardSize - 2, BoardSize - 2, gameMatrix, 2, player2Image, TileSize);

        // Reset visual elements
        player1View.setOpacity(1.0);
        player2View.setOpacity(1.0);

        System.out.println("Game restarted! Press R to restart when game over.");
    }

    private void useItem(Gatherable g, jeu.personnages.Character character, jeu.personnages.Character otherCharacter) {
        if (g.isBuff()){
            g.applyEffect(character);
        }
        else{
            g.applyEffect(otherCharacter);
        }
    }

    private List<Explosion> generateExplosionsFromBomb(Bomb bomb) {
        List<Explosion> explosionList = new ArrayList<>();
        int x = bomb.getX();
        int y = bomb.getY();
        int range = bomb.getCharacter().getRange();

        explosionList.add(new Explosion(x, y, 0));

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int dir = 0; dir < directions.length; dir++) {
            for (int i = 1; i <= range; i++) {
                int nx = x + directions[dir][0] * i;
                int ny = y + directions[dir][1] * i;

                if (nx < 0 || ny < 0 || nx >= BoardSize || ny >= BoardSize) break;
                if (gameMatrix[nx][ny] == 2) break;

                explosionList.add(new Explosion(nx, ny, dir + 1));

                if (gameMatrix[nx][ny] == 1) {
                    gameMatrix[nx][ny] = 0;
                    tileView[nx][ny].setImage(emptyImage);

                    // 1 in 5 chance to spawn a random item
                    if (new Random().nextInt(1) == 0) {
                        int randomNum = new Random().nextInt(2); //INCREASE THE NEXT INT (X) IF NEW ITEM IMPLEMENTED, AND ADAPT THE SWITCH CASE
                        Gatherable item;

                        switch (randomNum) {
                            case 0:
                                item = new BombUp(nx, ny, TileSize);
                                items.add(item);
                                break;
                            case 1:
                                item = new FlameUp(nx, ny, TileSize);
                                items.add(item);
                                break;
                            default:
                                item = null; // or handle other cases if needed
                                break;
                        }

                        gameMatrix[nx][ny] = 4; // item tile type
                        tileView[nx][ny].setImage(item.getImageView().getImage());
                        // You should also store this item somewhere to apply its effect on collision
                    }
                    break;
                }
            }
        }

        return explosionList;
    }


    private boolean isPlayerInExplosion(Player player, int explosionRow, int explosionCol, int tileSize) {
        // Get player's current tile position
        int playerRow = player.getY() / tileSize;
        int playerCol = player.getX() / tileSize;

        // Check if player's center is in the explosion tile
        if (playerRow == explosionRow && playerCol == explosionCol) {
            return true;
        }

        // Also check player's corners for more precise collision
        int playerX = player.getX();
        int playerY = player.getY();

        // Check all 4 corners of the player
        boolean topLeft = ((playerY + 15) / tileSize == explosionRow) && ((playerX + 15) / tileSize == explosionCol);
        boolean topRight = ((playerY + 15) / tileSize == explosionRow) && ((playerX + tileSize - 15) / tileSize == explosionCol);
        boolean bottomLeft = ((playerY + tileSize - 15) / tileSize == explosionRow) && ((playerX+15) / tileSize == explosionCol);
        boolean bottomRight = ((playerY + tileSize - 15) / tileSize == explosionRow) && ((playerX + tileSize - 15) / tileSize == explosionCol);

        return topLeft || topRight || bottomLeft || bottomRight;
    }

    private void updatePlayerViewsSmooth() {
        if (player1Alive) {
            player1View.setLayoutX(player1.getX());
            player1View.setLayoutY(player1.getY());
        }
        if (player2Alive) {
            player2View.setLayoutX(player2.getX());
            player2View.setLayoutY(player2.getY());
        }
    }

    private void updateBombView(Pane root) {
        for (Bomb bomb : bombs) {
            if (!bomb.isExists()) {
                bomb.setImageView(bomb.getImageView());
                bomb.getImageView().setFitWidth(TileSize);
                bomb.getImageView().setFitHeight(TileSize);
                bomb.getImageView().setLayoutX(bomb.getY() * TileSize);
                bomb.getImageView().setLayoutY(bomb.getX() * TileSize);
                root.getChildren().add(bomb.getImageView());
                bomb.setExists(true);
            }
        }
    }

    private Image getImageForValue(int val) {
        return switch (val) {
            case 1 -> DestructibleWallImage;
            case 2 -> IndestructibleWallImage;
            default -> emptyImage;
        };
    }

    public Pane getRoot() {
        return root;
    }
}