package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Journey {
    private Level startLevel;
    private Level currentLevel;

    public boolean hasNextLevel() {
        return this.currentLevel.getNextLevel() != null;
    }

    public void goNextLevel() {
        if (hasNextLevel())
            this.currentLevel = this.currentLevel.getNextLevel();
    }
}
