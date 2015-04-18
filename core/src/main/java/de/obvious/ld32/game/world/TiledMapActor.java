package de.obvious.ld32.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import de.obvious.ld32.core.Resource;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.Box2dWorld;

public class TiledMapActor extends WorldActor {

	private OrthogonalTiledMapRenderer renderer;

	public TiledMapActor(Box2dWorld world) {
		super(world);
		renderer = new OrthogonalTiledMapRenderer(Resource.GFX.LEVEL1, 1f/50f);
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		renderer.setView((OrthographicCamera) world.stage.getCamera());

		renderer.render();
	}

	@Override
	protected void doAct(float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.PLUS)){

		}
	}

}
