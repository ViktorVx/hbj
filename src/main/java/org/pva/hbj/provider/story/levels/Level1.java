package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.data.Message;

public class Level1 {

    public static Level generate() {
        return Level.builder()
                .message(Message.builder()
                        .text("1+1=?")
                        .mediaType(MediaType.IMAGE)
                        .mediaPath("images/gravity-falls-hd-wallpapers-wallpaper-cave-1-800x800.png")
                        .build())
                .answer("2")
                .secretLevelCode("l1")
                .build();
    }
}
