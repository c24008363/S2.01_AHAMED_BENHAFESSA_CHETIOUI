package UI;

import UI.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jeu.personnages.Player;

import java.io.File;
import java.io.IOException;

public class MainMenu extends Application {
    private Stage primaryStage;
    private Scene menuScene;
    private static String theme;
    private static int tileSize = 40;
    private static int boardSize = 17;
    private static String player1 = "";
    private static String player2 = "";
    private static PlayerStats stats1;
    private static PlayerStats stats2;


    public static int getBoardSize() {
        return boardSize;
    }

    public static void setBoardSize(int boardSize) {
        MainMenu.boardSize = boardSize;
    }

    public static int getTileSize() {
        return tileSize;
    }

    public static void setTileSize(int tileSize) {
        MainMenu.tileSize = tileSize;
    }

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String theme) {
        MainMenu.theme = theme;
    }

    public static PlayerStats getStats1() {
        return stats1;
    }

    public static PlayerStats getStats2() {
        return stats2;
    }


    private Game main = new Game();

    public static String getPlayer2() {
        return player2;
    }

    public static void setPlayer2(String player2) {
        MainMenu.player2 = player2;
    }

    public static String getPlayer1() {
        return player1;
    }

    public static void setPlayer1(String player1) {
        MainMenu.player1 = player1;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setWidth(main.getBoardSize() * main.getTileSize() + (main.getTileSize() / 2) - 5);
        primaryStage.setHeight(main.getBoardSize() * main.getTileSize() + main.getTileSize() - 1);

        // Charger le menu depuis FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/menu/menu.fxml"));
        Parent menuRoot = fxmlLoader.load();
        menuScene = new Scene(menuRoot);

        // Passer une référence à Main dans le controller du menu
        MenuController controller = fxmlLoader.getController();
        controller.setMainMenu(this);

        primaryStage.setTitle("Bomberman Menu");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Lancement du jeu
    public void launchGame() {
        main = new Game(boardSize, tileSize);
        System.out.print(boardSize + "   ");
        System.out.println(tileSize);
        System.out.println(theme);
        System.out.println(player1);
        System.out.println(player2);

        String userHome = System.getProperty("user.home");
        String basePath = userHome + File.separator + "BombermanProfiles" + File.separator;

        stats1 = new PlayerStats(basePath + player1 + ".txt");
        stats2 = new PlayerStats(basePath + player2 + ".txt");

        try {
            MainMenu.getStats1().incrementGamesPlayed();
        } catch (Exception e) {
            System.err.println("No profile loaded for player 1");
        }
        try {
            MainMenu.getStats2().incrementGamesPlayed();
        } catch (Exception e) {
            System.err.println("No profile loaded for player 2");
        }

        menuScene.setRoot(main.getRoot());
        primaryStage.sizeToScene();
        main.attachKeyHandlers(menuScene);
    }
}
