package UI;

import java.io.*;
import java.util.*;

public class PlayerStats {
    private int gamesPlayed;
    private int gamesWon;
    private int blocksDestroyed;
    private int itemsCollected;

    private final String filePath;

    public PlayerStats(String filePath) {
        this.filePath = filePath;
        load();
    }

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

    public void incrementGamesPlayed() { gamesPlayed++; }

    public void incrementGamesWon() { gamesWon++; }

    public void incrementBlocksDestroyed() { blocksDestroyed++; }

    public void incrementItemsCollected() { itemsCollected++; }

    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println(gamesPlayed);
            pw.println(gamesWon);
            pw.println(blocksDestroyed);
            pw.println(itemsCollected);
        } catch (IOException e) {
            System.err.println("Failed to save stats: " + filePath);
        }
    }
}
