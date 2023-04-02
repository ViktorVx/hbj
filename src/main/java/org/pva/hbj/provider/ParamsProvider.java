package org.pva.hbj.provider;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class ParamsProvider {

    @Value("${telegram.bot.hbj.name}")
    private String botName;
    @Value("${telegram.bot.hbj.token}")
    private String botToken;


    public String getBotName() {
        return this.botName;
    }

    public String getBotToken() {
        return this.botToken;
    }
}
