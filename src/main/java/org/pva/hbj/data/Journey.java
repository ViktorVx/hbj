package org.pva.hbj.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@AllArgsConstructor
@Slf4j
public class Journey {
    private Level startLevel;
    private Level currentLevel;
    private boolean codeEnterMode;

    public boolean isCodeEnterMode() {
        return this.codeEnterMode;
    }

    public void enterCodeModeOn() {
        this.codeEnterMode = true;
    }

    public void enterCodeModeOff() {
        this.codeEnterMode = false;
    }

    public boolean hasNextLevel() {
        return this.currentLevel.getNextLevel() != null;
    }

    public void goNextLevel() {
        if (hasNextLevel())
            this.currentLevel = this.currentLevel.getNextLevel();
    }

    public String getLevelTask() {
        return this.currentLevel.getText();
    }

    public boolean checkAnswer(String answer) {
        return this.getCurrentLevel().getAnswer().equalsIgnoreCase(answer);
    }

    public boolean doWin() {
        return !hasNextLevel();
    }

    public void reset() {
        this.currentLevel = this.startLevel;
    }

    public Journey() {
        var startLevel = Level.builder().text("1+1=?").answer("2").secretLevelCode("l1").build();
        startLevel
                .addNext(Level.builder().text("2+2=?").answer("4").secretLevelCode("l2").build())
                .addNext(Level.builder().text("3+3=?").answer("6").secretLevelCode("l3").build());

        this.setStartLevel(startLevel);
        this.setCurrentLevel(startLevel);
        this.codeEnterMode = false;
        log.info("Ready!");
    }
}
