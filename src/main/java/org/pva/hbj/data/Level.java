package org.pva.hbj.data;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Level {
    private Message message;
    private String secretLevelCode;
    private Level nextLevel;

    public Message getMessage() {
        return this.message;
    }

    public JourneyMode getJourneyMode() {
        return JourneyMode.NONE;
    }

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
