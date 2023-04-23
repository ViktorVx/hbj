package org.pva.hbj.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class SaveService {

    @Value("${save.path")
    private String savePath;
    @Value("${save.enabled}")
    private Boolean saveEnabled;

    public Boolean saveModeEnabled() {
        return this.saveEnabled;
    }

    public Boolean saveFileExists() {
        return new File(this.savePath.concat("save.txt")).exists();
    }

    public void save(String levelCode) {
        if (this.saveEnabled) {
            try {
                Files.write(Paths.get(this.savePath.concat("save.txt")), levelCode.getBytes());
            } catch (IOException e) {
                log.error("Произошла ошибка при сохранении");
                throw new RuntimeException(e);
            }
            log.info("Saving: {} success", levelCode);
        }
    }

    public String load() {
        if (this.saveEnabled) {
            try {
                var levelCode = Files.readString(Path.of(this.savePath.concat("save.txt")));
                log.info("Loading: {} success", levelCode);
                return levelCode;
            } catch (IOException e) {
                log.error("Произошла ошибка при загрузке");
                throw new RuntimeException(e);
            }
        } else {
            return "";
        }
    }

    public void clearProgress() {
        if (this.saveEnabled) {
            var success = new File(this.savePath.concat("save.txt")).delete();
            if (success) {
                log.info("Очистка прогресса успешно завершена");
            } else {
                log.error("Произошла ошибка при очистке прогресса");
            }
        }
    }
}
