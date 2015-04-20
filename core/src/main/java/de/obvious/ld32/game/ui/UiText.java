package de.obvious.ld32.game.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
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

	public UiText(UiRoot root) {
		super(" ", new LabelStyle(Resource.FONT.retro, new Color(Color.WHITE)));
		this.root = root;
		setAlignment(Align.center);
		texts = new HashMap<String, Boolean>();
	}

	@Override
	public void act(float delta) {
		root.getWorld().pollEvents(UiTextEvent.class, (UiTextEvent event) -> setNewEvent(event));
		setWidth(Constants.SCREEN_WIDTH);
		setY(300);
		time += delta;
		if (time >= textTime) {
			setText("");
		}

		super.act(delta);
	}

	private void setNewEvent(UiTextEvent event) {
		ArrayList<StoryText> newTexts = event.getTexts();
		float tmpTime = 0;
		if (texts.get(newTexts.get(0).getText()) == null) {
			texts.put(newTexts.get(0).getText(), true);
			for (StoryText storyText : newTexts) {
				root.getWorld().getPlayer().doSomethingForMe((Void) -> changeText(storyText), tmpTime);
				tmpTime += 3;
			}
		}
	}

	private void changeText(StoryText storyText) {
		System.out.println(storyText.getWhoSpeaks());
		time = 0;
		String tmp = storyText.getText();
		if(tmp.length()> 45){
			String tmp1 = tmp.substring(0, 45);
			String tmp2 = tmp.substring(45,tmp.length());
			tmp = tmp1 + "\n" + tmp2;
		}
		switch (storyText.getWhoSpeaks()) {
		case "Player":
			setColor(Color.BLUE);
			setText(tmp);
			break;
		case "Weapon":
			setColor(Color.WHITE);
			setText(tmp);
			break;
		case "Other":
			setColor(Color.RED);
			setText(tmp);
			break;

		default:
			break;
		}

	}

}
