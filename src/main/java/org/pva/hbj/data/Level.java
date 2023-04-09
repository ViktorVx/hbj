package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Level {
    private String text;
    private String answer;
    private String imagePath;
    private String secretLevelCode;
    private Level nextLevel;

    private boolean isStory;
    private List<String> storyPages;
    @Builder.Default
    private Integer pagePointer = 0;

    public String getText() {
        return isStory ? storyPages.get(pagePointer) : text;
    }

    public void goNextPage() {
        pagePointer ++;
    }

    public Level addNext(Level nextLevel) {
        this.nextLevel = nextLevel;
        return nextLevel;
    }
}
