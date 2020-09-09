/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie;

/**
 *
 * @author Sam
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ZombiesandHumans extends JPanel implements ActionListener {

    private JButton addHumanButton;
    private JButton addZombieButton;
    private Timer timer;
    private Random random;
    DrawPanel drawPanel;
    int z = 0;
    ArrayList<Human> Humans;
    
    private Human human;
    private Zombie zombie;

    public ZombiesandHumans() {
        super(new BorderLayout());
        random = new Random();
        addHumanButton = new JButton("Add Human");
        addZombieButton = new JButton("Add Zombie");
        addHumanButton.addActionListener((ActionListener) this);
        addZombieButton.addActionListener((ActionListener) this);
        JPanel southPanel = new JPanel();
        southPanel.add(addHumanButton);
        southPanel.add(addZombieButton);
        add(southPanel, BorderLayout.SOUTH);
        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);
        Humans = new ArrayList<Human>();
        timer = new Timer(5, this);

        timer.start();

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == addHumanButton) {
            human = new Human(random.nextInt(getWidth()), random.nextInt(getHeight()), Humans);

            Humans.add(human);

        }
        if (source == addZombieButton) {
            
                zombie = new Zombie(random.nextInt(getWidth()), random.nextInt(getHeight()), Humans);

                Humans.add(zombie);
            
        }
        if (source == timer) {

            drawPanel.repaint();

        }

    }

    private class DrawPanel extends JPanel {

        public DrawPanel() {
            super();
            setPreferredSize(new Dimension(400, 400));
            setBackground(Color.white);

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int i = 0; i < Humans.size(); i++) {
                Human.worldHeight = getHeight();
                Human.worldWidth = getWidth();
                human = Humans.get(i);

                
               
                if (human.isAlive || human instanceof Zombie){
                human.drawHuman(g);
                }
                else if (!human.isAlive && !(human instanceof Zombie)){
                    Zombie zom = new Zombie((int)human.x,(int)human.y, Humans);
                    human.t.interrupt();
                    Humans.set(i, zom);
                    

                }


            }

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Zombies VS. Humans");
        // kill all threads when frame closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ZombiesandHumans());

        frame.pack();
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width - frameDimension.width) / 2,
                (screenDimension.height - frameDimension.height) / 2);
        frame.setVisible(true);

        // now display something while the main thread is still alive
    }
    

}
