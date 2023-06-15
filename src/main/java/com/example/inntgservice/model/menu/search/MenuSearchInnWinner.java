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
import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.*;
import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.enums.State.*;

@Component
@Slf4j
public class MenuSearchInnWinner extends MenuSearchBase {
    private final static String MENU_COMMAND = SEARCH_BY_INN;

    private final static String INPUT_TEXT = "Введите ИНН:";
    private final static String DESCRIPTION = "Поиск по ИНН";

    private final static State WAIT_STATE = WAIT_INN;

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
        val answer = new ArrayList<PartialBotApiMethod>();
        val message = update.getMessage().getText().replaceAll(SPACE, EMPTY);
        val innsSplitComma = Arrays.asList(message.split(","));
        val innList = splitList(innsSplitComma, NEW_LINE);
        for (String inn : innList) {
            answer.addAll(findInnWinner(user, inn));
        }
        answer.addAll(createSendMessage(user, INPUT_TEXT));
        return answer;
    }

    private List<String> splitList(List<String> list, String regex) {
        val result = new ArrayList<String>();
        for (String value : list) {
            result.addAll(Arrays.asList(value.split(regex)));
        }
        return result;
    }

    private List<PartialBotApiMethod> findInnWinner(User user, String message) throws ParseException {
        Long innWinner = 0L;
        val answer = new ArrayList<PartialBotApiMethod>();
        try {
            innWinner = Long.parseLong(message);
        } catch (Exception ex) {
            val errorText = "Ошибка. Некорректный ИНН: " + message
                    + NEW_LINE + "ИНН может содержать только цифры.";
            answer.addAll(createSendMessage(user, errorText));
            return answer;
        }
        val innInfo = innInfoRepository.findById(innWinner).orElse(null);
        if (innInfo == null) {
            val errorText = "По введенным данным записей в БД не найдено: " + message;
            answer.addAll(createSendMessage(user, errorText));
        } else {
            val statistic = createStatistic(user);
            statistic.setInn(innWinner);
            statisticRepository.save(statistic);
            answer.addAll(createInnInfoMessaages(user, List.of(innInfo)));
        }
        return answer;
    }

}
