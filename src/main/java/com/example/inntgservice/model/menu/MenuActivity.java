package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.User;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface MenuActivity {

    public String getMenuComand();

    public String getDescription();

    public List<PartialBotApiMethod> menuRun(User user, Update update);

}
