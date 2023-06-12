package org.pva.hbj.utils.story;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.TaskLevel;

public class Level11 {

    public static Level generate() {
        return TaskLevel.builder()
                .message(Message.builder()
                        .text("Почеши нос)")
//                        .keyboard(GamerCompleteKeyboard.create())
                        .build())
                .secretLevelCode("l12")
                .build();
    }
}
