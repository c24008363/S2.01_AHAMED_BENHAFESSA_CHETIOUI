package UI;

import java.io.*;
import java.util.*;

public class PlayerStats {
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int blocksDestroyed = 0;
    private int itemsCollected = 0;

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
