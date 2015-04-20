package de.obvious.ld32.game.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.misc.StoryText;
import de.obvious.ld32.game.misc.UiTextEvent;

public class UiText extends Label {
	private UiRoot root;
	private static final float textTime = 3f;
	private float time = 0;
	private HashMap<String, Boolean> texts;
	private ArrayList<StoryText> allTexts = new ArrayList<StoryText>();
	boolean showBubble;

	public UiText(UiRoot root) {
		super(" ", new LabelStyle(Resource.FONT.retro, new Color(Color.WHITE)));
		this.root = root;
		setAlignment(Align.top);
		texts = new HashMap<String, Boolean>();
		root.getWorld().getPlayer().setUiText(this);
	}

	@Override
	public void act(float delta) {
		root.getWorld().pollEvents(UiTextEvent.class, (UiTextEvent event) -> setNewEvent(event));
		setWidth(Constants.SCREEN_WIDTH);
		setY(70);
		time += delta;

		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (showBubble)
			batch.draw(Resource.GFX.bubble.getKeyFrame(time,true), 240, 10);
		super.draw(batch, parentAlpha);
	}

	private void setNewEvent(UiTextEvent event) {
		ArrayList<StoryText> newTexts = event.getTexts();
		for (StoryText storyText : newTexts) {
			if (texts.get(storyText.getText()) == null) {
				texts.put(storyText.getText(), true);
				allTexts.add(storyText);
			}
		}
		if (allTexts.size() > 0 && !showBubble)
			changeText();
	}

	public void changeText() {
		if (allTexts.size() == 0) {
			setText("");
			showBubble = false;
			return;
		}
		showBubble = true;

		StoryText storyText = allTexts.get(0);

		time = 0;
		String tmp = storyText.getText();
		if (tmp.length() > 50) {
			String tmp1 = tmp.substring(0, 50);
			String tmp2 = tmp.substring(50, tmp.length());
			tmp = tmp1 + "\n" + tmp2;
		}
		switch (storyText.getWhoSpeaks()) {
		case "Player":
			setColor(Color.BLUE);
			setText(tmp);
			break;
		case "Weapon":
			setColor(Color.BLACK);
			setText(tmp);
			break;
		case "Other":
			setColor(Color.RED);
			setText(tmp);
			break;

		default:
			break;
		}

		allTexts.remove(0);

	}

}
