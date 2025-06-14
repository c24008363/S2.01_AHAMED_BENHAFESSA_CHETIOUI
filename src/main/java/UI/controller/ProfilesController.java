package UI.controller;

import UI.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controller class for the profile selection window.
 * Allows the user to choose existing profiles for Player 1 and Player 2,
 * or create a new profile if needed.
 */
public class ProfilesController {

    @FXML
    private ComboBox<String> comboBox1;

    @FXML
    private ComboBox<String> comboBox2;

    @FXML
    private TextArea profilePreview;

    @FXML
    public void initialize() {
        List<String> profileNames = loadProfileNames();

        comboBox1.getItems().addAll(profileNames);
        comboBox2.getItems().addAll(profileNames);

        comboBox1.setOnAction(e -> updatePreview(comboBox1.getValue()));
        comboBox2.setOnAction(e -> updatePreview(comboBox2.getValue()));
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
                    profileList.add(name.substring(0, name.length() - 4)); // remove .txt
                }
            }
        }

        return profileList;
    }

    private void updatePreview(String profileName) {
        if (profileName == null) {
            profilePreview.setText("");
            return;
        }

        String userHome = System.getProperty("user.home");
        String basePath = userHome + File.separator + "BombermanProfiles" + File.separator;
        File profileFile = new File(basePath + profileName + ".txt");

        if (!profileFile.exists()) {
            profilePreview.setText("Profile file not found.");
            return;
        }

        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(profileFile)) {
            int count = 0;
            while (scanner.hasNextLine() && count < 4) {
                lines.add(scanner.nextLine());
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            profilePreview.setText("Error reading profile.");
            return;
        }

        // Labels for the first four expected fields
        String[] labels = { "Games Played: ", "Wins: ", "Blocks Broken: ", "PowerUps Collected: " };

        StringBuilder preview = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            preview.append(labels[i]).append(lines.get(i)).append("\n");
        }

        profilePreview.setText(preview.toString());
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
