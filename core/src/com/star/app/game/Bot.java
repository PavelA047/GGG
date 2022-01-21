package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {

    private boolean active;
    private Vector2 tempVector;
    private Sound exSound;

    @Override
    public boolean isActive() {
        return active;
    }

    public Bot(GameController gc) {
        super(gc, 10, 150);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.tempVector = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
        this.active = false;
        this.ownerType = OwnerType.BOT;
        this.exSound = Assets.getInstance().getAssetManager().get("audio/Explosion.mp3");
    }

    public void update(float dt) {
        super.update(dt);

        if (!isAlive()) {
            deactivate();
            exSound.play();
            gc.getHero().addScore(gc.getLevel() * 10);
            gc.getSb().setLength(0);
            gc.getSb().append("SCORE + ").append(gc.getLevel() * 10);
            gc.getInfoController().setup(this.getPosition().x, this.getPosition().y, gc.getSb().toString(), Color.WHITE);
        }
        tempVector.set(gc.getHero().getPosition()).sub(position).nor();

        angle = tempVector.angleDeg();

        if (gc.getHero().getPosition().dst(position) > 200) {
            accelerate(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20),
                        velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.0f,
                        0, 1, 1, 0.5f, 0, 0);
            }
        }
        if (gc.getHero().getPosition().dst(position) < 300) {
            tryToFire();
        }
    }

    public void activate(float x, float y, int scl) {
        hpMax *= scl;
        hp = hpMax;
        position.set(x, y);
        active = true;
    }

    public void deactivate() {
        active = false;
    }
}
