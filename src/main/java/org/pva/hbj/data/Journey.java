package org.pva.hbj.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@Component
@AllArgsConstructor
@Slf4j
public class Journey {
    private Level startLevel;
    private Level currentLevel;
    private JourneyMode mode;

    public boolean secretCodeExists(String secretCode) {
        var level = this.startLevel;
        while (level != null) {
            if (level.getSecretLevelCode() != null && level.getSecretLevelCode().equals(secretCode)) {
                log.info("{} --- {}", level.getSecretLevelCode(), secretCode);
                return true;
            }
            level = level.getNextLevel();
        }
        return false;
    }

    public void moveToLevelBySecretCode(String secretCode) {
        if (secretCodeExists(secretCode)) {
            var level = this.startLevel;
            while (level != null) {
                if (level.getSecretLevelCode() != null && level.getSecretLevelCode().equals(secretCode)) {
                    this.currentLevel = level;
                    return;
                }
                level = level.getNextLevel();
            }
        }
    }

    public boolean isCodeEnterMode() {
        return this.mode == JourneyMode.CODE;
    }

    public boolean isStoreMode() {
        return this.mode == JourneyMode.STORY;
    }

    public boolean isNoneMode() {
        return this.mode == JourneyMode.NONE;
    }

    public void enterCodeModeOn() {
        this.mode = JourneyMode.CODE;
    }

    public void storyModeOn() {
        this.mode = JourneyMode.STORY;
    }

    public void storyModeOff() {
        if (this.currentLevel.isStory()) {
            this.mode = JourneyMode.STORY;
        } else {
            this.mode = JourneyMode.QUESTION;
        }
    }

    public void enterCodeModeOff() {
        if (this.currentLevel.isStory()) {
            this.mode = JourneyMode.STORY;
        } else {
            this.mode = JourneyMode.QUESTION;
        }
    }

    public boolean hasNextLevel() {
        return this.currentLevel.getNextLevel() != null;
    }

    public void goNextLevel() {
        if (hasNextLevel()) {
            this.currentLevel = this.currentLevel.getNextLevel();
            if (this.currentLevel.isStory()) {
                this.mode = JourneyMode.STORY;
            } else {
                this.mode = JourneyMode.QUESTION;
            }
        }
    }

    public String getLevelTask() {
        return this.currentLevel.getText();
    }

    public String getLevelStory() {
        if (this.currentLevel.isStory()) {
            return this.getCurrentLevel().getText();
        } else {
            return "";
        }
    }

    public void goNextPage() {
        if (this.currentLevel.isStory()) {
            this.currentLevel.goNextPage();
        }
    }

    public boolean isLastStoryPage() {
        int pointer = this.currentLevel.getPagePointer();
        int pages = this.currentLevel.getStoryPages().size() - 1;
        return pointer == pages;
    }

    public boolean checkAnswer(String answer) {
        return this.getCurrentLevel().getAnswer().equalsIgnoreCase(answer);
    }

    public boolean doWin() {
        return !hasNextLevel();
    }

    public void reset() {
        this.currentLevel = this.startLevel;
        updateMode();
    }

    private void updateMode() {
        if (this.currentLevel.isStory()) {
            this.mode = JourneyMode.STORY;
        } else {
            this.mode = JourneyMode.QUESTION;
        }
    }

    public Journey() {
        var startLevel = Level.builder().message(Message.builder().text("1+1=?").build()).answer("2").secretLevelCode("l1").build();
        startLevel
                .addNext(Level.builder().message(Message.builder().text("2+2=?").build()).answer("4").secretLevelCode("l2").build())
                .addNext(Level.builder().isStory(true).storyPages(
                        List.of(
                                Message.builder().text("Page0").build(),
                                Message.builder().text("Page1").build(),
                                Message.builder().text("Page2").build())
                ).build())
                .addNext(Level.builder().message(Message.builder().text("3+3=?").build()).answer("6").secretLevelCode("l3").build());

        this.setStartLevel(startLevel);
        this.setCurrentLevel(startLevel);
        this.mode = JourneyMode.NONE;
        log.info("Ready!");
    }
}
