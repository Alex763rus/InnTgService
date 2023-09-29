package com.example.inntgservice.model.menu.search;

import com.example.inntgservice.enums.State;
import com.example.inntgservice.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.SEARCH_BY_PHONE;
import static com.example.inntgservice.enums.State.FREE;
import static com.example.inntgservice.enums.State.WAIT_PHONE;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component
@Slf4j
public class MenuSearchByPhone extends MenuSearchBase {

    private static final String MENU_COMMAND = SEARCH_BY_PHONE;
    private static final String INPUT_TEXT = "Введите телефон:";
    private static final String DESCRIPTION = "Поиск по телефону";

    private static final State WAIT_STATE = WAIT_PHONE;

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
            return createMessageList(user, ex.getMessage());
        }
        return createErrorDefaultMessage(user);
    }

    private List<PartialBotApiMethod> freeLogic(User user) {
        stateService.setState(user, WAIT_STATE);
        return createMessageList(user, INPUT_TEXT);
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
            return createErrorDefaultMessage(user);
        }
        val answer = new ArrayList<PartialBotApiMethod>();
        val message = update.getMessage().getText();
        if (message == null || message.equals(EMPTY) || message.trim().length() < 6 || !checkLong(message)) {
            answer.add(createMessage(user, getCheckPhoneErrorMessageText(message)));
        } else {
            val innInfoList = innInfoRepository.findTop10ByPhoneContains(message);
            if (innInfoList.size() == 0) {
                val errorText = "По введенным данным записей в БД не найдено: " + message;
                answer.add(createMessage(user, errorText));
            } else {
                val statistic = createStatistic(user);
                statistic.setPhone(message);
                statisticRepository.save(statistic);
                answer.addAll(createInnInfoMessaages(user, innInfoList));
            }
        }
        answer.add(createMessage(user, INPUT_TEXT));
        return answer;
    }

}
