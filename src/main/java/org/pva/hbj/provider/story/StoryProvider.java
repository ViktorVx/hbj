package org.pva.hbj.provider.story;

import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.*;
import org.pva.hbj.utils.levels.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StoryProvider {

    @Value("${telegram.bot.hbj.journey-load-file}")
    private String journeyLoadFile;

    private StoryProvider() {
    }

    public Level makeStoryLine() {
        try {
            makeStoryFromYaml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var startLevel = Level0.generate();
        startLevel
                .addNext(Level1.generate())
                .addNext(Level11.generate())
                .addNext(Level2.generate())
                .addNext(Level3.generate())
                .addNext(Level4.generate())
                .addNext(Level5.generate())
                .addNext(Level6.generate());
        return startLevel;
    }

    public Level makeStoryFromYaml() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(this.journeyLoadFile);
        Map<String, Object> journey = yaml.load(inputStream);
        //
        Level startLevel = null;
        Level level = null;
        var lvls = (List<Object>) journey.get("journey");
        Collections.sort(lvls, (o1, o2) ->
            ((String)((Map<String, Object>) o1).get("id")).compareToIgnoreCase((String)((Map<String, Object>) o2).get("id"))
        );
        for (Object lvl : lvls) {
            var type = JourneyMode.valueOf((String) ((Map) lvl).get("type"));
            var l = switch (type) {
                case STORY -> parseStory((Map<String, Object>) lvl);
                case QUESTION -> parseQuestion((Map<String, Object>) lvl);
                case TASK -> parseTask((Map<String, Object>) lvl);
                default -> parseQuestion((Map<String, Object>) lvl);
            };
            if (startLevel == null) {
                startLevel = l;
            }
            if (level != null) {
                level.addNext(l);
            }
            level = l;
        }
        //
        var l = startLevel;
        while (true) {
            log.info(l.toString());
            if (l.getNextLevel() == null) {
                break;
            }
            l = l.getNextLevel();
        }
        //
        return null;
    }

    private StoryLevel parseStory(Map<String, Object> obj) {
        return StoryLevel.builder()
                .secretLevelCode((String) obj.get("secret-code"))
                .build();
    }

    private QuestionLevel parseQuestion(Map<String, Object> obj) {
        return QuestionLevel.builder()
                .secretLevelCode((String) obj.get("secret-code"))
                .answer((String) obj.get("answer"))
                .message(parseMessage((Map<String, Object>) obj.get("message")))
                .build();
    }

    private TaskLevel parseTask(Map<String, Object> obj) {
        return TaskLevel.builder()
                .build();
    }

    private Message parseMessage(Map<String, Object> obj) {
        return Message.builder()
                .text((String) obj.get("text"))
                .build();
    }
}
