package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Journey;
import org.pva.hbj.provider.ParamsProvider;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JourneyBot extends TelegramLongPollingBot {

    private final ParamsProvider paramsProvider;
    private final Journey journey;

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null && update.getMessage() != null) {
            var word = update.getMessage().getText();
            log.info(word);
            switch (word) {
                case "/start" -> resetJourney(update);
                case "/code" -> applyCode(update);
                default -> continueJourney(update);
            }
        }
    }

    private void applyCode(Update update) {
        journey.enterCodeModeOn();
        sendMessage(update, "Введи секретный код: ");
    }

    private void resetJourney(Update update) {
        journey.reset();
        sendMessage(update, "Начинаем)!");
        sendMessage(update, journey.getLevelTask());
    }

    private void continueJourney(Update update) {
        var answer = update.getMessage().getText();

        if (journey.isCodeEnterMode()) {
            if (journey.secretCodeExists(answer)) {
                journey.moveToLevelBySecretCode(answer);
                sendMessage(update, " Вжух)! И переносимся на нужный уровень)!");
                sendMessage(update, journey.getLevelTask());
            } else {
                sendMessage(update, "Нету такого кода) ты хочешь меня надурить)");
            }
            journey.enterCodeModeOff();
            return;
        }

        var success = journey.checkAnswer(answer);
        var msg = success ? "Правильно)! Поехали дальше)!" : "Не верно( Подумай еще!";
        if (success) {
            if (journey.doWin()) {
                sendMessage(update, "Ура! Ты победил!");
            } else {
                sendMessage(update, msg);
                journey.goNextLevel();
                sendMessage(update, journey.getLevelTask());
            }
        } else {
            sendMessage(update, msg);
            sendMessage(update, journey.getLevelTask());
        }
    }

    private void sendMessage(Update update, String msg) {
        try {
            var message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(msg);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
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
