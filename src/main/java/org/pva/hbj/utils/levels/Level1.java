package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

public class Level1 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder()
                        .text("1+1=?")
                        .build())
                .answer("2")
                .secretLevelCode("l1")
                .build();
    }
}
