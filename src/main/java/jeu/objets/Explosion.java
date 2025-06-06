package jeu.objets;

public class Explosion {
    private int dir; // 0=center, 1=up, 2=down, 3=left, 4=right
    private int dureeVie = 1; // You can use this to make explosions disappear after some time
    private int x;
    private int y;

    public Explosion(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public int getDureeVie() {
        return dureeVie;
    }

    public void decrementDureeVie() {
        dureeVie--;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
