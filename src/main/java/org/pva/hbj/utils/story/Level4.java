package org.pva.hbj.utils.story;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.QuestionLevel;

public class Level4 {

    public static Level generate() {
        return QuestionLevel.builder()
                .message(Message.builder()
                        .mediaType(MediaType.IMAGE)
                        .mediaPath("images/gravity-falls-hd-wallpapers-wallpaper-cave-1-800x800.png")
                        .text("3+3=?")
                        .build())
                .answer("6")
                .secretLevelCode("l4")
                .build();
    }
}
