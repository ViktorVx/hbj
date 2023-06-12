package org.pva.hbj.utils.story;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.StoryLevel;

public class Level3 {

    public static Level generate() {
        return StoryLevel.builder()
                .secretLevelCode("l3")
//                .storyPages(
//                List.of(
//                        Message.builder().text("Page0").keyboard(StoryKeyboard.create()).build(),
//                        Message.builder().text("Page1").keyboard(StoryKeyboard.create()).build(),
//                        Message.builder().text("Page2").keyboard(StoryKeyboard.create()).build())
//        )
                .build();
    }
}
