package de.obvious.ld32.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.shared.game.world.GameState;

public class StoryLabel extends Label {
    private UiRoot root;

    public StoryLabel(UiRoot root) {
        super("In the future, earth is forced into a nuclear war between three countries. This made earth almost uninhabitable. \n"
                + "Survivors tried their best to recover their planet while a few were sent to galaxies far far away to find a new inhabitable environment.\n\n"
                + "SHIELD (Secret Heroic Idea Economy Ludum Dare), under the leadership of Obvious Industries, send astronauts to the Tentacle system. \nAmong them was the Spacemonkey...\n\n"
                + "The spaceship crashed on the planet Swig and it seems like only Heroman was able to survive. What will await him on the new planet? What will he discover? And what will he have to do to escape Swig again?",
                new LabelStyle(Resource.FONT.retroSmall, new Color(1f, 1f, 0.67f, 1f)));
        this.root = root;
        setWrap(true);
        setWidth(Constants.SCREEN_WIDTH - 400);
        setX(150);
        setHeight(Constants.SCREEN_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(root.getWorld().getGameState() == GameState.INIT && root.getWorld().getStateTime() > 3.6f);
    }
}
