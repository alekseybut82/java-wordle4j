package ru.yandex.practicum.wordle.exception.technical;

import java.nio.file.Path;

public class ItIsNotFileException extends DictionaryFileLoadException {
    public ItIsNotFileException(Path absolutePath) {
        super(absolutePath + " не является файлом");
    }
}
