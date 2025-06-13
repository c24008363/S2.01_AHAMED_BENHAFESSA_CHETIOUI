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
    /** The primary JavaFX stage used to display the game and menu scenes. */
    private Stage primaryStage;
    /** The scene containing the main menu UI. */
    private Scene menuScene;
    /** The currently selected visual theme for the game's UI and assets. */
    private static String theme;
    /** The size of each tile on the game board, in pixels. */
    private static int tileSize = 40;
    /** The number of tiles per side of the square game board. */
    private static int boardSize = 17;
    /** The name or identifier of player 1. */
    private static String player1 = "";
    /** The name or identifier of player 2. */
    private static String player2 = "";
    /** The gameplay statistics and power-up status for player 1. */
    private static PlayerStats stats1;
    /** The gameplay statistics and power-up status for player 2. */
    private static PlayerStats stats2;
    /** Creates a new game core and assigns it directly to this menu for fluidity when switching scenes */
    private Game main = new Game();


    /** Returns the current board size in number of tiles. */
    public static int getBoardSize() {
        return boardSize;
    }
    /** Sets the board size in number of tiles. */
    public static void setBoardSize(int boardSize) {
        MainMenu.boardSize = boardSize;
    }
    /** Returns the current tile size in pixels. */
    public static int getTileSize() {
        return tileSize;
    }
    /** Sets the tile size in pixels. */
    public static void setTileSize(int tileSize) {
        MainMenu.tileSize = tileSize;
    }
    /** Returns the currently selected theme name. */
    public static String getTheme() {
        return theme;
    }
    /** Sets the currently selected theme name. */
    public static void setTheme(String theme) {
        MainMenu.theme = theme;
    }
    /** Returns the PlayerStats object for player 1. */
    public static PlayerStats getStats1() {
        return stats1;
    }
    /** Returns the PlayerStats object for player 2. */
    public static PlayerStats getStats2() {
        return stats2;
    }
    /** Returns the profile of player 2. */
    public static String getPlayer2() {
        return player2;
    }
    /** Sets the profile of player 2. */
    public static void setPlayer2(String player2) {
        MainMenu.player2 = player2;
    }
    /** Returns the profile of player 1. */
    public static String getPlayer1() {
        return player1;
    }
    /** Sets the profile of player 1. */
    public static void setPlayer1(String player1) {
        MainMenu.player1 = player1;
    }

    /**
     * JavaFX application entry point. Sets up the main menu scene and initializes the primary stage.
     * @param primaryStage the main stage for the JavaFX application
     * @throws IOException if the FXML menu cannot be loaded
     */
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
    /**
     * Launches the game from the main menu using the currently set board size, tile size, theme, and player profiles.
     * Initializes player stats and switches the scene to the game view.
     */
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
