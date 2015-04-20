package de.obvious.ld32.game.misc;

public class StoryText {

	private String text;
	private String whoSpeaks;

	public StoryText(String text, String whoSpeaks) {
		this.text = text;
		this.whoSpeaks = whoSpeaks;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWhoSpeaks() {
		return whoSpeaks;
	}

	public void setWhoSpeaks(String whoSpeaks) {
		this.whoSpeaks = whoSpeaks;
	}



}
