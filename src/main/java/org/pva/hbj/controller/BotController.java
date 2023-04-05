package org.pva.hbj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.provider.ParamsProvider;
import org.pva.hbj.service.JourneyService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {

    private final ParamsProvider paramsProvider;
    private final JourneyService journeyService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null && update.getMessage() != null) {
            var word = update.getMessage().getText();
            log.info(word);

            try {
                var message = switch (word) {
                    case "/start" -> journeyService.beginJourney(update);
                    default -> journeyService.beginJourney(update);
                }

                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.toString());
            }
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
