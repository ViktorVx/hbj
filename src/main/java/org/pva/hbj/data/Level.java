package org.pva.hbj.data;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Level {
    private String id;
    private Message message;
    private Message winMessage;
    private String secretLevelCode;
    private Level nextLevel;

    public Message getMessage() {
        return this.message;
    }

    public Message getWinMessage() {
        return this.winMessage;
    }

    public abstract JourneyMode getJourneyMode();

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
