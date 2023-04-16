package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;

public class Level5 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder()
                        .text("4+4=?")
                        .mediaType(MediaType.AUDIO)
                        .mediaPath("audio/tree.mp3")
                        .build())
                .answer("8")
                .secretLevelCode("l5")
                .build();
    }
}
