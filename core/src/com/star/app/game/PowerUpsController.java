package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.helpers.ObjectPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowerUpsController extends ObjectPool<PowerUps> {

    @Override
    protected PowerUps newObject() {
        List<PowerUps> powerUps = new ArrayList<>(Arrays.asList(PowerUps.values()));
        return powerUps.get(MathUtils.random(0, powerUps.size() - 1));
    }

    public void update() {
        for (int i = 0; i < activeList.size(); i++) {
            checkPool();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            PowerUps p = activeList.get(i);
            p.render(batch);
        }
    }

    public void setup(float x, float y) {
        PowerUps activeElement = getActiveElement();
        if (!activeElement.isActive()) {
            activeElement.activate(x, y);
        }
    }
}
