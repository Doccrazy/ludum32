package de.obvious.ld32.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

import de.obvious.ld32.data.GameRules;
import de.obvious.shared.game.BaseGameRenderer;
import de.obvious.shared.game.world.Box2dWorld;

public class GameRenderer extends BaseGameRenderer {
	private static final float CAM_PPS = 5f;

    private Scaling bgScaling = Scaling.fill;
	private float zoom = 1;
	private float zoomDelta = 0;
	private float camY;
    private boolean animateCamera;



    public GameRenderer(Box2dWorld world) {
        super(world, new Vector2(GameRules.LEVEL_WIDTH, GameRules.LEVEL_WIDTH * 9f / 16f));
    }

    @Override
    protected void init() {
        world.rayHandler.setAmbientLight(new Color(0.5f, 0.5f, 0.5f, 0.5f));
    }

    @Override
	protected void drawBackground(SpriteBatch batch) {
		//Gdx.gl.glClearColor(1, 1, 1, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Vector2 bgSize = bgScaling.apply(gameViewport.x, gameViewport.y, world.stage.getWidth(), world.stage.getHeight());
        //batch.draw(Resource.GFX.backgroundHigh, world.stage.getWidth() / 2 - bgSize.x / 2, 0, bgSize.x, bgSize.y);
        //batch.draw(Resource.GFX.backgroundLow, world.stage.getWidth() / 2 - bgSize.x / 2, -bgSize.y + 0.1f, bgSize.x, bgSize.y);
    }

	@Override
	protected void beforeRender() {
	    //zoom = MathUtils.clamp(zoom + zoomDelta*0.02f, 1f, 2f);

        camera.position.x = GameRules.LEVEL_WIDTH / 2;
        camera.position.y = GameRules.LEVEL_HEIGHT / 2;

        /*if (animateCamera) {
            camY -= Gdx.graphics.getDeltaTime() * CAM_PPS;
        }
        camera.position.x = GameRules.LEVEL_WIDTH / 2;
        camera.position.y = Math.max(camY, gameViewport.y/2 - GameRules.LEVEL_HEIGHT + 1);*/
	}
}
