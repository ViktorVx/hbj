package org.pva.hbj.data;


import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class QuestionLevel extends Level {

    private String answer;

    @Override
    public JourneyMode getJourneyMode() {
        return JourneyMode.QUESTION;
    }
}
