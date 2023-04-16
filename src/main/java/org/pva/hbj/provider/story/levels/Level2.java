package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;

public class Level2 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder()
                        .text("2+2=?")
                        .mediaType(MediaType.VIDEO)
                        .mediaPath("video/hv.mp4")
                        .build())
                .answer("4")
                .secretLevelCode("l2")
                .build();
    }
}
