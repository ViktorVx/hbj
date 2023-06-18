package org.pva.hbj.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public abstract class Bot extends TelegramLongPollingBot {

    public abstract String getMediaPath();
}
