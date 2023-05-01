package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TaskLevel extends Level {

    @Builder.Default
    private Boolean isTaskComplete = false;
}
