package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public enum PowerUps implements Poolable {
    MONEY(Assets.getInstance().getAtlas().findRegion("money")),
    MEDICINE(Assets.getInstance().getAtlas().findRegion("med")),
    AMMO(Assets.getInstance().getAtlas().findRegion("ammo"));

    private TextureRegion texture;
    private Vector2 position;
    boolean active;
    private Circle hitArea;
    private final float BASE_SIZE = 32;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    PowerUps(TextureRegion texture) {
        this.texture = texture;
        this.position = new Vector2();
        this.active = false;
        this.hitArea = new Circle(position, BASE_RADIUS);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 16, position.y - 16, 16, 16,
                32, 32, 1, 1, 0);
    }

    public void activate(float x, float y) {
        position.set(x, y);
        hitArea.setPosition(position);
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public Circle getHitArea() {
        return hitArea;
    }
}
