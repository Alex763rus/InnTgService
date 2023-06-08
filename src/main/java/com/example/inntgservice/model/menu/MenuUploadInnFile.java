package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import com.example.inntgservice.service.excel.InnUploaderService;
import jakarta.annotation.PostConstruct;
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
        return errorMessageDefault(update);
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
                    stateService.setState(user, FREE);
                    return Arrays.asList(SendMessageWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setText("Успешная загрузка")
                            .build().createSendMessage());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    return errorMessage(update, ex.getMessage());
                }
            } else {
                return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        stateService.setState(user, WAIT_UPLOAD_FILE);
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Отправьте исходный файл")
                .build().createSendMessage());
    }

    private List<PartialBotApiMethod> uploadFileWithoutTg(User user, Update update) {
        if (!update.getMessage().getText().equals(getMenuComand())) {
            return errorMessageDefault(update);
        }
        XSSFWorkbook book = null;
        try {
            book = fileUploadService.uploadXlsxFromServer("C:\\Users\\grigorevap\\Desktop\\inn\\svod_lite.xlsx");
            innUploaderService.uploadBookToDb(book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Успешно загружено!")
                .build().createSendMessage());
    }

}
