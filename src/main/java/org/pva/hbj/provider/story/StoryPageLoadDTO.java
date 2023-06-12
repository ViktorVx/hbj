package org.pva.hbj.provider.story;

import lombok.Data;
import org.pva.hbj.utils.keyboards.KeyboardType;

@Data
public class StoryPageLoadDTO {
    private KeyboardType keyboard;
    private String text;
}
