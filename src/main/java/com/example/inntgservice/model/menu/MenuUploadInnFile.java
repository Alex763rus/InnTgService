package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import com.example.inntgservice.service.excel.InnUploaderService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.UPLOAD_INN_FILE;
import static com.example.inntgservice.enums.FileType.USER_IN;
import static com.example.inntgservice.enums.State.*;

@Component
@Slf4j
public class MenuUploadInnFile extends Menu {

    @Autowired
    protected InnUploaderService innUploaderService;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update);
            case WAIT_UPLOAD_FILE:
                return convertFileLogic(user, update);
        }
        return createErrorDefaultMessage(user);
    }

    @Override
    public String getMenuComand() {
        return UPLOAD_INN_FILE;
    }

    @Override
    public String getDescription() {
        return "Загрузить файл";
    }

    protected List<PartialBotApiMethod> convertFileLogic(User user, Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                try {
                    val field = update.getMessage().getDocument();
                    val fileFullPath = fileUploadService.getFileName(USER_IN, field.getFileName());
                    val book = fileUploadService.uploadXlsx(fileFullPath, field.getFileId());
                    innUploaderService.uploadBookToDb(book);
                    stateService.refreshUser(user);
                    return createMessageList(user, "Успешная загрузка");
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    return createMessageList(user, ex.getMessage());
                }
            } else {
                return createMessageList(user, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return createErrorDefaultMessage(user);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        stateService.setState(user, WAIT_UPLOAD_FILE);
        return createMessageList(user, "Отправьте исходный файл");
    }

    private List<PartialBotApiMethod> uploadFileWithoutTg(User user, Update update) {
        if (!update.getMessage().getText().equals(getMenuComand())) {
            return createErrorDefaultMessage(user);
        }
        XSSFWorkbook book = null;
        try {
            book = fileUploadService.uploadXlsxFromServer("C:\\Users\\grigorevap\\Desktop\\inn\\svod_lite.xlsx");
            innUploaderService.uploadBookToDb(book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return createMessageList(user, "Успешно загружено!");
    }

}
