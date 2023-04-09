package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String text;
    private String imagePath;
}
