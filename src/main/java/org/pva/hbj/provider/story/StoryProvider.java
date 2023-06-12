package org.pva.hbj.provider.story;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.Level;
import org.pva.hbj.data.Message;
import org.pva.hbj.data.StoryLevel;
import org.pva.hbj.utils.keyboards.AdminCheckKeyboard;
import org.pva.hbj.utils.keyboards.GamerCompleteKeyboard;
import org.pva.hbj.utils.keyboards.StoryKeyboard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoryProvider {

    private final AdminCheckKeyboard adminCheckKeyboard;
    private final GamerCompleteKeyboard gamerCompleteKeyboard;
    private final StoryKeyboard storyKeyboard;

    @Value("${telegram.bot.hbj.journey-load-file}")
    private String journeyLoadFile;

    public Level makeStoryLine() {
        try {
            return makeStoryFromJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        var startLevel = Level0.generate();
//        startLevel
//                .addNext(Level1.generate())
//                .addNext(Level11.generate())
//                .addNext(Level2.generate())
//                .addNext(Level3.generate())
//                .addNext(Level4.generate())
//                .addNext(Level5.generate())
//                .addNext(Level6.generate());
//        return startLevel;
    }

    private Level makeStoryFromJson() throws IOException {
        String content = Files.readString(Path.of(journeyLoadFile), StandardCharsets.UTF_8);
        log.debug(content);
        var loadDTOList = new ObjectMapper().readValue(content, new TypeReference<List<LoadDTO>>() {});
        log.info("======");
        log.info(loadDTOList.toString());
        var levelList = loadDTOList.stream().map(this::dtoToLevel).toList();
        return levelList.get(0);
    }

    private Level dtoToLevel(LoadDTO loadDTO) {
        return switch (loadDTO.getType()) {
            case STORY -> StoryLevel.builder()
                    .secretLevelCode(loadDTO.getSecretLevelCode())
                    .storyPages(dtoToMesageList(loadDTO.getStoryPages()))
                    .build();
            default -> null;
        };
    }

    private List<Message> dtoToMesageList(List<StoryPageLoadDTO> loadPages) {
        return loadPages.stream().map(this::dtoToMessage).collect(Collectors.toList());
    }

    private Message dtoToMessage(StoryPageLoadDTO pageLoadDTO) {
        var keyboard = switch (pageLoadDTO.getKeyboard()) {
            case STORY_KEYBOARD -> storyKeyboard.create();
            case ADMIN_KEYBOARD -> adminCheckKeyboard.create();
            case GAMER_COMPLETE_KEYBOARD -> gamerCompleteKeyboard.create();
        };
        return Message.builder()
                .keyboard(keyboard)
                .text(pageLoadDTO.getText())
                .build();
    }
}
