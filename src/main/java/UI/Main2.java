package UI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jeu.personnages.Player;
import jeu.personnages.Player2;
import jeu.terrains.Terrain;
import jeu.terrains.Tile;

import java.util.HashSet;
import java.util.Set;

public class Main2 extends Application {
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private long lastUpdate = -1;

    private double tileSize = 40;

    private Image imagePlayer = new Image(getClass().getResourceAsStream("/UI/hagried.jpg"));
    private Image imagePlayer2 = new Image(getClass().getResourceAsStream("/UI/william.jpg"));

    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        Scene scene = new Scene(root);

        Terrain map = new Terrain(15, 15, tileSize);
        for (Tile[] row : map.getGrid()) {
            for (Tile tile : row){
                root.getChildren().add(tile.getView());
            }
        }

        Player player = new Player(imagePlayer, tileSize, tileSize, 35, 35, 150);
        Player2 player2 = new Player2(imagePlayer2, 13*tileSize, 13*tileSize, 35, 35, 150);
        root.getChildren().addAll(player.getRectangle(), player2.getRectangle());

        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now){
                if (lastUpdate < 0){
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

                player.update(deltaTime, activeKeys, map);
                player2.update(deltaTime, activeKeys, map);
                lastUpdate = now;
            }
        };

        timer.start();


        stage.setScene(scene);
        stage.show();
    }
}
