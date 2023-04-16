package org.pva.hbj.data;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private String text;
    @Builder.Default
    private String mediaPath = "";
    @Builder.Default
    private MediaType mediaType = MediaType.TEXT;
    @Builder.Default
    private InlineKeyboardMarkup keyboard = null;

    public Message(String text) {
        this.text = text;
    }
}
