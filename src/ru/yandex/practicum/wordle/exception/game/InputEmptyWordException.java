package ru.yandex.practicum.wordle.exception.game;

public class InputEmptyWordException extends GameException {
    public InputEmptyWordException(String message) {
        super(message);
    }
}
