package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.inntgservice.utils.StringUtils.prepareShield;

@Component
@Slf4j
public class MenuDefault extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_DEFAULT;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Не найдена доступная команда с именем: " + prepareShield(update.getMessage().getText()))
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
