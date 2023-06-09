package com.example.inntgservice.config;

import com.example.inntgservice.enums.UserRole;
import com.example.inntgservice.model.security.Security;
import com.example.inntgservice.service.excel.InnUploaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;

import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.enums.UserRole.*;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    private final String adminChatId = "799008767";

    @Value("${bot.version}")
    String botVersion;

    @Value("${bot.username}")
    String botUserName;

    @Value("${bot.token}")
    String botToken;


    @Autowired
    ObjectMapper objectMapper;

    @Value("${input.file.path}")
    String inputFilePath;

    @Autowired
    private InnUploaderService innUploaderService;

    @Bean
    public Security security() {
        val roleSecurity = new Security();
        roleSecurity.setBlocked(false);
        roleSecurity.setAdminChatId(Long.parseLong(getAdminChatId()));

        // Настройка команд по ролям:
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(SIMPLE_USER, List.of(COMMAND_DEFAULT, COMMAND_START, SEARCH_BY_INN, SEARCH_BY_INN_HEAD, SEARCH_BY_MAIL, SEARCH_BY_PHONE, SEARCH_BY_WEBSITE));
        roleAccess.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, SEARCH_BY_INN, SEARCH_BY_INN_HEAD, SEARCH_BY_MAIL, SEARCH_BY_PHONE, SEARCH_BY_WEBSITE
                , UPLOAD_INN_FILE, CREATE_STATISTIC));
        roleSecurity.setRoleAccess(roleAccess);
        return roleSecurity;
    }

}
