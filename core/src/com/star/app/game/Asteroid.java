package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Asteroid implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private GameController gc;
    boolean active;

    public Asteroid(GameController gc) {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.gc = gc;
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float dt) {
        position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
        position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;
        if (position.x < -100 || position.y > ScreenManager.SCREEN_HEIGHT + 100) {
            deactivate();
            gc.getAsteroidController().setup(MathUtils.random(ScreenManager.SCREEN_WIDTH / 10,
                    ScreenManager.SCREEN_WIDTH + 400), -100, -40, 40);
        }
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
