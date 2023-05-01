package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.QuestionLevel;

public class Level2 {

    public static Level generate() {
        return QuestionLevel.builder()
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
