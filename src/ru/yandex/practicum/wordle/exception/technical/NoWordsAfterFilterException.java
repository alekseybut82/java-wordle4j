package ru.yandex.practicum.wordle.exception.technical;

import java.nio.file.Path;

public class NoWordsAfterFilterException extends DictionaryFileLoadException {
    public NoWordsAfterFilterException(Path absolutePath) {
        super(String.format("В файле %s отсутствуют слова для загрузки в словарь Wordle\n",absolutePath));
    }
}

