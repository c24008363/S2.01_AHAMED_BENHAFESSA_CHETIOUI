package UI.controller;

import UI.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.File;

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

        try {
            // Load as URL
            URL url = getClass().getResource("/profiles");
            if (url == null) {
                System.err.println("Profile directory not found.");
                return profileList;
            }

            if (url.getProtocol().equals("file")) {
                File folder = new File(url.toURI());
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
                if (files != null) {
                    for (File file : files) {
                        profileList.add(file.getName());
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                String pathInJar = "profiles/";
                String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(pathInJar) && name.endsWith(".txt") && !entry.isDirectory()) {
                            profileList.add(name.substring(pathInJar.length()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        // Set static values in MainMenu
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
