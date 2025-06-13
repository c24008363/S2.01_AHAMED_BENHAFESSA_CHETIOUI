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

/**
 * Core game class that manages the entire game loop, including the game board,
 * players, items, bombs, and game state.
 *
 * <p>This class handles initialization and runtime behavior for a Bomberman-style game.
 * It maintains the grid layout, user input, collisions, bomb placement and explosion timing,
 * and win condition logic.</p>
 *
 * <p>Game Matrix Encoding:
 * <ul>
 *     <li>0 - Empty tile</li>
 *     <li>1 - Destructible wall</li>
 *     <li>2 - Indestructible wall</li>
 *     <li>3 - Bomb</li>
 *     <li>4 - Item</li>
 * </ul>
 * </p>
 */
public class Game {

    /** Size of the game board in tiles (width and height). */
    private static int BoardSize = 20;

    /** Pixel size of each tile. */
    private static int TileSize = 40;

    // ===================== Game Board =====================

    /** Integer matrix representing the tile state of the board. */
    private final int[][] gameMatrix = new int[BoardSize][BoardSize];

    /** Visual representation of the board tiles using ImageViews. */
    private final ImageView[][] tileView = new ImageView[BoardSize][BoardSize];

    /** Root pane that holds all game visual elements. */
    private final Pane root = new Pane();

    // ===================== Tile Images =====================

    /** Image for empty tiles. */
    private Image emptyImage;

    /** Image for destructible walls. */
    private Image DestructibleWallImage;

    /** Image for indestructible walls. */
    private Image IndestructibleWallImage;

    /** Player 1 sprite image. */
    private Image player1Image;

    /** Player 2 sprite image. */
    private Image player2Image;

    // ===================== Players =====================

    /** Player 1 object instance. */
    private Player player1;

    /** Player 2 object instance. */
    private Player player2;

    /** Player 1 sprite on screen. */
    private ImageView player1View;

    /** Player 2 sprite on screen. */
    private ImageView player2View;

    /**
     * Delay in frames after which player 1 can move again.
     * The higher the value, the slower the player.
     */
    private int playerSpeed = 0;

    /**
     * Delay in frames after which player 2 can move again.
     * The higher the value, the slower the player.
     */
    private int player2Speed = 0;

    // ===================== Input Handling =====================

    /** Tracks currently pressed keys. */
    private final Set<KeyCode> activeKeys = new HashSet<>();

    /** Whether player 1 is currently attempting to place a bomb. */
    private boolean player1BombPressed = false;

    /** Whether player 2 is currently attempting to place a bomb. */
    private boolean player2BombPressed = false;

    // ===================== Game State =====================

    /** True if the game has ended. */
    private boolean gameOver = false;

    /** Winner identifier, used for display purposes. */
    private String winner = "";

    /** Status flag for player 1's life. */
    private boolean player1Alive = true;

    /** Status flag for player 2's life. */
    private boolean player2Alive = true;

    // ===================== Bombs and Explosions =====================

    /** List of all active bombs on the field. */
    private final List<Bomb> bombs = new ArrayList<>();

    /** List of all active explosions (for rendering and collision). */
    private final List<Explosion> explosions = new ArrayList<>();

    /**
     * List of timed explosions managing the duration of each explosion.
     * Each TimedExplosion tracks how long its associated explosion will remain active.
     */
    private final List<TimedExplosion> timedExplosions = new ArrayList<>();

    // ===================== Items =====================

    /** List of collectible items currently active in the game. */
    private final List<Gatherable> items = new ArrayList<>();

    /**
     * Temporary holder for an item a player is interacting with.
     * When a player steps on an item, it's stored here for processing,
     * then removed from {@code items}.
     */
    private Gatherable tempItem;




    /**
     * Creates a new Game instance with default settings.
     * Initializes the game board, players, and game loop.
     */
    public Game() {
        initGame();
    }

    /**
     * Creates a new Game instance with specified board size and tile size.
     * Ensures the board size is odd by incrementing if an even number is provided.
     * Initializes the game board, players, and game loop.
     *
     * @param BoardsSize the desired size of the game board (must be an odd number)
     * @param TileSize the size in pixels of each tile on the board
     */
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

    /**
     * Returns the size of the game board (number of tiles per side).
     *
     * @return the board size
     */
    public int getBoardSize() {
        return BoardSize;
    }

    /**
     * Returns the size of each tile in pixels.
     *
     * @return the tile size
     */
    public int getTileSize() {
        return TileSize;
    }




    // #--- INITIALISATION DU JEU ET COMMENCEMENT DE LA PARTIE




    /**
     * Initializes the game board, players, images, and starts the main game loop.
     *
     * <p>The game matrix is populated with walls, destructible blocks, and empty spaces.
     * Player positions and images are initialized. An AnimationTimer is started to handle
     * player movement, bomb placement and explosions, item pickups, and game state updates.
     *
     * <p>Key controls:
     * <ul>
     *   <li>Player 1: Z, Q, S, D to move; E to place bomb</li>
     *   <li>Player 2: I, J, K, L to move; O to place bomb</li>
     * </ul>
     */
    private void initGame() {
        // Load images
        loadImagesAndProfiles();




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
                    try {
                        MainMenu.getStats1().incrementItemsCollected();
                    }
                    catch (Exception e) {
                        System.err.println("No profile loaded for player 1");
                    }

                    useItem(tempItem, player1, player2);
                    tileView[tempItem.getRow()][tempItem.getCol()].setImage(emptyImage);
                    gameMatrix[tempItem.getRow()][tempItem.getCol()] = 0;
                    items.remove(tempItem);
                }
                tempItem = player2.isOnGatherable(items, TileSize);
                if(tempItem != null){
                    try {
                        MainMenu.getStats2().incrementItemsCollected();
                    }
                    catch (Exception e) {
                        System.err.println("No profile loaded for player 2");
                    }
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

    // #--- INIT GAME BUT WHEN YOU RESTART TO AVOID ISSUES ---#

    /**
     * Initializes or resets the game board and players without restarting the game loop.
     * This method is intended for resetting the board state when restarting the game
     * to avoid re-creating the animation timer or other resources.
     *
     * <p>Like {@link #initGame()}, it creates the matrix of walls and blocks, and resets player positions and views.
     * However, it does not start the game loop timer.
     */
    public void initGameRestart(){

        loadImagesAndProfiles();

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




    //  #--- FONCTIONS ELEMENTAIRES DU JEU ---#




    /**
     * Ends the game by setting the gameOver flag and winner name.
     * Saves player statistics if profiles are loaded.
     * Prints the game over message and can be extended for additional end-game logic.
     *
     * @param winner the name of the winning player or "Draw"
     */
    private void endGame(String winner) {
        this.gameOver = true;
        this.winner = winner;
        try {
            MainMenu.getStats1().save();
        }
        catch (Exception e) {
            System.err.println("No profile loaded for player 1");
        }
        try {
            MainMenu.getStats2().save();
        }
        catch (Exception e) {
            System.err.println("No profile loaded for player 2");
        }
        System.out.println("Game Over! Winner: " + winner);

        // You can add more end game logic here:
        // - Show game over screen
        // - Display restart button
        // - Save high scores
        // - etc.
    }

    /**
     * Attaches key event handlers to the given Scene.
     * Tracks pressed keys for gameplay input and listens for 'R' key to restart the game if over.
     *
     * @param scene the JavaFX Scene to attach key handlers to
     */
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

    /**
     * Restarts the game by resetting all relevant game state variables, clearing bombs and explosions,
     * resetting player positions and visibility, and preparing the game for a new round.
     */
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

    /**
     * Checks if any player is currently in an active explosion and handles player elimination accordingly.
     */
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

    /**
     * Handles the logic when a player is hit by an explosion.
     * Updates player alive state, provides visual feedback, updates stats, and ends the game if needed.
     *
     * @param playerNumber the player number (1 or 2) that was hit
     */
    private void handlePlayerHit(int playerNumber) {
        if (playerNumber == 1) {
            player1Alive = false;
            // Visual feedback - make player semi-transparent or change color
            player1View.setOpacity(0.3);
            System.out.println("Player 1 has been eliminated!");

            if (player2Alive) {
                MainMenu.getStats2().incrementGamesWon();
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
                MainMenu.getStats1().incrementGamesWon();
                endGame("Player 1");
            } else {
                endGame("Draw");
            }
        }
    }

    /**
     * Applies an item effect depending on whether it is a buff or debuff,
     * applying it either to the character or the opposing character.
     *
     * @param g the Gatherable item being used
     * @param character the character using or receiving the item effect
     * @param otherCharacter the other character in the game
     */
    private void useItem(Gatherable g, jeu.personnages.Character character, jeu.personnages.Character otherCharacter) {
        if (g.isBuff()){
            g.applyEffect(character);
        }
        else{
            g.applyEffect(otherCharacter);
        }
    }

    /**
     * Generates a list of explosion tiles caused by a bomb, considering its position and range.
     * Updates the game matrix when destructible blocks are destroyed,
     * increments block destruction stats for the bomb owner,
     * and randomly spawns items on destroyed tiles.
     *
     * @param bomb the Bomb triggering the explosions
     * @return a list of Explosion objects representing affected tiles
     */
    private List<Explosion> generateExplosionsFromBomb(Bomb bomb) {
        List<Explosion> explosionList = new ArrayList<>();
        int x = bomb.getX();
        int y = bomb.getY();
        int range = bomb.getCharacter().getRange();

        explosionList.add(new Explosion(x, y));

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int dir = 0; dir < directions.length; dir++) {
            for (int i = 1; i <= range; i++) {
                int nx = x + directions[dir][0] * i;
                int ny = y + directions[dir][1] * i;

                if (nx < 0 || ny < 0 || nx >= BoardSize || ny >= BoardSize) break;
                if (gameMatrix[nx][ny] == 2) break;

                explosionList.add(new Explosion(nx, ny));

                if (gameMatrix[nx][ny] == 1) {
                    gameMatrix[nx][ny] = 0;
                    if (player1!=null) {
                        if (bomb.getCharacter() == player1) {
                            try {
                                MainMenu.getStats1().incrementBlocksDestroyed();
                            }
                            catch (Exception e) {
                                System.err.println("No profile loaded for player 1");
                            }

                        }
                        else {
                            try {
                                MainMenu.getStats2().incrementBlocksDestroyed();
                            }
                            catch (Exception e) {
                                System.err.println("No profile loaded for player 2");
                            }
                        }
                    }
                    tileView[nx][ny].setImage(emptyImage);

                    // 1 in 5 chance to spawn a random item
                    if (new Random().nextInt(5) == 0) {
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


    /**
     * Determines if a player is within the area of an explosion based on tile positions and size.
     * Checks the player's center and corners for collision with the explosion tile.
     *
     * @param player the player to check for explosion collision
     * @param explosionRow the row index of the explosion tile
     * @param explosionCol the column index of the explosion tile
     * @param tileSize the size of a tile in pixels
     * @return true if the player is in the explosion tile area, false otherwise
     */
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

    /**
     * Smoothly updates the visual positions of the players on the game board.
     *
     * <p>If player 1 is alive, updates the position of {@code player1View} to the current coordinates of {@code player1}.
     * Similarly, if player 2 is alive, updates {@code player2View} position to match {@code player2}'s coordinates.
     *
     * <p>This method directly sets the layout positions to reflect player movement smoothly.
     */
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

    /**
     * Adds visual representations of bombs to the game pane if they do not already exist.
     *
     * <p>For each bomb in the {@code bombs} collection, if the bomb's image view has not yet been added,
     * sets its size and position according to the tile size and bomb's coordinates, adds it to the root pane,
     * and marks the bomb as existing in the scene.
     *
     * @param root the Pane to which bomb image views should be added
     */
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


    /**
     * Returns the image corresponding to a given tile value.
     *
     * @param val the integer representing the type of tile:
     *            <ul>
     *              <li>1 - Destructible wall</li>
     *              <li>2 - Indestructible wall</li>
     *              <li>any other value - empty tile</li>
     *            </ul>
     * @return the Image object for the specified tile type
     */
    private Image getImageForValue(int val) {
        return switch (val) {
            case 1 -> DestructibleWallImage;
            case 2 -> IndestructibleWallImage;
            default -> emptyImage;
        };
    }

    /**
     * Loads the images for the game tiles and players according to the current theme set in MainMenu.
     *
     * <p>This method attempts to load images for:
     * <ul>
     *   <li>Floor (empty tile)</li>
     *   <li>Indestructible wall</li>
     *   <li>Destructible wall</li>
     *   <li>Player 1</li>
     *   <li>Player 2</li>
     * </ul>
     * If any image is not found in the current theme directory, it falls back to loading from the default theme directory.
     *
     * <p>After loading the images, it attempts to increment the number of games played for both player profiles
     * using {@code MainMenu.getStats1().incrementGamesPlayed()} and {@code MainMenu.getStats2().incrementGamesPlayed()}.
     * If profiles are not loaded, it catches exceptions and prints an error message.
     *
     * <p>This method should be called after setting the theme and loading player profiles to ensure images and stats
     * are properly initialized.
     */
    private void loadImagesAndProfiles(){
        try{
            emptyImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"floor.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Floor image not found. Using default.");
            emptyImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"floor.png"), TileSize, TileSize, false, true);
        }
        try{
            IndestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Durable Wall image not found. Using default.");
            IndestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"durable_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            DestructibleWallImage = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Destructible wall image not found. Using default.");
            DestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"destructible_wall.png"), TileSize, TileSize, false, true);
        }
        try{
            player1Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player1.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Player1 image not found. Using default.");
            player1Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player1.jpg"), TileSize, TileSize, false, true);
        }
        try{
            player2Image = new Image(getClass().getResourceAsStream(MainMenu.getTheme()+"player2.jpg"), TileSize, TileSize, false, true);
        }
        catch (Exception e) {
            // Fallback to default
            System.err.println("Player2 image not found. Using default.");
            player2Image = new Image(getClass().getResourceAsStream("/UI/themes/default/"+"player2.jpg"), TileSize, TileSize, false, true);
        }
        try {
            MainMenu.getStats1().incrementGamesPlayed();
        }
        catch (Exception e) {
            System.err.println("No profile loaded for player 1");
        }
        try {
            MainMenu.getStats2().incrementGamesPlayed();
        }
        catch (Exception e) {
            System.err.println("No profile loaded for player 2");
        }

    }

    public Pane getRoot() {
        return root;
    }
}