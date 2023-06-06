package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendDocumentWrap;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import com.example.inntgservice.service.excel.InnUploaderService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.UPLOAD_INN_FILE;
import static com.example.inntgservice.constant.Constant.SHEET_RESULT_NAME;
import static com.example.inntgservice.enums.FileType.USER_IN;
import static com.example.inntgservice.enums.State.FREE;

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
//            case CONVERT_FILE_LENTA:
//                return convertFileLogic(user, update, convertServiceImplLenta);
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

                    val convertedBook = innUploaderService.getConvertedBook(book);
//                    val document = excelGenerateService.processXlsx(convertedBook, "result", SHEET_RESULT_NAME);
//                    stateService.setState(user, FREE);
//                    return Arrays.asList(SendDocumentWrap.init()
//                            .setChatIdLong(update.getMessage().getChatId())
//                            .setDocument(document)
//                            .build().createMessage());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            } else {
                return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return errorMessageDefault(update);
    }

    protected List<PartialBotApiMethod> freeLogic(User user, Update update) {
        if (!update.getMessage().getText().equals(getMenuComand())) {
            return errorMessageDefault(update);
        }
//        stateService.setState(user, state);
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Отправьте исходный файл")
                .build().createSendMessage());
    }
}
