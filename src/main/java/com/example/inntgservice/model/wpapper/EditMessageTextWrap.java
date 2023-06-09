package com.example.inntgservice.model.wpapper;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static com.example.inntgservice.constant.Constant.PARSE_MODE;

@Getter
@SuperBuilder(setterPrefix = "set", builderMethodName = "init", toBuilder = true)
public class EditMessageTextWrap {

    private Integer messageId;
    private String chatIdString;
    private Long chatIdLong;
    private String text;

    public EditMessageText createMessage() {
        val editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        val chatId = chatIdString == null ? String.valueOf(chatIdLong) : chatIdString;
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
//        editMessageText.setParseMode(PARSE_MODE);
        return editMessageText;
    }
}
