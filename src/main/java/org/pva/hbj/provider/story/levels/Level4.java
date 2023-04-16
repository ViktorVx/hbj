package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

public class Level4 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder().text("3+3=?").build())
                .answer("6")
                .secretLevelCode("l4")
                .build();
    }
}
