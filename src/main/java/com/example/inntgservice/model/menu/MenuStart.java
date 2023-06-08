package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.Statistic;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.constant.Constant.NEW_LINE;
import static com.example.inntgservice.enums.State.WAIT_INN;
import static com.example.inntgservice.utils.StringUtils.prepareShield;

@Component
@Slf4j
public class MenuStart extends MenuSearchByInnBase {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            switch (stateService.getState(user)) {
                case FREE:
                    return freeLogic(user, update);
                case WAIT_INN:
                    return waitInnLogic(user, update);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return errorMessage(update, ex.getMessage());
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        val messageText = new StringBuilder();
        switch (user.getUserRole()) {
            case BLOCKED:
                messageText.append("Доступ запрещен");
            case SIMPLE_USER:
                return inputInnLogic(user, update);
            case ADMIN:
                messageText.append("Главное меню:").append(NEW_LINE)
                        .append("- поиск по ИНН: ").append(prepareShield(SEARCH_BY_INN)).append(NEW_LINE)
                        .append("- загрузить файл: ").append(prepareShield(UPLOAD_INN_FILE)).append(NEW_LINE)
                        .append("- сформировать статистику: ").append(prepareShield(CREATE_STATISTIC)).append(NEW_LINE)
                ;
                break;
        }
        return List.of(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(messageText.toString())
                .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
