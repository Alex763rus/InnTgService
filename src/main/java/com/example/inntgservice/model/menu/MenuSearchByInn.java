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
public class MenuSearchByInn extends MenuSearchByInnBase {
    @Override
    public String getMenuComand() {
        return SEARCH_BY_INN;
    }

    @Override
    public String getDescription() {
        return "Поиск по ИНН";
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            switch (stateService.getState(user)) {
                case FREE:
                    return inputInnLogic(user, update);
                case WAIT_INN:
                    return waitInnLogic(user, update);
            }
        } catch (Exception ex){
            log.error(ex.getMessage());
            return errorMessage(update, ex.getMessage());
        }
        return errorMessageDefault(update);
    }

}
