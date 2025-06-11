package UI.controller;

import UI.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class OptionsWindowController {

    @FXML private TextField BoardSizeField;
    @FXML private TextField TileSizeField;
    @FXML private ComboBox<String> themeComboBox;


    @FXML
    public void initialize() {
        BoardSizeField.setText("15");
        TileSizeField.setText("40");
        setupIntegerValueCappedField(BoardSizeField, 30, 1);
        setupIntegerValueCappedField(TileSizeField, 75, 2);
        populateThemeComboBox();
    }

    private void setupIntegerValueCappedField(TextField textField, int maxValue, int id) { //Allow the player to input only ints and a max value in BoardSize and TileSize.
        textField.textProperty().addListener((obs, oldText, newText) -> {
            StringBuilder filtered = new StringBuilder();
            for (char c : newText.toCharArray()) {
                if (Character.toString(c).matches("[0-9]")) {
                    filtered.append(c);
                }
            }

            String filteredStr = filtered.toString();
            if (!filteredStr.isEmpty()) {
                try {
                    int value = Integer.parseInt(filteredStr);
                    if (value > maxValue) {
                        filteredStr = String.valueOf(maxValue);
                    }
                } catch (NumberFormatException ignored) {
                    filteredStr = "";
                }
            }

            if (id == 1){
                MainMenu.setBoardSize(Integer.parseInt(filteredStr));
                System.out.println(MainMenu.getBoardSize() + "Size changed");
            }
            else if (id == 2){
                MainMenu.setTileSize(Integer.parseInt(filteredStr));
                System.out.println(MainMenu.getTileSize() + "Tile Size changed");
            }
            if (!newText.equals(filteredStr)) {
                textField.setText(filteredStr);

            }
        });
    }

    private void populateThemeComboBox() { //Gets the directories of theme located at resources/UI/themes and make them options of combobox
        try {
            URL url = getClass().getResource("/UI/themes");
            if (url != null) {
                File themesDir = new File(url.toURI());
                File[] directories = themesDir.listFiles(File::isDirectory);

                if (directories != null) {
                    for (File dir : directories) {
                        themeComboBox.getItems().add(dir.getName());
                    }
                }
            } else {
                System.err.println("Themes directory not found in resources.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set initial selection
        if (!themeComboBox.getItems().isEmpty()) {
            themeComboBox.setValue("Default");
        }
    }
}
