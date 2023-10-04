package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.service.excel.CrossLinkUploaderService;
import com.example.inntgservice.service.excel.InnUploaderService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.UPLOAD_LINK_FILE;
import static com.example.inntgservice.enums.FileType.USER_IN;
import static com.example.inntgservice.enums.State.WAIT_UPLOAD_FILE;

@Component(UPLOAD_LINK_FILE)
@Slf4j
public class MenuUploadCrossLinkFile extends Menu {

    @Autowired
    protected CrossLinkUploaderService crossLinkUploaderService;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update);
            default -> createErrorDefaultMessage(user);
        };
    }

    @Override
    public String getMenuComand() {
        return UPLOAD_LINK_FILE;
    }

    @Override
    public String getDescription() {
        return "Загрузить файл";
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        val files = new File(botConfig.getLinkFilePathDownloadNeed()).listFiles();
        for (File file : files) {
            log.info("--> Начал обрабатываться файл: " + file.getName());
            try (val book = fileUploadService.uploadXlsxFromServer(String.valueOf(file.toPath()))) {
                crossLinkUploaderService.uploadBookToDb(book);
            } catch (Exception e) {
                log.error("Ошибка во время обработки файла: " + file.getName() + " " + e.getMessage());
//                moveToError(file);
                continue;
            }
            moveToOk(file);
            log.info("<-- Файл успешно обработан: " + file.getName());
        }
        stateService.setState(user, WAIT_UPLOAD_FILE);
        return createMessageList(user, "Успешно загружено файлов: " + files.length);
    }

    private void moveToOk(File source) {
        move(source, botConfig.getLinkFilePathDownloadOk());
    }

    private void moveToError(File source) {
        move(source, botConfig.getLinkFilePathDownloadError());
    }

    private void move(File source, String target) {
        try {
            Files.move(source.toPath(), Path.of(target + source.getName()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
