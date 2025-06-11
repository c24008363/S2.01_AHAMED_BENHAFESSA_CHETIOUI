package UI;

import UI.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {
    private Stage primaryStage;
    private Scene menuScene;
    private Scene gameScene;
    private static String theme;
    private static int tileSize = 40;
    private static int boardSize = 17;

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

    private Game main =  new Game();
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setWidth(main.getBoardSize() * main.getTileSize() + (main.getTileSize()/2)-5);
        primaryStage.setHeight(main.getBoardSize() * main.getTileSize() + main.getTileSize()-1);

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
    public void launchGame(){
        main = new Game(boardSize, tileSize);
        System.out.print(boardSize + "   ");
        System.out.println(tileSize);
        System.out.println(theme);
        menuScene.setRoot(main.getRoot());
        primaryStage.sizeToScene();
        main.attachKeyHandlers(menuScene);
    }

    public void returnToMenu(){
        primaryStage.setScene(menuScene);
    }
}
