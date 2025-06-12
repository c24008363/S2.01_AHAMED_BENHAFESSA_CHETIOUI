package UI.controller;

import UI.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
    private MainMenu mainMenu;

    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    @FXML
    private void handlePlayButton(){

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

        mainMenu.launchGame();
    }
    @FXML
    private void handleQuitButton() { System.exit(0); }

    public void handleOptionsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/menu/OptionWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Options");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfilesClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/menu/ProfileWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Profiles");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
