package org.pva.hbj.service;

import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Journey;
import org.pva.hbj.data.Level;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class JourneyService {

    private final Journey journey;

    public JourneyService() {
        var startLevel = Level.builder()
                .text("1+1=?")
                .answer("2")
                .secretLevelCode("DGFJV")
                .imagePath(null)
                .build();
        startLevel
                .addNext(Level.builder().text("2+2=?").answer("4").build())
                .addNext(Level.builder().text("3+3=?").answer("6").build());
        this.journey = Journey.builder()
                .startLevel(startLevel)
                .currentLevel(startLevel)
                .build();
        log.info("Ready!");
    }

    public SendMessage beginJourney(Update update) {
        var message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(this.journey.getStartLevel().getText());
        return message;
    }
}
