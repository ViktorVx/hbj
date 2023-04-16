package org.pva.hbj.provider.story;

import org.pva.hbj.data.Level;
import org.pva.hbj.provider.story.levels.*;

public class StoryProvider {

    private StoryProvider() {
    }

    public static Level makeStoryLine() {
        var startLevel = Level0.generate();
        startLevel
                .addNext(Level1.generate())
                .addNext(Level2.generate())
                .addNext(Level3.generate())
                .addNext(Level4.generate())
                .addNext(Level5.generate());
        return startLevel;
    }
}
