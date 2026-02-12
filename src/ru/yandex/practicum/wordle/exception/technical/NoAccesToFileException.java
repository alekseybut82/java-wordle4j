package ru.yandex.practicum.wordle.exception.technical;

import java.nio.file.Path;

public class NoAccesToFileException extends DictionaryFileLoadException {
    public NoAccesToFileException(Path absolutePath) {
        super("Нет прав на работу с файлом" + absolutePath);
    }
}
