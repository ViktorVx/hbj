package org.pva.hbj.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.provider.story.StoryProvider;
import org.springframework.stereotype.Component;

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

    public void enterCodeModeOff() {
        updateMode();
        resetStories();
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

    public Message getLevelTask() {
        return this.currentLevel.getMessage();
    }

    public Message getLevelStory() {
        if (this.currentLevel.isStory()) {
            return this.getCurrentLevel().getMessage();
        } else {
            return new Message("");
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
        resetStories();
    }

    private void resetStories() {
        var level = this.startLevel;
        while (level != null) {
            if (level.isStory()) {
                level.setPagePointer(0);
            }
            level = level.getNextLevel();
        }
    }

    private void updateMode() {
        if (this.currentLevel.isStory()) {
            this.mode = JourneyMode.STORY;
        } else {
            this.mode = JourneyMode.QUESTION;
        }
    }

    public Journey() {
        var startLevel = StoryProvider.makeStoryLine();

        this.setStartLevel(startLevel);
        this.setCurrentLevel(startLevel);
        this.mode = JourneyMode.NONE;
        log.info("Ready!");
    }
}
