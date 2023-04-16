package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

public class Level5 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder().text("4+4=?").build())
                .answer("8")
                .secretLevelCode("l5")
                .build();
    }
}
