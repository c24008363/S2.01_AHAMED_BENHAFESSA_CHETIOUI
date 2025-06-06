package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    private static int BoardSize = 15;
    private static int TileSize = 40;
    private int[][] gameMatrix = new int[BoardSize][BoardSize];

    private Image emptyImage;
    private Image DestructibleWallImage;
    private Image IndestructibleWallImage;

    @Override
    public void start(Stage primaryStage) {
        // Load images from resources/UI folder
        emptyImage = new Image(getClass().getResourceAsStream("/UI/000-floor.png"), TileSize, TileSize, false, true);
        IndestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/001-durable_wall.png"), TileSize, TileSize, false, true);
        DestructibleWallImage = new Image(getClass().getResourceAsStream("/UI/002-destructible_wall.png"), TileSize, TileSize, false, true);

        Pane root = new Pane();
        Random random = new Random();

        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                // Border walls
                if (row == 0 || row == BoardSize - 1 || col == 0 || col == BoardSize - 1) {
                    gameMatrix[row][col] = 2;
                } else if (row % 2 == 0 && col % 2 == 0) {
                    gameMatrix[row][col] = 2;
                } else {
                    int val = random.nextInt(100);
                    if (val < 80) {   // 80% chance
                        gameMatrix[row][col] = 1;
                    } else {
                        gameMatrix[row][col] = 0;
                    }

                }
                if (
                        (row == 1 && col == 1) || (row == 1 && col == 2) || (row == 2 && col == 1) ||
                                (row == 1 && col == BoardSize - 3) || (row == 1 && col == BoardSize - 2) || (row == 2 && col == BoardSize - 2) ||
                                (row == BoardSize - 2 && col == BoardSize - 3) || (row == BoardSize - 2 && col == BoardSize - 2) || (row == BoardSize - 3 && col == BoardSize - 2) ||
                                (row == BoardSize - 2 && col == 1) || (row == BoardSize - 2 && col == 2) || (row == BoardSize - 3 && col == 1)
                ) {
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

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setTitle("15x15 Matrix with Images");
        primaryStage.show();
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
