package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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


    public int getScoreView() {
        return scoreView;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Hero(GameController gc) {
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        bulletTimeOut += dt;
        if(scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (bulletTimeOut > 0.2f) {
                bulletTimeOut = 0.0f;
                float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500.0f + velocity.x,
                        MathUtils.sinDeg(angle) * 500.0f + velocity.y);

                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500.0f + velocity.x,
                        MathUtils.sinDeg(angle) * 500.0f + velocity.y);
            }
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
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * (enginePower / 2) * dt;
            velocity.y -= MathUtils.sinDeg(angle) * (enginePower / 2) * dt;
        }

        position.mulAdd(velocity, dt);

        float stopK = 1.0f - 1.0f * dt;
        if (stopK < 0.0f) {
            stopK = 0.0f;
        }
        velocity.scl(stopK);

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
}
