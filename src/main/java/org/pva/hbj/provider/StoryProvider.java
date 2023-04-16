package org.pva.hbj.provider;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;

import java.util.List;

public class StoryProvider {

    private StoryProvider() {
    }

    public static Level makeStoryLine() {
        var startLevel = StoryProvider.intro0();
        startLevel
                .addNext(StoryProvider.level1())
                .addNext(StoryProvider.level2())
                .addNext(StoryProvider.story3())
                .addNext(StoryProvider.level4());
        return startLevel;
    }

    private static Level intro0() {
        return Level.builder()
                .isStory(true)
                .storyPages(List.of(Message.builder().text("Начинаем)!").build()))
                .build();
    }

    private static Level level1() {
        return Level.builder()
                .message(Message.builder().text("1+1=?").imagePath("gravity-falls-hd-wallpapers-wallpaper-cave-1-800x800.png").build())
                .answer("2")
                .secretLevelCode("l1")
                .build();
    }

    private static Level level2() {
        return Level.builder()
                .message(Message.builder().text("2+2=?").build())
                .answer("4")
                .secretLevelCode("l2")
                .build();
    }

    private static Level story3() {
        return Level.builder().isStory(true).storyPages(
                List.of(
                        Message.builder().text("Page0").build(),
                        Message.builder().text("Page1").build(),
                        Message.builder().text("Page2").build())
        ).build();
    }

    private static Level level4() {
        return Level.builder()
                .message(Message.builder().text("3+3=?").build())
                .answer("6")
                .secretLevelCode("l3")
                .build();
    }
}
