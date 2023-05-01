package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.utils.keyboards.GamerCompleteKeyboard;

public class Level11 {

    public static Level generate() {
        return Level.builder()
                .isTask(true)
                .message(Message.builder()
                        .text("Почеши нос)")
                        .keyboard(GamerCompleteKeyboard.create())
                        .build())
                .secretLevelCode("l12")
                .build();
    }
}
