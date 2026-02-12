package ru.yandex.practicum.wordle.exception.game;

public class InvalidWordLengthException  extends GameException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}