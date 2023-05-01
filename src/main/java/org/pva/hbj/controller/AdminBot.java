package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Level;
import org.pva.hbj.provider.ParamsProvider;
import org.pva.hbj.utils.keyboards.AdminCheckKeyboard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBot extends TelegramLongPollingBot {

    private final ParamsProvider paramsProvider;
    @Value("${telegram.bot.hbj-admin.chat}")
    private final String adminChatId;
    private Level currentCheckLevel;

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
            case "/accept" -> accept();
        }
    }

    private void accept() {
        this.currentCheckLevel.setIsTaskComplete(true);
        sendTextMessage("Зачтено!");
    }

    private void sendTextMessage(String msg) {
        try {
            var message = new SendMessage();
            message.setChatId(adminChatId);
            message.setText(msg);
            message.enableHtml(true);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    public void check(Level level) {
        this.currentCheckLevel = level;
        try {
            var message = new SendMessage();
            message.setChatId(adminChatId);
            message.setText(level.getMessage().getText());
            message.setReplyMarkup(AdminCheckKeyboard.create());
            message.enableHtml(true);
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return paramsProvider.getAdminBotName();
    }

    @Override
    public String getBotToken() {
        return paramsProvider.getAdminBotToken();
    }
}
