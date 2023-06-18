package org.pva.hbj.provider.story;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pva.hbj.data.*;
import org.pva.hbj.utils.keyboards.AdminCheckKeyboard;
import org.pva.hbj.utils.keyboards.GamerCompleteKeyboard;
import org.pva.hbj.utils.keyboards.StoryKeyboard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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
    }

    private Level makeStoryFromJson() throws IOException {
        String content = Files.readString(Path.of(journeyLoadFile), StandardCharsets.UTF_8);
        var loadDTOList = new ObjectMapper().readValue(content, new TypeReference<List<LoadDTO>>() {});
        var levelList = loadDTOList.stream().map(this::dtoToLevel).toList();
        //===
        for (int i = 0; i < levelList.size(); i++) {
            if (i != levelList.size() - 1)
                levelList.get(i).setNextLevel(levelList.get(i + 1));
        }
        //===
        return levelList.get(0);
    }

    private Level dtoToLevel(LoadDTO loadDTO) {
        return switch (loadDTO.getType()) {
            case STORY -> StoryLevel.builder()
                    .secretLevelCode(loadDTO.getSecretLevelCode())
                    .storyPages(dtoToMesageList(loadDTO.getStoryPages()))
                    .build();
            case QUESTION -> QuestionLevel.builder()
                    .message(dtoToMessage(loadDTO.getMessage()))
                    .answer(loadDTO.getAnswer())
                    .secretLevelCode(loadDTO.getSecretLevelCode())
                    .winMessage(dtoToMessage(loadDTO.getWinMessage()))
                    .build();
            case TASK -> TaskLevel.builder()
                    .message(dtoToMessage(loadDTO.getMessage()))
                    .secretLevelCode(loadDTO.getSecretLevelCode())
                    .winMessage(dtoToMessage(loadDTO.getWinMessage()))
                    .build();
            default -> null;
        };
    }

    private List<Message> dtoToMesageList(List<MessageDTO> loadPages) {
        return loadPages.stream().map(this::dtoToMessage).collect(Collectors.toList());
    }

    private Message dtoToMessage(MessageDTO pageLoadDTO) {
        InlineKeyboardMarkup keyboard;
        if (pageLoadDTO == null) {
            return null;
        }
        if (pageLoadDTO.getKeyboard() == null) {
            keyboard = null;
        } else {
            keyboard = switch (pageLoadDTO.getKeyboard()) {
                case STORY_KEYBOARD -> storyKeyboard.create();
                case ADMIN_KEYBOARD -> adminCheckKeyboard.create();
                case GAMER_COMPLETE_KEYBOARD -> gamerCompleteKeyboard.create();
            };
        }
        return Message.builder()
                .keyboard(keyboard)
                .text(pageLoadDTO.getText())
                .mediaType(pageLoadDTO.getMediaType())
                .mediaPath(pageLoadDTO.getMediaPath())
                .build();
    }
}
