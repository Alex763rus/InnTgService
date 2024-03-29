package com.example.inntgservice.api.bot;

import com.example.inntgservice.config.BotConfig;
import com.example.inntgservice.enums.Emoji;
import com.example.inntgservice.service.TelegramBot;
import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.example.tgcommons.constant.Constant.Telegramm.PARSE_MODE;

@Service
@Slf4j
public class DistributionService {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private BotConfig botConfig;

    @PostConstruct
    public void init() {
        StringBuilder message = new StringBuilder(EmojiParser.parseToUnicode(Emoji.ROBOT_FACE.getCode())).append(" telegramm bot was started!\n");
        message.append("Version: ").append(botConfig.getBotVersion());
        sendMessage(botConfig.getAdminChatId(), message.toString());
    }

    @PreDestroy
    public void squeezyExit() {
        StringBuilder message = new StringBuilder(EmojiParser.parseToUnicode(Emoji.ROBOT_FACE.getCode())).append(" telegramm bot will be *STOPPED*!\n");
        message.append("Version: ").append(botConfig.getBotVersion()).append("\n");
        message.append("*Buy!*");
        sendMessage(botConfig.getAdminChatId(), message.toString());
    }

    public void sendTgMessageToAdmin(String message) {
        sendMessage(botConfig.getAdminChatId().toString(), message);
    }

    private void sendMessage(String chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.setParseMode(PARSE_MODE);
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
