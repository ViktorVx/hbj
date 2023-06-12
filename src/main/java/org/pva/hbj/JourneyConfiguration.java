package org.pva.hbj;

import org.pva.hbj.data.JourneyMode;
import org.pva.hbj.data.Level;
import org.pva.hbj.provider.story.StoryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JourneyConfiguration {

    @Bean
    public Level startLevel(StoryProvider storyProvider) {
        return storyProvider.makeStoryLine();
    }

    @Bean
    public Level currentLevel(Level startLevel) {
        return startLevel;
    }

    @Bean
    public JourneyMode journeyMode() {
        return JourneyMode.NONE;
    }
}
