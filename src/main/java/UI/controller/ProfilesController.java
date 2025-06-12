package UI.controller;

import UI.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class ProfilesController {

    @FXML
    private ComboBox<String> comboBox1;

    @FXML
    private ComboBox<String> comboBox2;

    @FXML
    public void initialize() {
        List<String> profileNames = loadProfileNames();

        comboBox1.getItems().addAll(profileNames);
        comboBox2.getItems().addAll(profileNames);
    }

    private List<String> loadProfileNames() {
        List<String> profileList = new ArrayList<>();

        String userHome = System.getProperty("user.home");
        File profilesDir = new File(userHome, "BombermanProfiles");

        if (!profilesDir.exists() || !profilesDir.isDirectory()) {
            System.err.println("Profile directory does not exist.");
            return profileList;
        }

        File[] files = profilesDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith(".txt")) {
                    profileList.add(name.substring(0, name.length() - 4)); // strip .txt
                }
            }
        }

        return profileList;
    }

    @FXML
    private void handleConfirm() {
        String choice1 = comboBox1.getValue();
        String choice2 = comboBox2.getValue();

        if (choice1 == null || choice2 == null) {
            System.err.println("Please select both profiles.");
            return;
        }

        MainMenu.setPlayer1(choice1);
        MainMenu.setPlayer2(choice2);

        System.out.println("Selected: " + MainMenu.getPlayer1() + " and " + MainMenu.getPlayer2());

        // Close the current window
        comboBox1.getScene().getWindow().hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/menu/NewProfileWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Create New Profile");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
