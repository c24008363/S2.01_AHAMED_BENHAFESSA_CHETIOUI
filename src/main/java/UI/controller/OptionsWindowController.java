package UI.controller;

import UI.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * Controller class for the Options window.
 * Allows the user to configure board size, tile size, and choose a visual theme.
 * Values are validated and passed directly to the {@link MainMenu}.
 */
public class OptionsWindowController {

    @FXML private TextField BoardSizeField;
    @FXML private TextField TileSizeField;
    @FXML private ComboBox<String> themeComboBox;


    /**
     * Initializes the controller after the FXML fields are injected.
     * Sets default values and input validation for board and tile size,
     * and populates the theme combo box.
     */
    @FXML
    public void initialize() {
        BoardSizeField.setText("15");
        TileSizeField.setText("40");
        setupIntegerValueCappedField(BoardSizeField, 30, 1);
        setupIntegerValueCappedField(TileSizeField, 75, 2);
        populateThemeComboBox();
    }

    /**
     * Binds both text fields with the board size and the tile size (in px) both have a max cap.
     * Updates the corresponding setting in {@link MainMenu} based on the field ID.
     *
     * @param textField the TextField to restrict
     * @param maxValue  the maximum allowable value
     * @param id        the identifier for the setting (1 = board size, 2 = tile size)
     */
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


    /**
     * Populates the theme selection ComboBox with directory names found
     * in the <code>/UI/themes</code> resource folder.
     * Sets the default theme and listens for changes to update the theme in {@link MainMenu}.
     */
    private void populateThemeComboBox() {
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
            MainMenu.setTheme("/UI/themes/Default"); // Initialize MainMenu.theme
        }

        // Update MainMenu.theme when selection changes
        themeComboBox.setOnAction(e -> {
            String selectedTheme = themeComboBox.getValue();
            MainMenu.setTheme("/UI/themes/"+selectedTheme+"/");
            System.out.println("Theme changed to: " + selectedTheme); // Optional debug
        });
    }

}
