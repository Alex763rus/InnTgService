package com.example.inntgservice.enums;

public enum UserRole {

    BLOCKED("Заблокирован"),
    SIMPLE_USER("Пользователь"),
    ADMIN("Администратор");

    private String title;

    UserRole(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
