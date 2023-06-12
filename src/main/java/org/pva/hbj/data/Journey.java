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
                    this.journeyMode = this.currentLevel instanceof StoryLevel ? JourneyMode.STORY : JourneyMode.QUESTION;
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

    public void noneModeOn() {
        this.journeyMode = JourneyMode.NONE;
    }

    public void enterCodeModeOff() {
        updateMode();
        resetStories();
        resetTasks();
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
            if (this.currentLevel instanceof StoryLevel) {
                this.journeyMode = JourneyMode.STORY;
            } else if (this.currentLevel instanceof TaskLevel) {
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
        if (this.currentLevel instanceof StoryLevel) {
            return this.getCurrentLevel().getMessage();
        } else {
            return new Message("");
        }
    }

    public void goNextPage() {
        if (this.currentLevel instanceof StoryLevel) {
            ((StoryLevel)this.currentLevel).goNextPage();
        }
    }

    public boolean isLastStoryPage() {
        int pointer = ((StoryLevel)this.currentLevel).getPagePointer();
        int pages = ((StoryLevel)this.currentLevel).getStoryPages().size() - 1;
        return pointer == pages;
    }

    public boolean checkAnswer(String answer) {
        if (this.currentLevel instanceof TaskLevel) {
            adminBot.check(this.currentLevel);
            return ((TaskLevel)this.currentLevel).getIsTaskComplete();
        } else {
            return ((QuestionLevel)this.getCurrentLevel()).getAnswer().equalsIgnoreCase(answer);
        }
    }

    public boolean doWin() {
        return !hasNextLevel();
    }

    public void reset() {
        this.currentLevel = this.startLevel;
        updateMode();
        resetStories();
        resetTasks();
        if (saveService.saveModeEnabled()) {
            saveService.clearProgress();
        }
    }

    private void resetTasks() {
        var level = this.startLevel;
        while (level != null) {
            if (level instanceof TaskLevel) {
                ((TaskLevel) level).setIsTaskComplete(false);
            }
            level = level.getNextLevel();
        }
    }

    private void resetStories() {
        var level = this.startLevel;
        while (level != null) {
            if (level instanceof StoryLevel) {
                ((StoryLevel) level).setPagePointer(0);
            }
            level = level.getNextLevel();
        }
    }

    private void updateMode() {
        if (this.currentLevel instanceof StoryLevel) {
            this.journeyMode = JourneyMode.STORY;
        } else {
            this.journeyMode = JourneyMode.QUESTION;
        }
    }
}
