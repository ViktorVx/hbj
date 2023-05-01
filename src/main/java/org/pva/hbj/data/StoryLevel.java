package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class StoryLevel extends Level {

    private List<Message> storyPages;
    @Builder.Default
    private Integer pagePointer = 0;

    @Override
    public Message getMessage() {
        return storyPages.get(pagePointer);
    }

    @Override
    public JourneyMode getJourneyMode() {
        return JourneyMode.STORY;
    }

    public void goNextPage() {
        this.pagePointer ++;
    }
}
