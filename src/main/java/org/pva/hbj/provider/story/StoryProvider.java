package org.pva.hbj.provider.story;

import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Level;
import org.pva.hbj.utils.levels.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StoryProvider {

    private StoryProvider() {
    }

    public static Level makeStoryLine() {
        var startLevel = Level0.generate();
        startLevel
                .addNext(Level1.generate())
                .addNext(Level11.generate())
                .addNext(Level2.generate())
                .addNext(Level3.generate())
                .addNext(Level4.generate())
                .addNext(Level5.generate())
                .addNext(Level6.generate());
        return startLevel;
    }
}
