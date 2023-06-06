package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.enums.State.WAIT_INN;

@Component
@Slf4j
public class MenuStart extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        String messageText = "";
        switch (user.getUserRole()) {
            case BLOCKED:
                messageText = "Доступ запрещен";
            case SIMPLE_USER:
                messageText = "Введите ИНН:";
                stateService.setState(user, WAIT_INN);
                break;
            case ADMIN:
//                messageText = getAdminMenuText(user);
                break;

        }
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(user.getChatId())
                        .setText(EmojiParser.parseToUnicode(messageText))
                        .build().createSendMessage());
    }


    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
