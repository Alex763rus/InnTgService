package com.example.inntgservice.model.menu.search;

import com.example.inntgservice.enums.State;
import com.example.inntgservice.model.jpa.InnCrosslink;
import com.example.inntgservice.model.jpa.InnCrosslinkKey;
import com.example.inntgservice.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.SEARCH_BY_INN;
import static com.example.inntgservice.enums.State.FREE;
import static com.example.inntgservice.enums.State.WAIT_INN;
import static org.example.tgcommons.constant.Constant.TextConstants.*;

@Component(SEARCH_BY_INN)
@Slf4j
public class MenuSearchInnWinner extends MenuSearchBase {
    private static final String INPUT_TEXT = "Введите ИНН:";
    private static final String DESCRIPTION = "Поиск по ИНН";

    private static final State WAIT_STATE = WAIT_INN;

    @Override
    public String getMenuComand() {
        return SEARCH_BY_INN;
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

    private List<PartialBotApiMethod> waitLogic(User user, Update update) throws ParseException {
        if (!update.hasMessage()) {
            return createErrorDefaultMessage(user);
        }
        val answer = new ArrayList<PartialBotApiMethod>();
        val message = update.getMessage().getText().replaceAll(SPACE, EMPTY);
        val innsSplitComma = Arrays.asList(message.split(","));
        val innList = splitList(innsSplitComma, NEW_LINE);
        for (String inn : innList) {
            answer.addAll(findInnWinner(user, inn));
        }
        answer.addAll(createMessageList(user, INPUT_TEXT));
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
            answer.addAll(createMessageList(user, errorText));
            return answer;
        }
        val innInfo = innInfoRepository.findById(innWinner).orElse(null);
        if (innInfo == null) {
            val errorText = "По введенным данным записей в БД не найдено: " + message;
            answer.addAll(createMessageList(user, errorText));
            return answer;
        }
        val statistic = createStatistic(user);
        statistic.setInn(innWinner);
        statisticRepository.save(statistic);
        answer.addAll(createInnInfoMessaages(user, List.of(innInfo)));
        val innCrossLinkKeysFirst = innCrossLinkRepository.findByInnCrosslinkKeyInnFirst(innWinner);
        answer.add(getInnLinked(user, innCrossLinkKeysFirst, "Найдены прямые связи, ИНН:"));

        val innCrossLinkKeysSecond = innCrossLinkRepository.findByInnCrosslinkKeyInnSecond(innWinner);
        answer.add(getInnLinked(user, innCrossLinkKeysSecond, "Найдены обратные связи, ИНН:"));
        return answer;
    }

    private PartialBotApiMethod getInnLinked(User user, List<InnCrosslink> innCrossLinks, String title) {
        if (innCrossLinks.isEmpty()) {
            return null;
        }
        val innLinkedMessage = new StringBuilder();
        innLinkedMessage.append(title).append(NEW_LINE);
        innCrossLinks.stream().forEach(
                link -> innLinkedMessage.append(
                        String.format("- %s: %d - %d%s"
                                , link.getCrossLinkType().getTitle()
                                , link.getInnCrosslinkKey().getInnFirst()
                                , link.getInnCrosslinkKey().getInnSecond()
                                , NEW_LINE))
        );
        return createMessage(user, innLinkedMessage.toString());
    }


}
