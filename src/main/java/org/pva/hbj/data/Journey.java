package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.controller.AdminBot;
import org.pva.hbj.service.SaveService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Builder
@Component
@Slf4j
public class Journey {
    private Level startLevel;
    private Level currentLevel;
    private JourneyMode journeyMode;
    private SaveService saveService;
    private AdminBot adminBot;

    @PostConstruct
    private void postConstruct() {
        if (saveService.saveModeEnabled()) {
            if (saveService.saveFileExists()) {
                var levelCode = saveService.load();
                moveToLevelBySecretCode(levelCode);
            } else {
                log.error("Файла не найдено");
            }
        }
    }

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
                    this.journeyMode = this.currentLevel.isStory() ? JourneyMode.STORY : JourneyMode.QUESTION;
                    return;
                }
                level = level.getNextLevel();
            }
        }
    }

    public boolean isCodeEnterMode() {
        return this.journeyMode == JourneyMode.CODE;
    }

    public boolean isStoreMode() {
        return this.journeyMode == JourneyMode.STORY;
    }

    public boolean isNoneMode() {
        return this.journeyMode == JourneyMode.NONE;
    }

    public void enterCodeModeOn() {
        this.journeyMode = JourneyMode.CODE;
    }

    public void storyModeOn() {
        this.journeyMode = JourneyMode.STORY;
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
            if (saveService.saveModeEnabled()) {
                saveService.save(this.currentLevel.getSecretLevelCode());
            }
            if (this.currentLevel.isStory()) {
                this.journeyMode = JourneyMode.STORY;
            } else if (this.currentLevel.getIsTask()) {
                this.journeyMode = JourneyMode.TASK;
                adminBot.check(this.currentLevel);
            } else {
                this.journeyMode = JourneyMode.QUESTION;
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
        if (this.currentLevel.getIsTask()) {
            adminBot.check(this.currentLevel);
            return this.currentLevel.getIsTaskComplete();
        } else {
            return this.getCurrentLevel().getAnswer().equalsIgnoreCase(answer);
        }
    }

    public boolean doWin() {
        return !hasNextLevel();
    }

    public void reset() {
        this.currentLevel = this.startLevel;
        updateMode();
        resetStories();
        if (saveService.saveModeEnabled()) {
            saveService.clearProgress();
        }
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
            this.journeyMode = JourneyMode.STORY;
        } else {
            this.journeyMode = JourneyMode.QUESTION;
        }
    }
}
