package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

public class PowerUps implements Poolable {
    public enum Type {
        MEDKIT(0), MONEY(1), AMMOS(2);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    boolean active;
    private float time;
    private int power;
    private Type type;

    public GameController getGc() {
        return gc;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getTime() {
        return time;
    }

    public int getPower() {
        return power;
    }

    public Type getType() {
        return type;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(Type type, float x, float y, int power) {
        this.type = type;
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        this.velocity.nor().scl(50.0f);
        active = true;
        this.power = power;
        this.time = 0.0f;
    }

    PowerUps(GameController gc) {
        this.gc = gc;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= 7.0f) {
            deactivate();
        }
    }
}
