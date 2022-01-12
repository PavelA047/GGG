package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float bulletTimeOut;
    private GameController gc;
    private int score;
    private int scoreView;
    private int hpMax;
    private int hp;
    private int money;
    private StringBuilder sb;
    private Circle hitArea;
    private Weapon curWeapon;

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Weapon getCurWeapon() {
        return curWeapon;
    }

    public Hero(GameController gc) {
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.gc = gc;
        this.hpMax = 100;
        this.hp = hpMax;
        this.money = 0;
        this.sb = new StringBuilder();
        this.hitArea = new Circle(position, 29);
        this.curWeapon = new Weapon(gc, this, "Laser", 0.1f,
                1, 600.0f, 300,
                new Vector3[]{
                        new Vector3(28, 0, 0),
                        new Vector3(28, 90, 20),
                        new Vector3(28, -90, -20)
                });
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE: ").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append("/").append(hpMax).append("\n");
        sb.append("BULLETS: ").append(curWeapon.getCurBullets()).append("/").append(curWeapon.getMaxBullets()).append("\n");
        sb.append("$: ").append(money).append("\n");
        font.draw(batch, sb, 20, 700);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        bulletTimeOut += dt;
        updateScore(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            tryToFire();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20),
                        velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * (enginePower / 2) * dt;
            velocity.y -= MathUtils.sinDeg(angle) * (enginePower / 2) * dt;

            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20),
                        velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20),
                        velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
        }

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        float stopK = 1.0f - 1.0f * dt;
        if (stopK < 0.0f) {
            stopK = 0.0f;
        }
        velocity.scl(stopK);

        checkSpaceBorders();
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 4000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void tryToFire() {
        if (bulletTimeOut > curWeapon.getFirePeriod()) {
            bulletTimeOut = 0.0f;
            curWeapon.fire();
        }
    }

    private void checkSpaceBorders() {
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void consume(PowerUps p) {
        switch (p.getType()) {
            case MEDKIT:
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
            case MONEY:
                money += p.getPower();
            case AMMOS:
                curWeapon.addAmmos(p.getPower());
        }
    }
}
