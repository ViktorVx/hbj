package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

import java.util.List;

public class Level3 {

    public static Level generate() {
        return Level.builder().isStory(true).storyPages(
                List.of(
                        Message.builder().text("Page0").build(),
                        Message.builder().text("Page1").build(),
                        Message.builder().text("Page2").build())
        ).build();
    }
}
