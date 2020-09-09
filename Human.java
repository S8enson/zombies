/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

/**
 *
 * @author Sam
 */
public class Human implements Runnable {

    Thread t;
    double x, y, dx, dy, size, max_speed;
    int sightDistance;
    static int worldHeight, worldWidth;
    Color color;
    Random random;
    static ArrayList<Human> others;

    boolean isAlive;

    public Human(int x, int y, ArrayList<Human> humans) {
        random = new Random();
        others = humans;

        this.x = x;
        this.y = y;
        this.color = Color.blue;
        this.isAlive = true;
        this.sightDistance = (worldHeight + worldWidth) / 8;
        this.max_speed = 1;
        this.size = 10;
        this.dx = random.nextDouble() * max_speed * 2 - max_speed;
        this.dy = random.nextDouble() * max_speed * 2 - max_speed;
        t = new Thread(this);
        t.start();

    }

    // method that controls human 
    @Override
    public void run() {
        int i = 0;
        double oX, oY, d;
        while (t.isAlive()) {

            move();
            try {
                t.sleep(10);
            } catch (Exception e) {
            }

            i++;

            Pair<Boolean, Human> pair = closestVisible(this);
            if (pair.getKey()) {
                Human other = pair.getValue();
                oX = other.x + 1;
                oY = other.y + 1;
                d = distance(this, other);
                dx = -((oX - x) / d) * max_speed;
                dy = -((oY - y) / d) * max_speed;
                
            } else if ((i > (40 + random.nextInt(101))) && !pair.getKey()) {
                dx = random.nextDouble() * max_speed * 2 - max_speed;
                dy = random.nextDouble() * max_speed * 2 - max_speed;
                i = 0;

            }

        }

    }

    // basic detection of walls & movement
    public void move() {

        if (x >= worldWidth - size) {
            x = worldWidth - size;
            dx = -dx;
        } else if (x <= 0 + size) {
            x = 0 + size;
            dx = -dx;
        }
        if (y >= worldHeight - size) {
            y = worldHeight - size;
            dy = -dy;
        } else if (y <= 0 + size) {
            y = 0 + size;
            dy = -dy;
        }

        x += dx;
        y += dy;

    }

    // draws circle
    public void drawHuman(Graphics g) {
        g.setColor(color);

        g.fillOval((int) (x - size), (int) (y - size), (int) (2 * size), (int) (2 * size));

    }

    // calculates distance between two objects
    public double distance(Human h, Human z) {
        double d = Math.sqrt((h.x - z.x) * (h.x - z.x) + (h.y - z.y) * (h.y - z.y));
        return d;
    }

    // determines whether object is visible to another
    public boolean visible(Human h, Human z) {
        if (distance(h, z) < h.sightDistance) {

            return true;
        } else {
            return false;
        }
    }

    // searches array for closest visible zombie if human & vice versa
    public Pair<Boolean, Human> closestVisible(Human h) {
        int min = worldHeight;
        int minI = -1;
        int d = 0;
        boolean isVisible = false;
        Human other;
        for (int i = 0; i < others.size(); i++) {
            other = others.get(i);
            d = (int) distance(h, other);
            isVisible = visible(h, other);
            
            // if human
            if (h.isAlive) {
                if (!(other.isAlive) && isVisible && (d < min)) {
                    min = d;
                    minI = i;
                    if (d < (2 * size)) {
                        h.isAlive = false;
                    }
                }
                // if zombie
            } else if (!h.isAlive) {

                if ((other.isAlive) && isVisible && (d < min)) {
                    min = d;
                    minI = i;
                    if (d < (2 * size)) {
                        other.isAlive = false;
                    }
                }
            }
        }
        if (minI == -1) {
            return new Pair<Boolean, Human>(false, null);
        } else {
            other = others.get(minI);

            return new Pair<Boolean, Human>(true, other);
        }

    }
}
