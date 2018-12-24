package com.fgdev.game.helpers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.Constants;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.logics.GameScreenLogic;

/**
 *
 *
 * ScoreIndicator
 */
public class ScoreIndicator implements Disposable {

    private final String TAG = ScoreIndicator.class.getName();

    class ScoreItem implements Pool.Poolable {
        float x;
        float y;
        float life;
        String score;

        public ScoreItem() {
            x = 0;
            y = 0;
            life = 0;
            score = "";
        }

        public void init(float x, float y, int score) {
            this.x = x;
            this.y = y;
            this.score = Integer.toString(score);
            life = 0.8f;
        }

        @Override
        public void reset() {
            life = 0;
        }
    }

    private BitmapFont font; // TODO: use TTF

    private SpriteBatch batch;
    private GameScreenLogic gameScreenLogic;
    private OrthographicCamera camera;

    private Pool<ScoreItem> scoreItemPool;
    private Array<ScoreItem> scoreItems;

    private float RATIO;

    public ScoreIndicator(GameScreenLogic gameScreenLogic, SpriteBatch batch) {
        this.gameScreenLogic = gameScreenLogic;
        this.batch = batch;

        RATIO = Constants.WINDOW_WIDTH / Constants.V_WIDTH;

        camera = new OrthographicCamera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        camera.position.set(gameScreenLogic.getCamera().position.x * RATIO, Constants.WINDOW_HEIGHT / 2, 0);

        font = Assets.instance.fonts.textFontSmall;
        font.getData().setScale(0.6f);

        scoreItemPool = new Pool<ScoreItem>() {
            @Override
            protected ScoreItem newObject() {
                return new ScoreItem();
            }
        };

        scoreItems = new Array<ScoreItem>();

    }

    public void addScoreItem(float x, float y, int score) {
        ScoreItem scoreItem = scoreItemPool.obtain();
        scoreItem.init(x * RATIO, (y + 1.25f) * RATIO, score);
        scoreItems.add(scoreItem);

    }

    public void update(float delta) {
        for (int i = 0; i < scoreItems.size; i++) {
            scoreItems.get(i).life -= delta;
            scoreItems.get(i).y += 1.2;
            if (scoreItems.get(i).life < 0) {
                scoreItemPool.free(scoreItems.get(i));
                scoreItems.removeIndex(i);
            }
        }
    }

    public void draw() {
        camera.position.x = gameScreenLogic.getCamera().position.x * RATIO;
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        for (ScoreItem scoreItem : scoreItems) {
            font.draw(batch, scoreItem.score, scoreItem.x, scoreItem.y);
        }

    }


    @Override
    public void dispose() {
        font.dispose();
    }
}
