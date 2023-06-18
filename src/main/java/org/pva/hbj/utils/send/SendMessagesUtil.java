package org.pva.hbj.utils.send;

import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.controller.AdminBot;
import org.pva.hbj.controller.Bot;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Slf4j
public class SendMessagesUtil {

    private SendMessagesUtil() {}

    public static void sendMessage(Bot bot, AdminBot adminBot, Update update, String msg) {
        sendMessage(bot, adminBot, update, Message.builder().text(msg).mediaType(MediaType.TEXT).build());
    }

    public static void sendMessage(Bot bot, AdminBot adminBot, Update update, Message msg) {
        if (msg.getMediaType() == null) {
            sendTextMessage(bot, adminBot, update, msg);
        } else {
            switch (msg.getMediaType()) {
                case TEXT  -> sendTextMessage(bot, adminBot, update, msg);
                case IMAGE -> sendImageMessage(bot, adminBot, update, msg);
                case VIDEO -> sendVideoMessage(bot, adminBot, update, msg);
                case AUDIO -> sendAudioMessage(bot, adminBot, update, msg);
            }
        }
    }

    private static void sendTextMessage(Bot bot, AdminBot adminBot, Update update, Message msg) {
        try {
            var message = new SendMessage();
            message.setChatId(extractChatId(update));
            message.setText(msg.getText());
            message.enableHtml(true);
            if (msg.getKeyboard() != null) {
                message.setReplyMarkup(msg.getKeyboard());
            }
            bot.execute(message);
            //***
            adminBot.sendTextMessage(message.getText());
            //***
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static void sendImageMessage(Bot bot, AdminBot adminBot, Update update, Message msg) {
        try {
            var photo = new InputFile(new File(bot.getMediaPath() + msg.getMediaPath()), "filename");
            var message = new SendPhoto();
            message.setChatId(extractChatId(update));
            message.setCaption(msg.getText());
            message.setPhoto(photo);
            if (msg.getKeyboard() != null) {
                message.setReplyMarkup(msg.getKeyboard());
            }
            bot.execute(message);
            //***
            adminBot.sendTextMessage(msg.getText());
            //***
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static void sendAudioMessage(Bot bot, AdminBot adminBot, Update update, Message msg) {
        try {
            var audio = new InputFile(new File(bot.getMediaPath() + msg.getMediaPath()), "filename");
            var message = new SendAudio();
            message.setChatId(extractChatId(update));
            message.setCaption(msg.getText());
            message.setAudio(audio);
            if (msg.getKeyboard() != null) {
                message.setReplyMarkup(msg.getKeyboard());
            }
            bot.execute(message);
            //***
            adminBot.sendTextMessage(msg.getText());
            //***
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static void sendVideoMessage(Bot bot, AdminBot adminBot, Update update, Message msg) {
        try {
            var video = new InputFile(new File(bot.getMediaPath() + msg.getMediaPath()), "filename");
            var message = new SendVideo();
            message.setChatId(extractChatId(update));
            message.setCaption(msg.getText());
            message.setVideo(video);
            if (msg.getKeyboard() != null) {
                message.setReplyMarkup(msg.getKeyboard());
            }
            bot.execute(message);
            //***
            adminBot.sendTextMessage(msg.getText());
            //***
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    private static String extractChatId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        } else {
            return update.getMessage().getChatId().toString();
        }
    }
}
