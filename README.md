# S2.01_AHAMED_BENHAFESSA_CHETIOUI

# Bomberman JavaFX Game

## Overview

This project is a Bomberman-style game implemented in Java using JavaFX for the graphical interface. It supports local multiplayer gameplay, player profiles with saved statistics, and customizable visual themes. Players navigate a tile-based board, place bombs to destroy walls and opponents, and collect power-ups.

---

## Features

- **Two-player local multiplayer:** Control two players on the same keyboard with separate controls.
- **Tile-based map:** Indestructible walls, destructible walls, and open spaces randomly generated.
- **Bomb mechanics:** Place bombs that explode after a timer, affecting tiles and players.
- **Power-ups and items:** Collect items such as BombUp to increase abilities.
- **Player profiles:** Save player stats (games played, items collected) and load them between sessions.
- **Themes:** Customize game visuals through selectable themes. You can also import you themes by placing there along side the others in resources/UI/themes/. Any filename that isn't found in the said folder will be set to default image.
- **Game loop with smooth animations and responsive controls.**

---


## Project Structure

│   module-info.java <br>
│<br>
├───jeu<br>
│   ├───items<br>
│   │       BombUp.java<br>
│   │       FlameUp.java<br>
│   │       Gatherable.java<br>
│   │<br>
│   ├───objets<br>
│   │       Bomb.java<br>
│   │       Explosion.java<br>
│   │       TimedExplosion.java<br>
│   │<br>
│   └───personnages<br>
│           Character.java<br>
│           Player.java<br>
│<br>
└───UI<br>
‎‎....│   Game.java<br>
....│   MainMenu.java<br>
....│   PlayerStats.java<br>
....│<br>
....└───controller<br>
............MenuController.java<br>
............NewProfileController.java<br>
............OptionsWindowController.java<br>
............ProfilesController.java<br>

## Project Context
This project was developed as a university assignment for Aix Marseille Université. It aims to demonstrate practical skills in Java programming, GUI design with JavaFX, and game logic implementation.
