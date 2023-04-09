package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Level {
    private String answer;
    private Message message;
    private String secretLevelCode;
    private Level nextLevel;

    private boolean isStory;
    private List<Message> storyPages;
    @Builder.Default
    private Integer pagePointer = 0;

    public Message getMessage() {
        return isStory ? storyPages.get(pagePointer) : this.message;
    }

    public void goNextPage() {
        pagePointer ++;
    }

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
