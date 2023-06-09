package com.example.inntgservice.model.menu;

import com.example.inntgservice.config.BotConfig;
import com.example.inntgservice.model.jpa.InnInfoRepository;
import com.example.inntgservice.model.jpa.StatisticRepository;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import com.example.inntgservice.service.database.UserService;
import com.example.inntgservice.service.excel.ExcelService;
import com.example.inntgservice.service.excel.FileUploadService;
import com.example.inntgservice.service.excel.ExcelGenerateService;
import com.example.inntgservice.service.menu.ButtonService;
import com.example.inntgservice.service.menu.StateService;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@MappedSuperclass
public abstract class Menu implements MenuActivity {

    @Autowired
    protected BotConfig botConfig;

    @Autowired
    protected StateService stateService;

    @Autowired
    protected FileUploadService fileUploadService;

    @Autowired
    protected ExcelGenerateService excelGenerateService;

    @Autowired
    protected ExcelService excelService;

    @Autowired
    protected ButtonService buttonService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected InnInfoRepository innInfoRepository;

    @Autowired
    protected StatisticRepository statisticRepository;

    private static final String DEFAULT_TEXT_ERROR = "Ошибка! Команда не найдена";

    protected List<PartialBotApiMethod> errorMessageDefault(Update update) {
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(DEFAULT_TEXT_ERROR)
                .build().createSendMessage());
    }

    protected List<PartialBotApiMethod> errorMessage(Update update, String message) {
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(message)
                .build().createSendMessage());
    }

    protected PartialBotApiMethod createAdminMessage(String message) {
        return SendMessageWrap.init()
                .setChatIdString(botConfig.getAdminChatId())
                .setText(message)
                .build().createSendMessage();
    }

    protected boolean checkLong(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
