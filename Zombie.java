/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie;

import java.awt.Color;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author Sam
 */
public class Zombie extends Human {

    public Zombie(int x, int y, ArrayList<Human> humans) {
        super(x, y, humans);

        this.max_speed /= 2;
        this.isAlive = false;
        this.sightDistance = worldHeight;
        this.color = Color.RED;
        this.dx = max_speed;
        this.dy = max_speed;

    }

    // same as human but moves towards closest visible rather than away from
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
                dx = ((oX - x) / d) * max_speed;
                dy = ((oY - y) / d) * max_speed;
                
            } else if ((i > (40 + random.nextInt(101))) && !pair.getKey()) {
                dx = random.nextDouble() * max_speed * 2 - max_speed;
                dy = random.nextDouble() * max_speed * 2 - max_speed;
                i = 0;
                
            }

        }

    }


}
