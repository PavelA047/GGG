package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private BulletController bulletController;
    private ParticleController particleController;
    private AsteroidController asteroidController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private boolean nextLevel;
    private int level;
    private float nextLevelTime;
    private Music music;
    private StringBuilder sb;
    private BotController botController;

    public BotController getBotController() {
        return botController;
    }

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
        this.bulletController = new BulletController(this);
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

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bulletController;
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

    public StringBuilder getSb() {
        return sb;
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        checkLevel(dt);
        if (nextLevel) {
            return;
        }
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        asteroidController.update(dt);
        powerUpsController.update(dt);
        botController.update(dt);
        infoController.update(dt);
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
                    botController.setup(ScreenManager.SCREEN_WIDTH + 32, MathUtils
                            .random(0, ScreenManager.SCREEN_HEIGHT));
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
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot b = botController.getActiveList().get(j);
                if (a.getHitArea().overlaps(b.getHitArea())) {
                    float dst = a.getPosition().dst(b.getPosition());
                    float halfOverLen = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2;
                    tempVec.set(b.getPosition()).sub(a.getPosition()).nor();
                    b.getPosition().mulAdd(tempVec, halfOverLen);
                    a.getPosition().mulAdd(tempVec, -halfOverLen);

                    float sumScl = b.getHitArea().radius + a.getHitArea().radius;
                    b.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                    a.getVelocity().mulAdd(tempVec, -b.getHitArea().radius / sumScl * 100);
                }
            }
        }
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.getEffectBuilder().bulletCollideWithSubject(b);

                    b.deactivate();
                    if (a.takeDamage(b.getOwner().getCurWeapon().getDamage())) {
                        if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                            hero.addScore(a.getHpMax() * 100);
                            for (int k = 0; k < 3; k++) {
                                powerUpsController.setup(a.getPosition().x, a.getPosition().y,
                                        a.getScale() * 0.25f);
                            }
                        }
                    }
                    break;
                }
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
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage(b.getOwner().getCurWeapon().getDamage());
                    sb.setLength(0);
                    sb.append("HP - ").append(b.getOwner().getCurWeapon().getDamage());
                    infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
                    b.deactivate();
                    particleController.getEffectBuilder().bulletCollideWithSubject(b);
                }
            }
            if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                for (int j = 0; j < botController.getActiveList().size(); j++) {
                    Bot bot = botController.getActiveList().get(j);
                    if (bot.getHitArea().contains(b.getPosition())) {
                        bot.takeDamage(b.getOwner().getCurWeapon().getDamage());
                        b.deactivate();
                        particleController.getEffectBuilder().bulletCollideWithSubject(b);
                    }
                }
            }
        }
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot1 = botController.getActiveList().get(i);
            if (bot1.getHitArea().overlaps(hero.getHitArea())) {
                float dst = bot1.getPosition().dst(hero.getPosition());
                float halfOverLen = (bot1.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                tempVec.set(hero.getPosition()).sub(bot1.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                bot1.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius + bot1.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, bot1.getHitArea().radius / sumScl * 30);
                bot1.getVelocity().mulAdd(tempVec, -hero.getHitArea().radius / sumScl * 30);

                bot1.takeDamage(1);
                hero.takeDamage(1);
                sb.setLength(0);
                sb.append("HP - 1");
                infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
            }
            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot bot2 = botController.getActiveList().get(j);
                if (bot1 == bot2) {
                    continue;
                } else {
                    if (bot1.getHitArea().overlaps(bot2.getHitArea())) {
                        float dst = bot1.getPosition().dst(bot2.getPosition());
                        float halfOverLen = (bot1.getHitArea().radius + bot2.getHitArea().radius - dst) / 2;
                        tempVec.set(bot2.getPosition()).sub(bot1.getPosition()).nor();
                        bot2.getPosition().mulAdd(tempVec, halfOverLen);
                        bot1.getPosition().mulAdd(tempVec, -halfOverLen);

                        float sumScl = bot2.getHitArea().radius + bot1.getHitArea().radius;
                        bot2.getVelocity().mulAdd(tempVec, bot1.getHitArea().radius / sumScl * 30);
                        bot1.getVelocity().mulAdd(tempVec, -bot2.getHitArea().radius / sumScl * 30);
                    }
                }
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}
