package de.obvious.ld32.game.ui;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.misc.UiTextEvent;

public class UiText extends Label {
	private UiRoot root;
	private static final float textTime = 3f;
	private float time = 0;
	private HashMap<String, Boolean> texts;

	public UiText(UiRoot root) {
		super(" ", new LabelStyle(Resource.FONT.retro, new Color(Color.WHITE)));
		this.root = root;
		setAlignment(Align.center);
		texts = new HashMap<String, Boolean>();
	}

	@Override
	public void act(float delta) {
		root.getWorld().pollEvents(UiTextEvent.class, (UiTextEvent event) -> changeText(event));
		setWidth(Constants.SCREEN_WIDTH);
		setY(300);
		time += delta;
		if (time >= textTime) {
			setText("");
		}

		super.act(delta);
	}

	private void changeText(UiTextEvent event) {
		String txt = event.getTextPlayer();
		if ( (texts.get(txt) == null && event.isStory()) ||(!event.isStory() && time >= textTime)) {
			texts.put(txt, true);
			if(txt.length() > 45){
				String tmp1 = txt.substring(0, 44);
				String tmp2 = txt.substring(44, txt.length());

				txt = tmp1 + "\n" + tmp2;
			}


			setText(txt);

			time = 0;
		}
	}
}
