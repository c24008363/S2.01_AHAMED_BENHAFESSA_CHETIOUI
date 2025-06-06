package UI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jeu.objets.Bomb;
import jeu.objets.Explosion;
import jeu.objets.TimedExplosion;
import jeu.personnages.Player;

import java.util.*;
import java.time.Duration;
import java.time.Instant;

public class Main extends Application {



    private static int BoardSize = 15;
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

    private boolean player1BombPressed = false;
    private boolean player2BombPressed = false;


    private List<Bomb> bombs = new ArrayList<>();
    private List<Explosion> explosions = new ArrayList<>();
    private final List<TimedExplosion> timedExplosions = new ArrayList<>();


    public void setBombs(List<Bomb> bombs) {
        this.bombs = bombs;
    }

    @Override
    public void start(Stage primaryStage) {
        // Load images
        emptyImage = new Image(getClass().getResourceAsStream("/UI/000-floor.png"), TileSize, TileSize, false, true);
        IndestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/001-durable_wall.png"), TileSize, TileSize, false, true);
        DestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/002-destructible_wall.png"), TileSize, TileSize, false, true);
        player1Image = new Image(getClass().getResourceAsStream("/UI/hagried.jpg"), TileSize, TileSize, false, true);
        player2Image = new Image(getClass().getResourceAsStream("/UI/william.jpg"), TileSize, TileSize, false, true);

        Pane root = new Pane();
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
        player1 = new Player(1, 1, gameMatrix, 1, player1Image);
        player2 = new Player(BoardSize - 2, BoardSize - 2, gameMatrix, 2, player2Image);

        player1View = new ImageView(player1Image);
        player2View = new ImageView(player2Image);

        player1View.setFitWidth(TileSize);
        player1View.setFitHeight(TileSize);
        player2View.setFitWidth(TileSize);
        player2View.setFitHeight(TileSize);

        root.getChildren().addAll(player1View, player2View);

        updatePlayerViews();

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (activeKeys.contains(KeyCode.Z)) player1.moveUp();
                if (activeKeys.contains(KeyCode.S)) player1.moveDown();
                if (activeKeys.contains(KeyCode.Q)) player1.moveLeft();
                if (activeKeys.contains(KeyCode.D)) player1.moveRight();
                if (activeKeys.contains(KeyCode.E)) {
                    if (!player1BombPressed && player1.getBombCount() > 0) {
                        bombs.add(player1.placeBomb(TileSize));
                        player1BombPressed = true;
                    }
                } else {
                    player1BombPressed = false;
                }


                if (activeKeys.contains(KeyCode.I)) player2.moveUp();
                if (activeKeys.contains(KeyCode.K)) player2.moveDown();
                if (activeKeys.contains(KeyCode.J)) player2.moveLeft();
                if (activeKeys.contains(KeyCode.L)) player2.moveRight();
                if (activeKeys.contains(KeyCode.O)) {
                    if (!player2BombPressed && player2.getBombCount() > 0) {
                        bombs.add(player2.placeBomb(TileSize));
                        player2BombPressed = true;
                    }
                } else {
                    player2BombPressed = false;
                }

                updatePlayerViews();

                List<Bomb> explodedBombs = new ArrayList<>();
                for (Bomb bomb : bombs) {
                    if (bomb.countDown()) {
                        root.getChildren().remove(bomb.getImageView());
                        explodedBombs.add(bomb);

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

                List<TimedExplosion> expiredExplosions = new ArrayList<>();
                for (TimedExplosion te : timedExplosions) {
                    if ((now - te.getCreatedTimeNano()) >= 500_000_000L) { // 0.5 seconds in nanoseconds
                        root.getChildren().remove(te.getImageView());
                        expiredExplosions.add(te);
                    }
                }
                timedExplosions.removeAll(expiredExplosions);


                //fonction()
                bombs.removeAll(explodedBombs);


                updateBombView(root);

            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Bomberman super (javafx remake)");
        primaryStage.show();
    }


    private List<Explosion> generateExplosionsFromBomb(Bomb bomb) {
        List<Explosion> explosionList = new ArrayList<>();
        int x = bomb.getX();
        int y = bomb.getY();
        int range = 1; // you can make this dynamic later

        // Center explosion
        explosionList.add(new Explosion(x, y, 0));

        // Four directions
        int[][] directions = {
                {-1, 0}, // up
                {1, 0},  // down
                {0, -1}, // left
                {0, 1}   // right
        };

        for (int dir = 0; dir < directions.length; dir++) {
            for (int i = 1; i <= range; i++) {
                int nx = x + directions[dir][0] * i;
                int ny = y + directions[dir][1] * i;

                // Stop if out of bounds
                if (nx < 0 || ny < 0 || nx >= BoardSize || ny >= BoardSize) break;

                // Stop at indestructible walls
                if (gameMatrix[nx][ny] == 2) break;

                explosionList.add(new Explosion(nx, ny, dir + 1));

                // Stop after hitting destructible wall
                if (gameMatrix[nx][ny] == 1){
                    gameMatrix[nx][ny] = 0;
                    tileView[nx][ny].setImage(emptyImage);
                    break;
                }
            }
        }


        return explosionList;
    }


    private void updatePlayerViews() {
        player1View.setLayoutX(player1.getCol() * TileSize);
        player1View.setLayoutY(player1.getRow() * TileSize);
        player2View.setLayoutX(player2.getCol() * TileSize);
        player2View.setLayoutY(player2.getRow() * TileSize);
    }

    private void updateBombView(Pane root) {
        for (Bomb bomb: bombs){
            if (bomb.getImageView() == null){
                bomb.setImageView(new ImageView(new Image(getClass().getResourceAsStream("/UI/bomb.png"), TileSize, TileSize, false, true)));
                bomb.getImageView().setFitWidth(TileSize);
                bomb.getImageView().setFitHeight(TileSize);
                bomb.getImageView().setLayoutX(bomb.getY() * TileSize);
                bomb.getImageView().setLayoutY(bomb.getX() * TileSize);
                root.getChildren().add(bomb.getImageView());
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

    public static void main(String[] args) {
        launch(args);
    }
}
