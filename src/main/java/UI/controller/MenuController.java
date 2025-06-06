package UI.controller;

import UI.MainMenu;
import javafx.fxml.FXML;

public class MenuController {
    private MainMenu mainMenu;

    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    @FXML
    private void handlePlayButton(){
        mainMenu.launchGame();
    }

}
