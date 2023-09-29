package com.example.inntgservice.model.security;

import com.example.inntgservice.enums.UserRole;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.menu.MenuActivity;
import lombok.*;
import org.example.tgcommons.constant.Constant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.inntgservice.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.inntgservice.constant.Constant.Command.COMMAND_START;
import static com.example.inntgservice.constant.Constant.Security.ADMIN_BLOCK_APP;
import static com.example.inntgservice.constant.Constant.Security.ADMIN_UNBLOCK_APP;

@Component
@Getter
@AllArgsConstructor
public class Security {

    private final Map<UserRole, List<String>> roleAccess;

    private final Map<String, MenuActivity> mainMenu;

    private final Long adminChatId;

    private boolean isBlocked = false;

    public boolean isBlockedApp(User user, Message message) {
        if (message == null || !user.getChatId().equals(adminChatId)) {
            return isBlocked;
        }
        val messageText = message.getText();
        if (messageText == null) {
            return isBlocked;
        }
        if (messageText.equals(ADMIN_BLOCK_APP)) {
            isBlocked = true;
        } else if (messageText.equals(ADMIN_UNBLOCK_APP)) {
            isBlocked = false;
        }
        return false;
    }

    public MenuActivity getMenuActivity(String commandName) {
        return mainMenu.getOrDefault(commandName, null);
    }

    public boolean checkAccess(User user, String menuComand) {
        if (menuComand.equals(Constant.Command.COMMAND_START) || menuComand.equals(Constant.Command.COMMAND_DEFAULT)) {
            return true;
        }
        return roleAccess.get(user.getUserRole()).contains(menuComand);
    }
}
