package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Journey;
import org.pva.hbj.data.Message;
import org.pva.hbj.provider.ParamsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class JourneyBot extends TelegramLongPollingBot {

    private final ParamsProvider paramsProvider;
    private final Journey journey;
    @Value("${telegram.bot.hbj.media-path}")
    private String mediaPath;

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null && update.getMessage() != null) {
            var word = update.getMessage().getText();
            log.info(word);
            switch (word) {
                case "/start" -> start(update);
                case "/code" -> applyCode(update);
                case "/cancel" -> cancel(update);
                case "/next" -> next(update);
                default -> process(update);
            }
        }
    }

    private void next(Update update) {
        if (!journey.isStoreMode()) {
            return;
        }
        if (journey.isLastStoryPage()) {
            journey.goNextLevel();
            if (journey.getCurrentLevel().isStory()) {
                sendMessage(update, journey.getLevelStory());
            } else {
                sendMessage(update, journey.getLevelTask());
            }
        } else {
            journey.goNextPage();
            sendMessage(update, journey.getLevelStory());
        }
    }

    private void cancel(Update update) {
        if (!journey.isCodeEnterMode()) {
            return;
        }
        journey.enterCodeModeOff();
        sendMessage(update, journey.getLevelTask());
    }

    private void applyCode(Update update) {
        journey.enterCodeModeOn();
        sendMessage(update, "Введи секретный код: ");
    }

    private void start(Update update) {
        journey.reset();
        sendMessage(update, journey.getLevelTask());
    }

    private void process(Update update) {
        var answer = update.getMessage().getText();
        // None mode
        if (journey.isNoneMode()) {
            return;
        }
        // Code mode
        if (journey.isCodeEnterMode()) {
            log.info("Enter code mode");
            if (journey.secretCodeExists(answer)) {
                log.info("Secret code exist");
                journey.moveToLevelBySecretCode(answer);
                sendMessage(update, "Вжух)! И переносимся на нужный уровень)!");
                sendMessage(update, journey.getLevelTask());
                journey.enterCodeModeOff();
            } else {
                sendMessage(update, "Нету такого кода) ты хочешь меня надурить)");
            }
            return;
        }
        // Story mode
        if (journey.getCurrentLevel().isStory()) {
            journey.storyModeOn();
            return;
        }
        // Question mode
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
        sendMessage(update, Message.builder().text(msg).build());
    }

    private void sendMessage(Update update, Message msg) {
        switch (msg.getMediaType()) {
            case IMAGE -> sendImageMessage(update, msg);
            case VIDEO -> sendVideoMessage(update, msg);
            case AUDIO -> sendAudioMessage(update, msg);
            default -> sendTextMessage(update, msg);
        }
    }

    private void sendAudioMessage(Update update, Message msg) {
        try {
            var audio = new InputFile(new File(mediaPath + msg.getMediaPath()), "filename");
            var message = new SendAudio();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setCaption(msg.getText());
            message.setAudio(audio);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private void sendVideoMessage(Update update, Message msg) {
        try {
            var video = new InputFile(new File(mediaPath + msg.getMediaPath()), "filename");
            var message = new SendVideo();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setCaption(msg.getText());
            message.setVideo(video);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private void sendImageMessage(Update update, Message msg) {
        try {
            var photo = new InputFile(new File(mediaPath + msg.getMediaPath()), "filename");
            var message = new SendPhoto();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setCaption(msg.getText());
            message.setPhoto(photo);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private void sendTextMessage(Update update, Message msg) {
        try {
            var message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(msg.getText());
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
