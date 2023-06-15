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

import static com.example.inntgservice.constant.Constant.Command.SEARCH_BY_PHONE;
import static com.example.inntgservice.constant.Constant.EMPTY;
import static com.example.inntgservice.constant.Constant.NEW_LINE;
import static com.example.inntgservice.enums.State.*;

@Component
@Slf4j
public class MenuSearchByPhone extends MenuSearchBase {

    private final static String MENU_COMMAND = SEARCH_BY_PHONE;
    private final static String INPUT_TEXT = "Введите телефон:";
    private final static String DESCRIPTION = "Поиск по телефону";

    private final static State WAIT_STATE = WAIT_PHONE;

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

    private String getCheckPhoneErrorMessageText(String message) {
        val errorText = new StringBuilder();
        errorText.append("Введено некорректное значение: ").append(message).append(NEW_LINE)
                .append("Требования к искомому значению:").append(NEW_LINE)
                .append("- введенное значение должно быть числом").append(NEW_LINE)
                .append("- количество символов искомого номера должно быть больше 5");
        return errorText.toString();
    }

    private List<PartialBotApiMethod> waitLogic(User user, Update update) throws ParseException {
        if (!update.hasMessage()) {
            return errorMessageDefault(update);
        }
        val answer = new ArrayList<PartialBotApiMethod>();
        val message = update.getMessage().getText();
        if (message == null || message.equals(EMPTY) || message.trim().length() < 6 || !checkLong(message)) {
            answer.addAll(createSendMessage(user, getCheckPhoneErrorMessageText(message)));
        } else {
            val innInfoList = innInfoRepository.findTop10ByPhoneContains(message);
            if (innInfoList.size() == 0) {
                val errorText = "По введенным данным записей в БД не найдено: " + message;
                answer.addAll(createSendMessage(user, errorText));
            } else {
                val statistic = createStatistic(user);
                statistic.setPhone(message);
                statisticRepository.save(statistic);
                answer.addAll(createInnInfoMessaages(user, innInfoList));
            }
        }
        answer.addAll(createSendMessage(user, INPUT_TEXT));
        return answer;
    }

}
