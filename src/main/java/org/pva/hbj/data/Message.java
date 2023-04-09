package org.pva.hbj.data;

import lombok.*;

@Data
@Builder
public class Message {
    private String text;
    private String imagePath;
}
