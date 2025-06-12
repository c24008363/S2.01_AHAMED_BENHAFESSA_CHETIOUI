package UI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;

public class NewProfileController {

    @FXML
    private TextField profileNameField;

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
