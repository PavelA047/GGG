package com.star.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private float angle;
    private Vector2 lastMovie;
    private Vector2 lastMovieBack;

    public Vector2 getLastMovie() {
        return lastMovie;
    }

    public Vector2 getLastMovieBack() {
        return lastMovieBack;
    }

    public Hero() {
        this.texture = new Texture("ship.png");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.angle = 0.0f;
        this.lastMovie = new Vector2(0, 0);
        this.lastMovieBack = new Vector2(0, 0);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle, 0, 0, 64, 64,
                false, false);
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += MathUtils.cosDeg(angle) * 240.0f * dt;
            position.y += MathUtils.sinDeg(angle) * 240.0f * dt;
            lastMovie.set(MathUtils.cosDeg(angle) * 240.0f * dt,
                    MathUtils.sinDeg(angle) * 240.0f * dt);
        } else {
            lastMovie.set(0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x -= MathUtils.cosDeg(angle) * 120.0f * dt;
            position.y -= MathUtils.sinDeg(angle) * 120.0f * dt;
            lastMovieBack.set(MathUtils.cosDeg(angle) * 120.0f * dt,
                    MathUtils.sinDeg(angle) * 120.0f * dt);
        } else {
            lastMovieBack.set(0, 0);
        }

        if (position.x < 32) {
            position.x = 32;
            lastMovie.set(0, 0);
            lastMovieBack.set(0, 0);
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            lastMovie.set(0, 0);
            lastMovieBack.set(0, 0);
        }
        if (position.y < 32) {
            position.y = 32;
            lastMovie.set(0, 0);
            lastMovieBack.set(0, 0);
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            lastMovie.set(0, 0);
            lastMovieBack.set(0, 0);
        }
    }
}
