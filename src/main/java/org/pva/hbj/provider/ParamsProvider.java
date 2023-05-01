package org.pva.hbj.provider;

import lombok.NoArgsConstructor;
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

    @Value("${telegram.bot.hbj-admin.name}")
    private String adminBotName;
    @Value("${telegram.bot.hbj-admin.token}")
    private String adminBotToken;

    public String getAdminBotName() {
        return this.adminBotName;
    }

    public String getAdminBotToken() {
        return this.adminBotToken;
    }

    public String getBotName() {
        return this.botName;
    }

    public String getBotToken() {
        return this.botToken;
    }
}
