package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    GameController gc;
    private Texture texture;

    public AsteroidController(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("asteroid.png");
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
            checkPool();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            batch.draw(texture, a.getPosition().x - 128, a.getPosition().y - 128, 128, 128,
                    256, 256, 0.5f, 0.5f, 0, 0, 0, 256, 256,
                    false, false);
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }
}
