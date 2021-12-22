package com.star.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private MyStarGame starGame;

    public Asteroid(MyStarGame starGame) {
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH + 100,
                ScreenManager.SCREEN_HEIGHT + 100);
        this.velocity = new Vector2(-40, 40);
        this.starGame = starGame;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 0.5f, 0.5f, 0, 0, 0, 256, 256,
                false, false);
    }

    public void update(float dt) {
        position.x += (velocity.x - starGame.getHero().getLastMovie().x * 15 + starGame.getHero().getLastMovieBack().x * 15) * dt;
        position.y += (velocity.y - starGame.getHero().getLastMovie().y * 15 + starGame.getHero().getLastMovieBack().y * 15) * dt;
        if (position.x < -100) {
            position.x = ScreenManager.SCREEN_WIDTH + 100;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 100) {
            position.y = -100;
        }
    }
}
