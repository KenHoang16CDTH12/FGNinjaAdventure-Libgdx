package com.fgdev.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetTexture textures;
    public AssetSounds sounds;
    public AssetMusic music;
    public AssetFonts fonts;
    public AssetPlayer player;
    public AssetGoldCoin goldCoin;
    public AssetFeather feather;
    public AssetItem item;

    // singleton: prevent instantiation from other classes
    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture png
        assetManager.load("images/bg.png", Texture.class);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_PLAYER_BOY, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_PLAYER_GIRL, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_ITEM, TextureAtlas.class);
        // load sounds
        assetManager.load("sounds/jump.wav", Sound.class);
        assetManager.load("sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/pickup_feather.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);
        // load music
        assetManager.load("musics/keith303_-_brand_new_highscore.mp3",
                Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlasPlayer =
                assetManager.get(GamePreferences.instance.isGirl ? Constants.TEXTURE_ATLAS_PLAYER_GIRL : Constants.TEXTURE_ATLAS_PLAYER_BOY);
        // enable texture filtering for pixel smoothing
        for (Texture texture: atlasPlayer.getTextures())
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        TextureAtlas atlasItem =
                assetManager.get(Constants.TEXTURE_ATLAS_ITEM);
        // enable texture filtering for pixel smoothing
        for (Texture texture: atlasItem.getTextures())
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // create game resource objects
        fonts = new AssetFonts();
        player = new AssetPlayer(atlasPlayer);
        goldCoin = new AssetGoldCoin(atlasItem);
        feather = new AssetFeather(atlasItem);
        item = new AssetItem(atlasItem);
        music = new AssetMusic(assetManager);
        sounds = new AssetSounds(assetManager);
        textures = new AssetTexture(assetManager);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '"
                + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    public class AssetTexture {

        public final Texture background;

        public AssetTexture (AssetManager am) {
            background = am.get("images/bg.png",
                    Texture.class);
        }

    }

    public class AssetSounds {

        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;

        public AssetSounds (AssetManager am) {
            jump = am.get("sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("sounds/jump_with_feather.wav",
                    Sound.class);
            pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
            pickupFeather = am.get("sounds/pickup_feather.wav",
                    Sound.class);
            liveLost = am.get("sounds/live_lost.wav", Sound.class);
        }

    }

    public class AssetMusic {

        public final Music song01;

        public AssetMusic (AssetManager am) {
            song01 = am.get("musics/keith303_-_brand_new_highscore.mp3",
                    Music.class);
        }

    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    public class AssetPlayer {
        public final TextureAtlas.AtlasRegion player;
        public final TextureAtlas.AtlasRegion kunai;
        public final Animation animIddle;
        public final Animation animRun;
        public final Animation animJump;
        public final Animation animClimb;
        public final Animation animDead;
        public final Animation animGlide;
        public final Animation animGlideBack;
        public final Animation animJumpAttack;
        public final Animation animJumpThrow;
        public final Animation animAttack;
        public final Animation animSlide;
        public final Animation animThrow;


        public AssetPlayer(TextureAtlas atlas) {
            player = atlas.findRegion("anim_iddle");

            kunai = atlas.findRegion("kunai");

            Array<TextureAtlas.AtlasRegion> regions = null;
            TextureAtlas.AtlasRegion region = null;
            // Animation: Iddle
            regions = atlas.findRegions("anim_iddle");
            animIddle = new Animation(1.0f / 10.0f, regions,
                    Animation.PlayMode.LOOP);
            // Animation: Run
            regions = atlas.findRegions("anim_run");
            animRun = new Animation(1.0f / 10.0f, regions);
            // Animation: Jump
            regions = atlas.findRegions("anim_jump");
            animJump = new Animation(1.0f / 6.0f, regions);
            // Animation: Climb
            regions = atlas.findRegions("anim_climb");
            animClimb = new Animation(1.0f / 10.0f, regions);
            // Animation: Dead
            regions = atlas.findRegions("anim_dead");
            animDead = new Animation(1.0f / 10.0f, regions);
            // Animation: Glide
            regions = atlas.findRegions("anim_glide");
            animGlide = new Animation(1.0f / 10.0f, regions);
            // Animation: Glide Back
            regions = atlas.findRegions("anim_glide");
            animGlideBack = new Animation(1.0f / 10.0f, regions,
                    Animation.PlayMode.REVERSED);
            // Animation: Jump Attack
            regions = atlas.findRegions("anim_jump_attack");
            animJumpAttack = new Animation(1.0f / 30.0f, regions);
            // Animation: Jump Throw
            regions = atlas.findRegions("anim_jump_throw");
            animJumpThrow = new Animation(1.0f / 10.0f, regions);
            // Animation: Attack
            regions = atlas.findRegions("anim_attack");
            animAttack = new Animation(1.0f / 10.0f, regions);
            // Animation: Slide
            regions = atlas.findRegions("anim_slide");
            animSlide = new Animation(1.0f / 10.0f, regions);
            // Animation: Throw
            regions = atlas.findRegions("anim_throw");
            animThrow = new Animation(1.0f / 10.0f, regions);
        }
    }

    public class AssetGoldCoin {
        public final TextureAtlas.AtlasRegion goldCoin;
        public final Animation animGoldCoin;

        public AssetGoldCoin (TextureAtlas atlas) {
            goldCoin = atlas.findRegion("item_gold_coin");
            Array<TextureAtlas.AtlasRegion> regions = null;
            TextureAtlas.AtlasRegion region = null;
            // Animation: Gold Coin
            regions = atlas.findRegions("anim_gold_coin");
            animGoldCoin = new Animation(1.0f / 20.0f, regions,
                    Animation.PlayMode.LOOP_PINGPONG);
        }
    }

    public class AssetFeather {
        public final TextureAtlas.AtlasRegion feather;
        public AssetFeather (TextureAtlas atlas) {
            feather = atlas.findRegion("item_feather");
        }
    }

    public class AssetItem {

        public final TextureAtlas.AtlasRegion barrel1;
        public final TextureAtlas.AtlasRegion barrel2;
        public final TextureAtlas.AtlasRegion mushroom1;
        public final TextureAtlas.AtlasRegion mushroom2;
        public final TextureAtlas.AtlasRegion crate;
        public final TextureAtlas.AtlasRegion box;
        public final TextureAtlas.AtlasRegion icebox;
        public final TextureAtlas.AtlasRegion stone;
        public final TextureAtlas.AtlasRegion stoneblock;

        public AssetItem (TextureAtlas atlas) {
            barrel1 = atlas.findRegion("barrel", 1);
            barrel2 = atlas.findRegion("barrel", 2);
            mushroom1 = atlas.findRegion("mushroom", 1);
            mushroom2 = atlas.findRegion("mushroom", 2);
            crate = atlas.findRegion("crate");
            box = atlas.findRegion("box");
            icebox = atlas.findRegion("icebox");
            stone = atlas.findRegion("stone");
            stoneblock = atlas.findRegion("stoneblock");
        }

    }
}
