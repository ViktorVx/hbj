package org.pva.hbj.utils.story;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.QuestionLevel;

public class Level6 {

    public static Level generate() {
        return QuestionLevel.builder()
                .message(Message.builder().text("5+4=?").build())
                .answer("9")
                .secretLevelCode("l6")
                .build();
    }
}
