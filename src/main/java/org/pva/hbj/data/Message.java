package org.pva.hbj.data;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private String text;
    @Builder.Default
    private String mediaPath = "";
    @Builder.Default
    private MediaType mediaType = MediaType.TEXT;

    public Message(String text) {
        this.text = text;
    }
}
