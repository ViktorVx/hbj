package org.pva.hbj.data;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Level {
    private String id;
    private Message message;
    private String secretLevelCode;
    private Level nextLevel;

    public Message getMessage() {
        return this.message;
    }

    public abstract JourneyMode getJourneyMode();

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
