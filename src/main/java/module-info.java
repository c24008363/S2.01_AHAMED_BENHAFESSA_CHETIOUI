/**
 * The UI module for the Bomberman game project.
 *
 * <p>This project implements a Bomberman-style game with a graphical user interface
 * built using JavaFX. It supports player profile management, customizable themes,
 * and a tile-based game board with destructible and indestructible walls.</p>
 *
 * <p>The module contains:</p>
 * <ul>
 *   <li><b>UI components and controllers</b> responsible for the main menu, profile selection,
 *       and in-game HUD (see {@link UI.MainMenu}, {@link UI.controller.ProfilesController})</li>
 *   <li><b>Game logic and state management</b> encapsulated in classes like
 *       {@link UI.Game}, handling game initialization, player movement,
 *       bomb placement, explosion handling, and game end conditions</li>
 *   <li><b>Player and item models</b> such as {@link jeu.personnages.Character} and item classes
 *       that define player stats, inventory, and item effects</li>
 *   <li><b>Resource management</b> including theme-based image loading
 *       and profile statistics persistence (e.g., {@link UI.MainMenu#getStats1()})</li>
 * </ul>
 *
 * <p>The module requires JavaFX modules for UI controls and FXML support,
 * and leverages Java 9+ module system for encapsulation and explicit dependencies.</p>
 *
 * <p>Gameplay features include:</p>
 * <ul>
 *   <li>Two-player local multiplayer support</li>
 *   <li>Tile-based board with indestructible walls, destructible walls, and empty spaces</li>
 *   <li>Bomb placement and explosion mechanics</li>
 *   <li>Item pickups that modify player stats (e.g., BombUp)</li>
 *   <li>Profile management with saved stats and customizable themes</li>
 * </ul>
 *
 * <p>See also:</p>
 * <ul>
 *   <li>{@link UI.Game} for core gameplay logic and game loop</li>
 *   <li>{@link UI.controller.ProfilesController} for managing player profiles</li>
 *   <li>{@link UI.MainMenu} for application entry point and theme selection</li>
 * </ul>
 */
module UI {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;

    opens UI to javafx.fxml;
    opens UI.controller to javafx.fxml;
    exports UI;
}
