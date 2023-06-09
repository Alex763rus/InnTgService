package com.example.inntgservice.service.menu;

import com.example.inntgservice.model.menu.search.*;
import com.example.inntgservice.model.security.Security;
import com.example.inntgservice.model.menu.*;
import com.example.inntgservice.model.wpapper.EditMessageTextWrap;
import com.example.inntgservice.service.database.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.COMMAND_START;
import static com.example.inntgservice.enums.State.FREE;

@Slf4j
@Service
public class MenuService {

    private boolean isBlocked;

    @Autowired
    private MenuDefault menuActivityDefault;

    @Autowired
    private StateService stateService;

    @Autowired
    private UserService userService;

    @Autowired
    private Security security;

    @Autowired
    private MenuUploadInnFile menuUploadInnFile;

    @Autowired
    private MenuCreateStatistic menuCreateStatistic;

    @Autowired
    private MenuSearchInnWinner menuSearchInnWinnerByInn;

    @Autowired
    private MenuSearchInnHead menuSearchByInnHead;

    @Autowired
    private MenuSearchByMail menuSearchByMail;

    @Autowired
    private MenuSearchByPhone menuSearchByPhone;

    @Autowired
    private MenuSearchByWebsite menuSearchByWebsite;

    @Autowired
    private MenuStart menuStart;

    @PostConstruct
    public void init() {
        // Список всех возможных обработчиков меню:
        security.setMainMenu(List.of(menuStart, menuUploadInnFile, menuCreateStatistic, menuSearchInnWinnerByInn, menuSearchByInnHead
                , menuSearchByMail, menuSearchByPhone, menuSearchByWebsite));
        isBlocked = false;
    }


    public List<PartialBotApiMethod> messageProcess(Update update) {
        val user = userService.getUser(update);
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            val message = update.getMessage();
            if (security.isBlockedApp(user, message)) {
                return new ArrayList<>();
            }
            if (message != null && message.getText() != null) {
                for (val menu : security.getMainMenu()) {
                    if (menu.getMenuComand().equals(message.getText())) {
                        if (security.checkAccess(user, menu.getMenuComand())) {
                            menuActivity = menu;
                        } else {
                            menuActivity = menuActivityDefault;
                        }
                    }
                }
            }
        }
        if (menuActivity != null) {
            stateService.setMenu(user, menuActivity);
        } else {
            menuActivity = stateService.getMenu(user);
            if (menuActivity == null) {
                log.warn("Не найдена команда с именем: " + update.getMessage().getText());
                menuActivity = menuActivityDefault;
            }
        }
        val answer = new ArrayList<PartialBotApiMethod>();
        if (update.hasCallbackQuery()) {
            val message = update.getCallbackQuery().getMessage();
            val menuName = update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().stream()
                    .filter(e -> e.get(0).getCallbackData().equals(update.getCallbackQuery().getData()))
                    .findFirst().get().get(0).getText();
            answer.add(EditMessageTextWrap.init()
                    .setChatIdLong(message.getChatId())
                    .setMessageId(message.getMessageId())
                    .setText("Выбрано меню: " + menuName)
                    .build().createMessage());
        }
        answer.addAll(menuActivity.menuRun(user, update));
        if (stateService.getState(user) == FREE && !menuActivity.getMenuComand().equals(menuStart.getMenuComand())) {
            answer.addAll(menuStart.menuRun(user, update));
        }
        return answer;
    }

    public List<BotCommand> getMainMenuComands() {
        val listofCommands = new ArrayList<BotCommand>();
        security.getMainMenu().stream()
                .filter(e -> e.getMenuComand().equals(COMMAND_START))
                .forEach(e -> listofCommands.add(new BotCommand(e.getMenuComand(), e.getDescription())));
        return listofCommands;
    }

}