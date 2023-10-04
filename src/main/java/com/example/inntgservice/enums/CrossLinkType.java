package com.example.inntgservice.enums;

public enum CrossLinkType {

    LINK_HIGH("Высокая"),
    LINK_MEDIUM("Средняя"),
    LINK_LOW("Низкая");

    private String title;

    CrossLinkType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
