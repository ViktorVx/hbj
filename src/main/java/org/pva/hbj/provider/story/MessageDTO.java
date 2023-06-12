package org.pva.hbj.provider.story;

import lombok.Data;
import org.pva.hbj.data.MediaType;
import org.pva.hbj.utils.keyboards.KeyboardType;

@Data
public class MessageDTO {
    private KeyboardType keyboard;
    private MediaType mediaType;
    private String mediaPath;
    private String text;
}
