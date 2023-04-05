package org.pva.hbj.data;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class Journey {
    private Level startLevel;
    private Level currentLevel;


}
