package com.example.inntgservice.model.menu;

import com.example.inntgservice.config.BotConfig;
import com.example.inntgservice.model.jpa.InnCrossLinkRepository;
import com.example.inntgservice.model.jpa.InnInfoRepository;
import com.example.inntgservice.model.jpa.StatisticRepository;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.service.database.UserService;
import com.example.inntgservice.service.excel.ExcelGenerateService;
import com.example.inntgservice.service.excel.ExcelService;
import com.example.inntgservice.service.excel.FileUploadService;
import com.example.inntgservice.service.menu.ButtonService;
import com.example.inntgservice.service.menu.StateService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.exception.InputCallbackException;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.DeleteMessageWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static org.example.tgcommons.constant.Constant.MessageConstants.DEFAULT_TEXT_ERROR;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

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
    protected InnCrossLinkRepository innCrossLinkRepository;

    @Autowired
    protected StatisticRepository statisticRepository;

    protected String getInputCallback(Update update) {
        if (!update.hasCallbackQuery()) {
            throw new InputCallbackException();
        }
        return update.getCallbackQuery().getData();
    }
    protected boolean checkLong(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    protected List<PartialBotApiMethod> createErrorDefaultMessage(User user) {
        return createMessageList(user, DEFAULT_TEXT_ERROR);
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message) {
        return List.of(this.createMessage(user, message));
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, ButtonsDescription buttonsDescription) {
        return List.of(this.createMessage(user, message, buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .build().createMessage();
    }

    protected PartialBotApiMethod createDeleteMessage(Update update) {
        if (!update.hasCallbackQuery()) {
            return null;
        }
        val message = update.getCallbackQuery().getMessage();
        return DeleteMessageWrap.init()
                .setChatIdLong(message.getChatId())
                .setMessageId(message.getMessageId())
                .build().createMessage();
    }

    protected PartialBotApiMethod createMessage(User user, String message, ButtonsDescription buttonsDescription) {
        return createMessage(user, message, createVerticalColumnMenu(buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .setInlineKeyboardMarkup(inlineKeyboardMarkup)
                .build().createMessage();
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return List.of(this.createMessage(user, message, inlineKeyboardMarkup));
    }

}
