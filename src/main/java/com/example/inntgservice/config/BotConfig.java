package com.example.inntgservice.config;

import com.example.inntgservice.enums.UserRole;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.inntgservice.constant.Constant.Command.*;
import static com.example.inntgservice.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_DEFAULT;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;

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

    @Value("${input.file.path}")
    String inputFilePath;

    @Value("${link.file.path.download.need}")
    String linkFilePathDownloadNeed;

    @Value("${link.file.path.download.ok}")
    String linkFilePathDownloadOk;

    @Value("${link.file.path.download.error}")
    String linkFilePathDownloadError;

    @Bean
    public Map<UserRole, List<String>> roleAccess() {
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(SIMPLE_USER, List.of(COMMAND_DEFAULT, COMMAND_START, SEARCH_BY_INN, SEARCH_BY_INN_HEAD, SEARCH_BY_MAIL, SEARCH_BY_PHONE, SEARCH_BY_WEBSITE));
        roleAccess.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, SEARCH_BY_INN, SEARCH_BY_INN_HEAD, SEARCH_BY_MAIL, SEARCH_BY_PHONE, SEARCH_BY_WEBSITE
                , UPLOAD_INN_FILE, CREATE_STATISTIC, UPLOAD_LINK_FILE));
        return roleAccess;
    }

    @Bean
    public Long adminChatId() {
        return Long.parseLong(adminChatId);
    }

    @Bean
    public boolean isBlocked() {
        return false;
    }
}
