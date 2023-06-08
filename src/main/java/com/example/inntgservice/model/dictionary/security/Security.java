package com.example.inntgservice.model.dictionary.security;

import com.example.inntgservice.enums.UserRole;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.menu.MenuActivity;
import lombok.*;

import java.util.List;
import java.util.Map;

import static com.example.inntgservice.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.inntgservice.constant.Constant.Command.COMMAND_START;
import static com.example.inntgservice.constant.Constant.Security.ADMIN_BLOCK_APP;
import static com.example.inntgservice.constant.Constant.Security.ADMIN_UNBLOCK_APP;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Security {

    private boolean isBlocked;
    private Long adminChatId;
    private Map<UserRole, List<String>> roleAccess;

    private List<MenuActivity> mainMenu;

    public boolean isBlockedApp(User user, String message) {
        if (!user.getChatId().equals(adminChatId) || message == null) {
            return isBlocked;
        }
        if (message.equals(ADMIN_BLOCK_APP)) {
            isBlocked = true;
        } else if (message.equals(ADMIN_UNBLOCK_APP)) {
            isBlocked = false;
        }
        return false;
    }

    public boolean checkAccess(User user, String menuComand) {
        if (menuComand.equals(COMMAND_START) || menuComand.equals(COMMAND_DEFAULT)) {
            return true;
        }
        val isRoleAccess = roleAccess.get(user.getUserRole()).contains(menuComand);
        return isRoleAccess;
    }
}
