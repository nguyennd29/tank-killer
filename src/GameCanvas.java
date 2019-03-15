import Tilemap.Map;
import base.GameObjectManager;
import base.Settings;
import base.Vector2D;
import game.Gun.CreateDesertEagle;
import game.ViewPort;
import game.player.Player;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GameCanvas extends JPanel {


    private BufferedImage backBuffered;
    public Player player;
    private Graphics2D g2d;

    //    private ViewPort viewPort;

    GameCanvas() {
        this.setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);

        this.setupBackBuffered();
        this.setBackground(Color.black);

        setupCharacter();

//        this.viewPort=new ViewPort();
        ViewPort.instance.getFollowOffset().set(-Settings.GAMEPLAY_WIDTH/2,-Settings.GAMEPLAY_HEIGHT/2);
        this.addWall();
        this.setVisible(true);
    }

    private void addWall(){
        Map map = Map.load("resources/mapcreate/test.json");
        map.generate();
        System.out.println(map);
    }

    private void setupBackBuffered() {

        this.backBuffered = new BufferedImage(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.g2d =(Graphics2D) this.backBuffered.getGraphics();
    }

    private void setupCharacter() {
//        GameObjectManager.instance.add(new Background());
//        GameObjectManager.instance.add(new EnemyFollowCreate());
//        GameObjectManager.instance.add(new EnemyCreate());
//        GameObjectManager.instance.add(new CreateBoss());
//        GameObjectManager.instance.add(new CreateDesertEagle());
        this.setupPlayer();

    }


    private void setupPlayer() {
        Player.instance.position.set(600,2000);

        GameObjectManager.instance.add(Player.instance);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.black);
        g2d.drawImage(this.backBuffered, 0, 0, null);
    }

    public void renderAll() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        GameObjectManager.instance.renderBackground(this.g2d,ViewPort.instance);
        GameObjectManager.instance.renderAll(this.g2d,ViewPort.instance);
        this.repaint();

    }

    public void runAll() {

        GameObjectManager.instance.runAll();
        ViewPort.instance.follow(Player.instance);
    }
}