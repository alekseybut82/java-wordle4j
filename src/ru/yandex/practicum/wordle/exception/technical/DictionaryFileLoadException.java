package ru.yandex.practicum.wordle.exception.technical;

import java.nio.file.Path;

public class DictionaryFileLoadException extends RuntimeException {
    public DictionaryFileLoadException(String message) {
        super(message);
    }

    public DictionaryFileLoadException(Path absolutePath) {
        super("Неклассифицированная ошибка при загрузке файла " + absolutePath);
    }
}









