package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {

    private Circle shootArea;
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public Circle getShootArea() {
        return shootArea;
    }

    public Bot(GameController gc) {
        super(gc, 0, 150);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.shootArea = new Circle(position, 300);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
    }

    public void update(float dt) {
        shootArea.setPosition(position);
        super.update(dt);
        checkSpaceBorders();
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, int maxHp) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = maxHp;
        hp = hpMax;
        angle = MathUtils.random(0.0f, 360.0f);
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
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
}
