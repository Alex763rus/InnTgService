package com.example.inntgservice.model.menu.search;

import com.example.inntgservice.enums.State;
import com.example.inntgservice.model.jpa.InnInfo;
import com.example.inntgservice.model.jpa.Statistic;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.SEARCH_BY_INN_HEAD;
import static com.example.inntgservice.constant.Constant.NEW_LINE;
import static com.example.inntgservice.enums.State.*;

@Component
@Slf4j
public class MenuSearchInnHead extends MenuSearchBase {

    private final static String MENU_COMMAND = SEARCH_BY_INN_HEAD;

    private final static String INPUT_TEXT = "Введите ИНН руководителя:";
    private final static String DESCRIPTION = "Поиск по ИНН руководителя";

    private final static State WAIT_STATE = WAIT_INN_HEAD;

    @Override
    public String getMenuComand() {
        return MENU_COMMAND;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            if (stateService.getState(user) == FREE) {
                return freeLogic(user);
            }
            if (stateService.getState(user) == WAIT_STATE) {
                return waitLogic(user, update);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return errorMessage(update, ex.getMessage());
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user) {
        stateService.setState(user, WAIT_STATE);
        return createSendMessage(user, INPUT_TEXT);
    }

    private List<PartialBotApiMethod> waitLogic(User user, Update update) throws ParseException {
        if (!update.hasMessage()) {
            return errorMessageDefault(update);
        }
        val message = update.getMessage().getText();
        Long innHead = 0L;
        val answer = new ArrayList<PartialBotApiMethod>();
        try {
            innHead = Long.parseLong(message);
        } catch (Exception ex) {
            val errorText = "Ошибка. Некорректный ИНН: " + message
                    + NEW_LINE + "ИНН может содержать только цифры.";
            answer.addAll(createSendMessage(user, errorText));
            answer.addAll(createSendMessage(user, INPUT_TEXT));
            return answer;
        }
        val innInfoList = innInfoRepository.findTop10ByHeadInnIs(innHead);
        if (innInfoList.size() == 0) {
            val errorText = "По введенным данным записей в БД не найдено: " + message;
            answer.addAll(createSendMessage(user, errorText));
        } else {
            val statistic = createStatistic(user);
            statistic.setInnHead(innHead);
            statisticRepository.save(statistic);
            answer.addAll(createInnInfoMessaages(user, innInfoList));
        }
        answer.addAll(createSendMessage(user, INPUT_TEXT));
        return answer;
    }

}
