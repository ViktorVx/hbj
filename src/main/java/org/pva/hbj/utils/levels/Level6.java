package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

public class Level6 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder().text("5+4=?").build())
                .answer("9")
                .secretLevelCode("l5")
                .build();
    }
}
