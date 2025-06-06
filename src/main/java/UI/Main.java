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
import jeu.personnages.Player;

import java.util.*;

public class Main extends Application {

    private static int BoardSize = 15;
    private static int TileSize = 40;
    private int[][] gameMatrix = new int[BoardSize][BoardSize];

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

    private List<Bomb> bombs = new ArrayList<>();

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

                ImageView tileView = new ImageView(getImageForValue(gameMatrix[row][col]));
                tileView.setFitWidth(TileSize);
                tileView.setFitHeight(TileSize);
                tileView.setLayoutX(col * TileSize);
                tileView.setLayoutY(row * TileSize);
                root.getChildren().add(tileView);
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
                if (activeKeys.contains(KeyCode.E) && player1.getBombCount()>0) {
                    bombs.add(player1.placeBomb(TileSize));
                }
                System.out.println("aaaaaa  " + player1.getBombCount());

                if (activeKeys.contains(KeyCode.I)) player2.moveUp();
                if (activeKeys.contains(KeyCode.K)) player2.moveDown();
                if (activeKeys.contains(KeyCode.J)) player2.moveLeft();
                if (activeKeys.contains(KeyCode.L)) player2.moveRight();
                if (activeKeys.contains(KeyCode.O)) bombs.add(player2.placeBomb(TileSize));

                updatePlayerViews();

                for (Bomb bomb : bombs) {
                    if (bomb.countDown()) {
                        root.getChildren().remove(bomb.getImageView());
                    }
                }
                bombs.removeIf(bomb -> bomb.countDown());

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

    private void updatePlayerViews() {
        player1View.setLayoutX(player1.getCol() * TileSize);
        player1View.setLayoutY(player1.getRow() * TileSize);
        player2View.setLayoutX(player2.getCol() * TileSize);
        player2View.setLayoutY(player2.getRow() * TileSize);
    }

    private void updateBombView(Pane root) {
        for (Bomb bomb: bombs){
            if (bomb.getImageView() == null){
                bomb.setImageView(new ImageView(new Image(getClass().getResourceAsStream("/UI/005-bombFace.png"), TileSize, TileSize, false, true)));
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
