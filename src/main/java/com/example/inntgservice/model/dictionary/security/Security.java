package com.example.inntgservice.model.dictionary.security;

import com.example.inntgservice.enums.UserRole;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.menu.MenuActivity;
import lombok.*;

import java.util.List;
import java.util.Map;

import static com.example.inntgservice.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.inntgservice.constant.Constant.Command.COMMAND_START;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Security {

    private Map<UserRole, List<String>> roleAccess;

    private List<MenuActivity> mainMenu;

    public boolean checkAccess(User user, String menuComand) {
        if (menuComand.equals(COMMAND_START) || menuComand.equals(COMMAND_DEFAULT)) {
            return true;
        }
        val isRoleAccess = roleAccess.get(user.getUserRole()).contains(menuComand);
        return isRoleAccess;
    }
}
