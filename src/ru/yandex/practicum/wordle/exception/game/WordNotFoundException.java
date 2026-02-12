package ru.yandex.practicum.wordle.exception.game;

public class WordNotFoundException extends GameException {
    public WordNotFoundException(String s) {
        super(s);
    }
}
