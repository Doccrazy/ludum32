package de.obvious.ld32.game.misc;

public class UiTextEvent extends de.obvious.shared.game.event.Event {
	private String textPlayer;
	private String textWeapon;
	private boolean story;
	private boolean player;
	private String textOther;

	public UiTextEvent(String textPlayer, String textWeapon, String textOther, boolean story, boolean playerFirst) {
		super(0, 0);
		this.textPlayer = textPlayer;
		this.textWeapon = textWeapon;
		this.textOther = textOther;
		this.story = story;
		this.player = player;
	}

	public String getTextOther() {
		return textOther;
	}

	public void setTextOther(String textOther) {
		this.textOther = textOther;
	}

	public String getTextPlayer() {
		return textPlayer;
	}

	public void setTextPlayer(String textPlayer) {
		this.textPlayer = textPlayer;
	}

	public String getTextWeapon() {
		return textWeapon;
	}

	public void setTextWeapon(String textWeapon) {
		this.textWeapon = textWeapon;
	}

	public boolean isStory() {
		return story;
	}

	public void setStory(boolean show) {
		this.story = show;
	}

	public boolean isPlayer() {
		return player;
	}

	public void setPlayer(boolean player) {
		this.player = player;
	}

}
