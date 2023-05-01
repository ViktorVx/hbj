package org.pva.hbj.utils.levels;

import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.StoryLevel;
import org.pva.hbj.utils.keyboards.StoryKeyboard;

import java.util.List;

public class Level0 {
    public static Level generate() {
        return StoryLevel.builder()
                .secretLevelCode("l0")
                .storyPages(List.of(Message.builder()
                        .keyboard(StoryKeyboard.create())
                        .text("""
                              Начинаем)!
                              <u><b>Привет всем)!</b></u>
                              Я тут 🤘
                              """)
                        .build()))
                .build();
    }
}
