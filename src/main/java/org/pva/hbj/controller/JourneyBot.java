package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Journey;
import org.pva.hbj.data.StoryLevel;
import org.pva.hbj.provider.ParamsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

import static org.pva.hbj.utils.send.SendMessagesUtil.sendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class JourneyBot extends TelegramLongPollingBot {

    private final ParamsProvider paramsProvider;
    private final Journey journey;
    @Value("${settings.message-new-level-delay}")
    private Integer newLevelDelay;

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
        switch (word) {
            case "/start" -> start(update);
            case "/code" -> applyCode(update);
            case "/cancel" -> cancel(update);
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
                sendMessage(this, update, "Ура! Ты победил!");
                journey.noneModeOn();
                return;
            }
            journey.goNextLevel();
            if (journey.getCurrentLevel() instanceof StoryLevel) {
                sendMessage(this, update, journey.getLevelStory());
            } else {
                sendMessage(this, update, journey.getLevelTask());
            }
        } else {
            journey.goNextPage();
            sendMessage(this, update, journey.getLevelStory());
        }
    }

    private void cancel(Update update) {
        if (!journey.isCodeEnterMode()) {
            return;
        }
        journey.enterCodeModeOff();
        sendMessage(this, update, journey.getLevelTask());
    }

    private void applyCode(Update update) {
        journey.enterCodeModeOn();
        sendMessage(this, update, "Введи секретный код: ");
    }

    private void start(Update update) {
        journey.reset();
        sendMessage(this, update, journey.getLevelTask());
    }

    private void process(Update update) throws InterruptedException {
        switch (journey.getJourneyMode()) {
            case NONE -> processNoneMode();
            case CODE -> processCodeMode(update);
            case STORY -> processStoryMode(update);
            case QUESTION -> processQuestionMode(update);
            case TASK -> processTaskMode(update);
        }
    }

    private void processTaskMode(Update update) {
        var answer = update.getCallbackQuery().getMessage().getText();
        var success = journey.checkAnswer(answer);
        var msg = success ? "Отлично)! Поехали дальше)!" : "Не, не, не)) ты хочешь меня надурить)!";
        if (success) {
            if (journey.doWin()) {
                sendMessage(this, update, "Ура! Ты победил!");
            } else {
                sendMessage(this, update, msg);
                journey.goNextLevel();
                sendMessage(this, update, journey.getLevelTask());
            }
        } else {
            sendMessage(this, update, msg);
            sendMessage(this, update, journey.getLevelTask());
        }
    }

    private void processQuestionMode(Update update) throws InterruptedException {
        var answer = update.getMessage().getText();
        var success = journey.checkAnswer(answer);
        var msg = success ? "Правильно)! Поехали дальше)!" : "Не верно( Подумай еще!";
        if (success) {
            if (journey.doWin()) {
                sendMessage(this, update, "Ура! Ты победил!");
            } else {
                sendMessage(this, update, msg);
                journey.goNextLevel();
                TimeUnit.SECONDS.sleep(newLevelDelay);
                sendMessage(this, update, journey.getLevelTask());
            }
        } else {
            sendMessage(this, update, msg);
            sendMessage(this, update, journey.getLevelTask());
        }
    }

    private void processStoryMode(Update update) {
        journey.storyModeOn();
        if (journey.doWin()) {
            journey.noneModeOn();
            sendMessage(this, update, "Ура! Ты победил!");
        }
    }

    private void processCodeMode(Update update) {
        var answer = update.getMessage().getText();
        log.info("Enter code mode");
        if (journey.secretCodeExists(answer)) {
            log.info("Secret code exist");
            journey.moveToLevelBySecretCode(answer);
            sendMessage(this, update, "Вжух)! И переносимся на нужный уровень)!");
            sendMessage(this, update, journey.getLevelTask());
            journey.enterCodeModeOff();
        } else {
            sendMessage(this, update, "Нету такого кода) ты хочешь меня надурить)");
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
}
