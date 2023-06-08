package com.example.inntgservice.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {
    @NoArgsConstructor(access = PRIVATE)
    public final class App {

    }

    @NoArgsConstructor(access = PRIVATE)
    public final class Command {
        public static final String COMMAND_DEFAULT = "/default";
        public static final String COMMAND_START = "/start";
        public static final String UPLOAD_INN_FILE = "/upload_inn_file";
        public static final String SEARCH_BY_INN = "/search_by_inn";
        public static final String CREATE_STATISTIC = "/create_statistic";

    }

    @NoArgsConstructor(access = PRIVATE)
    public final class Security {
        public static final String ADMIN_BLOCK_APP = "block";
        public static final String ADMIN_UNBLOCK_APP = "unblock";
    }

    public static String APP_NAME = "InnTgService";
    public static String PARSE_MODE = "Markdown";

    public static final String SHIELD = "\\";

    public static final String STAR = "*";

    public static String NEW_LINE = "\n";
    public static String SPACE = " ";
    public static String EMPTY = "";
    public static String SHEET_RESULT_NAME = "ИМПОРТ";


}
