package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {
    private Background background;
    private Hero hero;
    private BulletController bulletControllerHero;
    private BulletController bulletControllerBot;
    private ParticleController particleController;
    private AsteroidController asteroidController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private BotController botController;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private boolean nextLevel;
    private int level;
    private float nextLevelTime;
    private Music music;
    private StringBuilder sb;
    private float dt;

    public InfoController getInfoController() {
        return infoController;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public Stage getStage() {
        return stage;
    }

    public int getLevel() {
        return level;
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public GameController(SpriteBatch batch) {
        this.infoController = new InfoController();
        this.asteroidController = new AsteroidController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletControllerHero = new BulletController(this);
        this.bulletControllerBot = new BulletController(this);
        this.tempVec = new Vector2();
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.botController = new BotController(this);
        this.level = 1;
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();
        this.nextLevelTime = 0.0f;
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.nextLevel = true;
        this.sb = new StringBuilder();
    }

    public BulletController getBulletControllerBot() {
        return bulletControllerBot;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletControllerHero() {
        return bulletControllerHero;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public BotController getBotController() {
        return botController;
    }

    public void update(float dt) {
        this.dt = dt;
        if (pause) {
            return;
        }
        checkLevel(dt);
        if (nextLevel) {
            return;
        }
        background.update(dt);
        hero.update(dt);
        bulletControllerHero.update(dt);
        bulletControllerBot.update(dt);
        particleController.update(dt);
        asteroidController.update(dt);
        powerUpsController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER, hero);
        }
        stage.act(dt);
    }

    private void checkLevel(float dt) {
        if (!nextLevel) {
            if (asteroidController.getActiveList().size() == 0) {
                nextLevel = true;
                level++;
                return;
            }
        } else {
            nextLevelTime += dt;
            if (nextLevelTime >= 2) {
                for (int i = 0; i < 3; i++) {
                    asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                            MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                            MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f, level * 10);
                }
                for (int i = 0; i < 2; i++) {
                    botController.setup(ScreenManager.SCREEN_WIDTH + 64,
                            MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                            0, 0, level * 10);
                }

                nextLevelTime = 0.0f;
                nextLevel = false;
            }
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                tempVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                a.getVelocity().mulAdd(tempVec, -hero.getHitArea().radius / sumScl * 100);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                }
                hero.takeDamage(level * 2);
                sb.setLength(0);
                sb.append("HP - ").append(level * 2);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
            }
        }
        for (int i = 0; i < bulletControllerHero.getActiveList().size(); i++) {
            Bullet bul = bulletControllerHero.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(bul.getPosition())) {
                    particleController.setup(bul.getPosition().x + MathUtils.random(-4, 4),
                            bul.getPosition().y + MathUtils.random(-4, 4),
                            bul.getVelocity().x * -0.3f + MathUtils.random(-20, 20),
                            bul.getVelocity().y * -0.3f + MathUtils.random(-20, 20),
                            0.2f, 2.2f, 1.5f, 1.0f, 1.0f,
                            1.0f, 1, 0, 0, 1, 0);

                    bul.deactivate();
                    if (a.takeDamage(hero.getCurWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y,
                                    a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }

            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot bot = botController.getActiveList().get(j);
                if (bot.getHitArea().contains(bul.getPosition())) {
                    particleController.setup(bul.getPosition().x + MathUtils.random(-4, 4),
                            bul.getPosition().y + MathUtils.random(-4, 4),
                            bul.getVelocity().x * -0.3f + MathUtils.random(-20, 20),
                            bul.getVelocity().y * -0.3f + MathUtils.random(-20, 20),
                            0.2f, 2.2f, 1.5f, 1.0f, 1.0f,
                            1.0f, 1, 0, 0, 1, 0);

                    bul.deactivate();
                    bot.takeDamage(level);
                    if (!bot.isAlive()) {
                        bot.deactivate();
                        hero.addScore(bot.getHpMax() * 50);
                        sb.setLength(0);
                        sb.append("SCORE + ").append(bot.getHpMax() * 50);
                        infoController.setup(bot.getPosition().x, bot.getPosition().y, sb.toString(), Color.WHITE);
                    }
                }
            }
        }

        for (int i = 0; i < bulletControllerBot.getActiveList().size(); i++) {
            Bullet bul = bulletControllerBot.getActiveList().get(i);
            if (hero.getHitArea().contains(bul.getPosition())) {
                particleController.setup(bul.getPosition().x + MathUtils.random(-4, 4),
                        bul.getPosition().y + MathUtils.random(-4, 4),
                        bul.getVelocity().x * -0.3f + MathUtils.random(-20, 20),
                        bul.getVelocity().y * -0.3f + MathUtils.random(-20, 20),
                        0.2f, 2.2f, 1.5f, 1.0f, 1.0f,
                        1.0f, 1, 0, 0, 1, 0);

                bul.deactivate();
                hero.takeDamage(level);
                sb.setLength(0);
                sb.append("HP - ").append(level);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
            }
        }


        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps p = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
            if (hero.getMagnetArea().contains(p.getPosition())) {
                tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tempVec, 50.0f);
            }
        }

        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            tempVec.set(hero.getPosition()).sub(bot.getPosition()).nor();
            bot.angle = tempVec.angleDeg();
            if (!hero.getHitArea().overlaps(bot.getShootArea())) {
                bot.velocity.mulAdd(tempVec, bot.enginePower * dt);
            } else bot.tryToFire(bulletControllerBot);
        }
    }

    public void dispose() {
        background.dispose();
    }
}
