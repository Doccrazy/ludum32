package de.obvious.ld32.game.misc;

import java.util.ArrayList;

public class UiTextEvent extends de.obvious.shared.game.event.Event {
	private boolean story;
	private ArrayList<StoryText> texts;


	public UiTextEvent(ArrayList<StoryText> texts, boolean story) {
		super(0, 0);
		this.texts = texts;
		this.story = story;
	}


	public ArrayList<StoryText> getTexts() {
		return texts;
	}


	public void setTexts(ArrayList<StoryText> texts) {
		this.texts = texts;
	}


	public boolean isStory() {
		return story;
	}

	public void setStory(boolean show) {
		this.story = show;
	}
}
