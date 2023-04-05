package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Level {
    private String text;
    private String answer;
    private String imagePath;
    private String secretLevelCode;
    private Level nextLevel;

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
