package org.pva.hbj.provider.story;

import lombok.Data;
import org.pva.hbj.data.JourneyMode;

import java.util.List;

@Data
public class LoadDTO {
    private JourneyMode type;
    private String secretLevelCode;
    private List<StoryPageLoadDTO> storyPages;
}
