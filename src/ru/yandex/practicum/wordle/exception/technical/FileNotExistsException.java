package ru.yandex.practicum.wordle.exception.technical;

import java.nio.file.Path;

public class FileNotExistsException extends DictionaryFileLoadException {
    public FileNotExistsException(Path absolutePath) {
        super("Файл " + absolutePath + " не существует.");
    }
}
