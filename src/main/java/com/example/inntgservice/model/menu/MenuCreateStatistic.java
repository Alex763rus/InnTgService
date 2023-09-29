package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.CREATE_STATISTIC;
import static com.example.inntgservice.utils.StringUtils.prepareLong;
import static com.example.inntgservice.utils.StringUtils.prepareShield;

@Component(CREATE_STATISTIC)
@Slf4j
public class MenuCreateStatistic extends Menu {

    @Override
    public String getMenuComand() {
        return CREATE_STATISTIC;
    }

    @Override
    public String getDescription() {
        return "Сформировать статистику";
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        val excelData = new ArrayList<List<String>>();
        val allStatistic = statisticRepository.findAll();
        if (allStatistic.size() == 0) {
            return createMessageList(user, "Статистика отсутствует");
        }
        excelData.add(List.of("ID:", "Дата:", "Пользователь:", "Логин:", "ИНН:", "ИНН Руководителя", "Почта", "Телефон", "Сайт"));
        for (val statistic : allStatistic) {
            excelData.add(List.of(
                    String.valueOf(statistic.getStatisticId())
                    , String.valueOf(statistic.getRegisteredAt())
                    , prepareShield(statistic.getUser().getFirstName())
                    , prepareShield(statistic.getUser().getUserName())
                    , prepareLong(statistic.getInn())
                    , prepareLong(statistic.getInnHead())
                    , prepareShield(statistic.getMail())
                    , prepareShield(statistic.getPhone())
                    , prepareShield(statistic.getWebsite())
            ));
        }
        val excelDocument = excelService.createExcelDocument("Статистика", excelData);
        stateService.refreshUser(user);
        return List.of(SendDocumentWrap.init()
                .setChatIdLong(user.getChatId())
                .setDocument(excelDocument)
                .build().createMessage());
    }
}
