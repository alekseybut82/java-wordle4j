package ru.yandex.practicum.wordle.exception.game;

public class NotOnlyRussiaLetterException extends GameException {
    public NotOnlyRussiaLetterException(String s) {
        super(s);
    }
}
