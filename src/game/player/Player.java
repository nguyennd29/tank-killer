package game.player;

import base.GameObject;
import base.GameObjectManager;
import base.Vector2D;
import game.Gun.*;
import game.boss.BossShoot;
import game.boss.BulletBoss;
import game.enemy.BulletEnemy;
import game.enemy.Enemies;
import game.enemyfollow.BulletEnemyFollow;
import game.enemyfollow.EnemyFollow;
import game.platforms.Platform;
import input.KeyboardInput;
import physic.BoxCollider;
import physic.PhysicBody;
import physic.RunHitObject;
import renderer.ImageRenderer;


public class Player extends GameObject implements PhysicBody {
    public static Player instance = new Player();
    public Vector2D velocity;
    public BoxCollider boxCollider;
    public RunHitObject runHitObject;
    public RunHitObject runHitPlatform;
    public DesertEagle desertEagle;

    public double angle;
    public float SPEED = 15;
    private int width;
    private int height;

    public int Mana =30;
    public int damage;



   private Player() {
        this.isAlive = true;
        this.velocity = new Vector2D();
        this.width=100;
        this.height=100;
       this.boxCollider = new BoxCollider(width * 3 / 5, height * 4 / 5);
       this.renderer = new ImageRenderer("resources/images/FirePlayer.png", width, height);

        this.desertEagle = new DesertEagle();
        this.runHitObject = new RunHitObject(
                EnemyFollow.class,
                Enemies.class, BulletEnemy.class, BulletBoss.class, BulletEnemyFollow.class);
        this.runHitPlatform = new RunHitObject(Platform.class);

       this.damage=15;
    }

    public void run() {
        super.run();

        this.boxCollider.position.set(this.position.x - this.width / 3 + this.width / 9, this.position.y - this.height / 3 - this.width / 9);
        this.runHitObject.run(this);
        desertEagle.shoot(GameObjectManager.instance.findPlayer());

        this.move();


//        this.position.addUp(velocity);

    }
    private void move(){
        this.velocity.set(0, 0);

        if (KeyboardInput.instance.isUp) {
            this.velocity.y -= SPEED;
        }
        if (KeyboardInput.instance.isDown) {
            this.velocity.y += SPEED;
        }
        if (KeyboardInput.instance.isRight) {
            this.velocity.x += SPEED;
        }
        if (KeyboardInput.instance.isLeft) {
            this.velocity.x -= SPEED;
        }
        moveVertical();
        moveHorizontal();
    }

    private void moveVertical() {
        BoxCollider nextBoxCollider = this.boxCollider.shift(0, velocity.y);
        Platform platform = GameObjectManager.instance.checkCollision(nextBoxCollider, Platform.class);
        if (platform != null) {
            float shiftDistance = Math.signum(velocity.y);
            boolean moveContinue = true;
            while (moveContinue) {
                if (GameObjectManager.instance.checkCollision(this.boxCollider.shift(0, shiftDistance), Platform.class) != null) {
                    moveContinue = false;
                } else {
                    shiftDistance += Math.signum(velocity.y);
                    this.position.addUp(0, Math.signum(velocity.y));
                }
            }
            velocity.y = 0;
        }
        this.position.addUp(0,velocity.y);
        this.screenPosition.addUp(0,velocity.y);
    }
    private void moveHorizontal() {
        BoxCollider nextBoxCollider = this.boxCollider.shift(velocity.x, 0);
        Platform platform = GameObjectManager.instance.checkCollision(nextBoxCollider, Platform.class);
        if (platform != null) {
            float shiftDistance = Math.signum(velocity.x);
            boolean moveContinue = true;
            while (moveContinue) {
                if (GameObjectManager.instance.checkCollision(this.boxCollider.shift(shiftDistance,0), Platform.class) != null) {
                    moveContinue = false;
                } else {
                    shiftDistance += Math.signum(velocity.x);
                    this.position.addUp(Math.signum(velocity.x), 0);
                }
            }
            velocity.x = 0;
        }
        this.position.addUp(velocity.x,0);
        this.screenPosition.addUp(velocity.x,0);
    }


    @Override
    public BoxCollider getBoxCollider() {
        return this.boxCollider;
    }

    @Override
    public void getHit(GameObject gameObject) {

            this.isAlive = false;

    }

    @Override
    public boolean isActive() {
        return this.isAlive;
    }
}


