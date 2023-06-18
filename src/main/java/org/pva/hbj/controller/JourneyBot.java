package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Journey;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.StoryLevel;
import org.pva.hbj.provider.ParamsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

import static org.pva.hbj.utils.send.SendMessagesUtil.sendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class JourneyBot extends Bot {

    private final ParamsProvider paramsProvider;
    private final Journey journey;
    private final AdminBot adminBot;
    @Value("${settings.message-new-level-delay}")
    private Integer newLevelDelay;
    @Value("${telegram.bot.hbj.media-path}")
    private String mediaPath;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var word = "";
        if (update.hasCallbackQuery()) {
            word = update.getCallbackQuery().getData();
        } else if (update.hasMessage()) {
            word = update.getMessage().getText();
        }
        log.info(word);
        adminBot.sendTextMessage(word);
        switch (word) {
            case "/start" -> start(update);
            case "/next" -> next(update);
            default -> process(update);
        }
    }

    private void next(Update update) {
        if (!journey.isStoreMode()) {
            return;
        }
        if (journey.isLastStoryPage()) {
            if (journey.doWin()) {
                sendMessage(this, adminBot, update, "Ура! Ты победил!");
                journey.noneModeOn();
                return;
            }
            journey.goNextLevel();
            if (journey.getCurrentLevel() instanceof StoryLevel) {
                sendMessage(this, adminBot, update, journey.getLevelStory());
            } else {
                sendMessage(this, adminBot, update, journey.getLevelTask());
            }
        } else {
            journey.goNextPage();
            sendMessage(this, adminBot, update, journey.getLevelStory());
        }
    }

    private void start(Update update) {
        journey.reset();
        sendMessage(this, adminBot, update, journey.getLevelTask());
    }

    private void process(Update update) throws InterruptedException {
        switch (journey.getJourneyMode()) {
            case NONE -> processNoneMode();
            case STORY -> processStoryMode(update);
            case QUESTION -> processQuestionMode(update);
            case TASK -> processTaskMode(update);
        }
    }

    private void processTaskMode(Update update) {
        var answer = update.getCallbackQuery().getMessage().getText();
        var success = journey.checkAnswer(answer);
        Message msg;
        if (success) {
            if (journey.getCurrentLevel().getWinMessage() == null) {
                msg = Message.builder().mediaType(MediaType.TEXT).text("Ура! Получилось)!").build();
            } else {
                msg = journey.getCurrentLevel().getWinMessage();
            }
        } else {
            msg = Message.builder().mediaType(MediaType.TEXT).text("Не, не, не)) ты хочешь меня надурить)!").build();
        }
        sendMessage(this, adminBot, update, msg);
        if (success) {
            if (journey.doWin()) {
                journey.noneModeOn();
            } else {
                journey.goNextLevel();
                sendMessage(this, adminBot, update, journey.getLevelTask());
            }
        } else {
            sendMessage(this, adminBot, update, journey.getLevelTask());
        }
    }

    private void processQuestionMode(Update update) throws InterruptedException {
        var answer = update.getMessage().getText();
        var success = journey.checkAnswer(answer);
        Message msg;
        if (success) {
            if (journey.getCurrentLevel().getWinMessage() == null) {
                msg = Message.builder().mediaType(MediaType.TEXT).text("Правильно)! Поехали дальше)!").build();
            } else {
                msg = journey.getCurrentLevel().getWinMessage();
            }
        } else {
            msg = Message.builder().mediaType(MediaType.TEXT).text("Не верно( Подумай еще!").build();
        }

        sendMessage(this, adminBot, update, msg);
        if (success) {
            if (journey.doWin()) {
                journey.noneModeOn();
            } else {
                journey.goNextLevel();
                TimeUnit.SECONDS.sleep(newLevelDelay);
                sendMessage(this, adminBot, update, journey.getLevelTask());
            }
        } else {
            sendMessage(this, adminBot, update, journey.getLevelTask());
        }
    }

    private void processStoryMode(Update update) {
        journey.storyModeOn();
        if (journey.doWin()) {
            journey.noneModeOn();
            sendMessage(this, adminBot, update, "Ура! Ты победил!");
        }
    }

    private void processNoneMode() {
        log.info("None mode processing complete");
    }

    @Override
    public String getBotUsername() {
        return paramsProvider.getBotName();
    }

    @Override
    public String getBotToken() {
        return paramsProvider.getBotToken();
    }

    @Override
    public String getMediaPath() {
        return this.mediaPath;
    }
}
