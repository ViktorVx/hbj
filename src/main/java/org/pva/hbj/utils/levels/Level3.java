package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.utils.keyboards.StoryKeyboard;

import java.util.List;

public class Level3 {

    public static Level generate() {
        return Level.builder().isStory(true).storyPages(
                List.of(
                        Message.builder().text("Page0").keyboard(StoryKeyboard.create()).build(),
                        Message.builder().text("Page1").keyboard(StoryKeyboard.create()).build(),
                        Message.builder().text("Page2").keyboard(StoryKeyboard.create()).build())
        ).build();
    }
}
