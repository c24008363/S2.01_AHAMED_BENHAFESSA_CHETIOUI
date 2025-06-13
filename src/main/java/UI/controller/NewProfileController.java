package UI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;

/**
 * Controller class for the New Profile creation window.
 * Handles user input to create a new player profile file and ensures
 * no duplicate profile names exist.
 */
public class NewProfileController {

    @FXML
    private TextField profileNameField;

    /**
     * Handles the confirmation action when the user submits a new profile name.
     * Creates a new profile file in the user's home directory under "BombermanProfiles".
     * The file contains four lines initialized to 0 (for stats).
     *
     * @param event the action event triggered by the confirm button
     */
    @FXML
    private void handleConfirm(ActionEvent event) {
        String profileName = profileNameField.getText().trim();

        if (profileName.isEmpty()) {
            showAlert("Invalid Input", "Profile name cannot be empty.");
            return;
        }

        try {
            String userHome = System.getProperty("user.home");
            File profilesDir = new File(userHome, "BombermanProfiles");

            if (!profilesDir.exists()) {
                profilesDir.mkdirs();
            }

            File newFile = new File(profilesDir, profileName + ".txt");

            if (newFile.exists()) {
                showAlert("Already Exists", "A profile with this name already exists.");
                return;
            }

            try (FileWriter writer = new FileWriter(newFile)) {
                for (int i = 0; i < 4; i++) {
                    writer.write("0\n");
                }
            }

            System.out.println("Created profile: " + newFile.getAbsolutePath());

            // Close the window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Exception", "Failed to create profile file.");
        }
    }

    /**
     * Displays a warning alert with the given title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the main message/content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
