package org.pva.hbj.provider.story.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

import java.util.List;

public class Level0 {
    public static Level generate() {
        return Level.builder()
                .isStory(true)
                .storyPages(List.of(Message.builder().text("Начинаем)!").build()))
                .build();
    }
}
