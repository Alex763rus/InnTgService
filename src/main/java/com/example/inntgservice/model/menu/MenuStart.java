package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.utils.StringUtils.prepareShield;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_START)
@Slf4j
public class MenuStart extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freeLogic(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        val messageText = new StringBuilder();
        switch (user.getUserRole()) {
            case BLOCKED:
                messageText.append("Доступ запрещен");
            case SIMPLE_USER:
                messageText.append("Главное меню:").append(NEW_LINE)
                        .append("- поиск по ИНН организации: ").append(prepareShield(SEARCH_BY_INN)).append(NEW_LINE)
                        .append("- поиск по ИНН руководителя: ").append(prepareShield(SEARCH_BY_INN_HEAD)).append(NEW_LINE)
                        .append("- поиск по почте: ").append(prepareShield(SEARCH_BY_MAIL)).append(NEW_LINE)
                        .append("- поиск по телефону: ").append(prepareShield(SEARCH_BY_PHONE)).append(NEW_LINE)
                        .append("- поиск по сайту: ").append(prepareShield(SEARCH_BY_WEBSITE)).append(NEW_LINE)
                ;
                break;
            case ADMIN:
                messageText.append("Главное меню:").append(NEW_LINE)
                        .append("- поиск по ИНН организации: ").append(prepareShield(SEARCH_BY_INN)).append(NEW_LINE)
                        .append("- поиск по ИНН руководителя: ").append(prepareShield(SEARCH_BY_INN_HEAD)).append(NEW_LINE)
                        .append("- поиск по почте: ").append(prepareShield(SEARCH_BY_MAIL)).append(NEW_LINE)
                        .append("- поиск по телефону: ").append(prepareShield(SEARCH_BY_PHONE)).append(NEW_LINE)
                        .append("- поиск по сайту: ").append(prepareShield(SEARCH_BY_WEBSITE)).append(NEW_LINE)
                        .append("Меню администратора:").append(NEW_LINE)
                        .append("- загрузить файл: ").append(prepareShield(UPLOAD_INN_FILE)).append(NEW_LINE)
                        .append("- сформировать статистику: ").append(prepareShield(CREATE_STATISTIC)).append(NEW_LINE)
                ;
                break;
        }
        return createMessageList(user, messageText.toString());
    }

    @Override
    public String getDescription() {
        return "Начало работы";
    }
}
