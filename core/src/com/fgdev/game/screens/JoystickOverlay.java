package com.fgdev.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fgdev.game.Constants;
import com.fgdev.game.logics.GameScreenLogic;
import com.fgdev.game.utils.Assets;

public class JoystickOverlay implements Disposable {

    private static final String TAG = JoystickOverlay.class.getName();

    private Stage stage;
    private SpriteBatch batch;
    OrthographicCamera cam;
    Viewport viewport;
    GameScreenLogic gameScreenLogic;

    private boolean upPressed, downPressed, leftPressed, rightPressed, throwPressed, meleePressed, jumpThrowPressed, climPressed;

    public JoystickOverlay(SpriteBatch batch, final GameScreenLogic gameScreenLogic) {
        this.gameScreenLogic = gameScreenLogic;
        this.batch = batch;
        cam = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, cam);
        stage = new Stage(viewport, batch);

        final Image imgUp = new Image(Assets.instance.joystick.up);
        imgUp.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgDown = new Image(Assets.instance.joystick.down);
        imgDown.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgLeft = new Image(Assets.instance.joystick.left);
        imgLeft.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgRight = new Image(Assets.instance.joystick.right);
        imgRight.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgThrow = new Image(Assets.instance.joystick.attackThrow);
        imgThrow.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgMelee = new Image(Assets.instance.joystick.melee);
        imgMelee.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgJumpThrow = new Image(Assets.instance.joystick.jumpThrow);
        imgJumpThrow.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        final Image imgClimb = new Image(Assets.instance.joystick.climb);
        imgClimb.setSize(Constants.WINDOW_HEIGHT / 9f, Constants.WINDOW_HEIGHT / 9f);

        Table tableLeft = new Table();
        tableLeft.left().bottom();

        tableLeft.add();
        tableLeft.add(imgClimb).size(imgClimb.getWidth(), imgClimb.getHeight());
        tableLeft.add();
        tableLeft.row().pad(5, 5, 5, 5);
        tableLeft.add(imgLeft).size(imgLeft.getWidth(), imgLeft.getHeight());
        tableLeft.add();
        tableLeft.add(imgRight).size(imgRight.getWidth(), imgRight.getHeight());
        tableLeft.row().padBottom(5);
        tableLeft.add();
        tableLeft.add(imgDown).size(imgDown.getWidth(), imgDown.getHeight());
        tableLeft.add();

        Table tableRight = new Table();
        tableRight.right().bottom();

        tableRight.add();
        tableRight.add(imgUp).size(imgUp.getWidth(), imgUp.getHeight());
        tableRight.add();
        tableRight.row().pad(5, 5, 5, 5);
        tableRight.add(imgThrow).size(imgThrow.getWidth(), imgThrow.getHeight());
        tableRight.add();
        tableRight.add(imgMelee).size(imgMelee.getWidth(), imgMelee.getHeight());
        tableRight.row().padBottom(5);
        tableRight.add();
        tableRight.add(imgJumpThrow).size(imgJumpThrow.getWidth(), imgJumpThrow.getHeight());
        tableRight.add();

        imgUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                imgUp.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.upHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
                imgUp.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.up)));
            }
        });
        imgDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                imgDown.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.downHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
                imgDown.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.down)));
            }
        });

        imgLeft.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                imgLeft.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.leftHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
                imgLeft.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.left)));
            }
        });
        imgRight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                imgRight.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.rightHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
                imgRight.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.right)));
            }
        });
        imgThrow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                throwPressed = true;
                imgThrow.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.attackThrowHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                throwPressed = false;
                imgThrow.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.attackThrow)));
            }
        });

        imgMelee.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                meleePressed = true;
                imgMelee.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.meleeHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                meleePressed = false;
                imgMelee.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.melee)));
            }
        });

        imgJumpThrow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpThrowPressed = true;
                imgJumpThrow.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.jumpThrowHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpThrowPressed = false;
                imgJumpThrow.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.jumpThrow)));
            }
        });

        imgClimb.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                climPressed = true;
                imgClimb.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.climbHover)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                climPressed = false;
                imgClimb.setDrawable(new SpriteDrawable(new Sprite(Assets.instance.joystick.climb)));
            }
        });
        stage.clear();
        Stack stack = new Stack();
        stack.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stack.add(tableLeft);
        stack.add(tableRight);
        stage.addActor(stack);

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){

                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.R:
                        break;
                    case Input.Keys.ENTER:
                        break;
                    case Input.Keys.BACK:
                    case Input.Keys.ESCAPE:
                        gameScreenLogic.backToMenu();
                        break;
                }
                return true;
            }
        });

    }

    public void render() {
        stage.draw();
    }

    public void resize(final int width, final int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isThrowPressed() {
        return throwPressed;
    }

    public boolean isMeleePressed() {
        return meleePressed;
    }

    public boolean isJumpThrowPressed() {
        return jumpThrowPressed;
    }

    public boolean isClimPressed() {
        return climPressed;
    }

    public Stage getStage() {
        return stage;
    }
}
