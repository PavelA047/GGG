package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private GameController gc;

    public Asteroid(GameController gc) {
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH + 100,
                ScreenManager.SCREEN_HEIGHT + 100);
        this.velocity = new Vector2(-40, 40);
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 0.5f, 0.5f, 0, 0, 0, 256, 256,
                false, false);
    }

    public void update(float dt) {
        position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
        position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;
        if (position.x < -100) {
            position.x = ScreenManager.SCREEN_WIDTH + 100;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 100) {
            position.y = -100;
        }
    }
}
