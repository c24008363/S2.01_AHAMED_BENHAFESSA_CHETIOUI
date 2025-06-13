package UI;

import java.io.*;
import java.util.*;

/**
 * Manages the gameplay statistics for a single player, including persistence to a file.
 * The statistic will update on game end.
 */
public class PlayerStats {
    /** Number of games the player has played. */
    private int gamesPlayed = 0;
    /** Number of games the player has won. */
    private int gamesWon = 0;
    /** Number of destructible blocks the player has destroyed. */
    private int blocksDestroyed = 0;
    /** Number of items the player has collected. */
    private int itemsCollected = 0;
    /** Path to the file where the player's statistics are saved. */
    private final String filePath;

    /**
     * Constructs a PlayerStats object with the given save file path and loads existing data.
     * @param filePath path to the stats file
     */
    public PlayerStats(String filePath) {
        this.filePath = filePath;
        load();
    }

    /**
     * Loads statistics from the file.
     * If the file is missing or contains invalid data, stats are initialized to 0.
     */
    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            gamesPlayed = Integer.parseInt(br.readLine());
            gamesWon = Integer.parseInt(br.readLine());
            blocksDestroyed = Integer.parseInt(br.readLine());
            itemsCollected = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or has bad data, init to 0s
            gamesPlayed = gamesWon = blocksDestroyed = itemsCollected = 0;
        }
    }

    /** Increments the number of games played by 1. */
    public void incrementGamesPlayed() { gamesPlayed++; }
    /** Increments the number of games won by 1. */
    public void incrementGamesWon() { gamesWon++; }
    /** Increments the number of blocks destroyed by 1. */
    public void incrementBlocksDestroyed() { blocksDestroyed++; }
    /** Increments the number of items collected by 1. */
    public void incrementItemsCollected() { itemsCollected++; }

    /**
     * Saves the current statistics to the file.
     * If the file cannot be written to, an error message is printed.
     */
    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println(gamesPlayed);
            System.out.println(gamesPlayed);
            pw.println(gamesWon);
            System.out.println(gamesWon);
            pw.println(blocksDestroyed);
            System.out.println(blocksDestroyed);
            pw.println(itemsCollected);
            System.out.println(itemsCollected);
        } catch (IOException e) {
            System.err.println("Failed to save stats: " + filePath);
        }
    }
}
