package ru.ilysenko.tinka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Ticker {
    APPLE("AAPL"),
    SBERBANK("SBER");

    private String value;
}
