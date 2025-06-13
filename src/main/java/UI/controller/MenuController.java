package UI.controller;

import UI.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the main menu view.
 * Handles user interactions from the main menu screen, such as starting the game,
 * quitting the application, and opening the options or profiles windows.
 */
public class MenuController {
    private MainMenu mainMenu;

    /**
     * Sets the reference to the main menu application.
     *
     * @param mainMenu the main menu instance
     */
    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    /**
     * Handles the Play button click.
     * Attempts to increment games played for both players (if profiles are loaded),
     * then launches the game scene.
     */
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

    /**
     * Handles the Quit button click.
     * Exits the application immediately.
     */
    @FXML
    private void handleQuitButton() { System.exit(0); }

    /**
     * Handles the Options button click.
     * Opens a new window to allow the user to adjust application settings.
     */
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

    /**
     * Handles the Profiles button click.
     * Opens a new window for managing player profiles.
     */
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
