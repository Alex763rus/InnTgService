package com.example.inntgservice.model.menu;

import com.example.inntgservice.enums.State;
import com.example.inntgservice.model.jpa.Statistic;
import com.example.inntgservice.model.jpa.StatisticRepository;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendDocumentWrap;
import com.example.inntgservice.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.CREATE_STATISTIC;
import static com.example.inntgservice.enums.State.FREE;
import static com.example.inntgservice.utils.DateConverter.convertDateFormat;
import static com.example.inntgservice.utils.StringUtils.prepareShield;

@Component
@Slf4j
public class MenuCreateStatistic extends Menu {

    @Autowired
    private StatisticRepository statisticRepository;

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
            return errorMessage(update, "Статистика отсутствует");
        }
        excelData.add(List.of("ID:", "Дата:", "Пользователь:", "Логин:", "ИНН:"));
        for (val statistic : allStatistic) {
            excelData.add(List.of(
                    String.valueOf(statistic.getStatisticId())
                    , String.valueOf(statistic.getRegisteredAt())
                    , prepareShield(statistic.getUser().getFirstName())
                    , prepareShield(statistic.getUser().getUserName())
                    , String.valueOf(statistic.getInn())
            ));
        }
        val excelDocument = excelService.createExcelDocument("Статистика", excelData);
        stateService.setState(user, FREE);
        return List.of(SendDocumentWrap.init()
                .setChatIdLong(user.getChatId())
                .setDocument(excelDocument)
                .build().createMessage());
    }
}
