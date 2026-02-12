package ru.yandex.practicum;

import ru.yandex.practicum.wordle.exception.technical.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public static WordleDictionary loadFromFile(String fileName, int dictionaryWordLength, PrintWriter logger)
            throws FileNotExistsException, ItIsNotFileException, NoAccesToFileException, NoWordsAfterFilterException {

        List<String> wordsList = new ArrayList<>();
        Path path = Paths.get(fileName).toAbsolutePath();
        logger.printf("Подключаемся к файлу %s\n", path);

        try (
                InputStream fis = new FileInputStream(fileName);
                Reader charDecode = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader wordLineBuffer = new BufferedReader(charDecode);
        ) {
            String readWord;
            logger.printf("Отбираем только подходящие по длине слова и нормализуем их\n");
            while ((readWord = wordLineBuffer.readLine()) != null) {
                if (readWord.length() == dictionaryWordLength) {
                    wordsList.add(WordleDictionary.normalizeWord(readWord));
                }
            }

            if (wordsList.isEmpty()) throw new NoWordsAfterFilterException(path);

            logger.printf("Сформирован словарь из слов длинной %d букв. Количество записей в словаре %d\n", dictionaryWordLength, wordsList.size());

            return new WordleDictionary(dictionaryWordLength).addAll(wordsList);

        } catch (FileNotFoundException exp) {
            if (!Files.exists(path)) {
                logger.printf("Файл не существует: %s\n", path);
                throw new FileNotExistsException(path);
            } else if (!Files.isRegularFile(path)) {
                logger.printf("Это не файл (возможно, директория): %s\n", path);
                throw new ItIsNotFileException(path);
            } else if (!Files.isReadable(path)) {
                logger.printf("Нет доступа к файлу: %s\n", path);
                throw new NoAccesToFileException(path);
            } else {
                logger.printf("Неизвестная ошибка при доступе к файлу: %s\n", exp.getMessage());
                throw new DictionaryFileLoadException(path);
            }
        } catch (NoWordsAfterFilterException exp) {
            logger.printf("В файле нет слов нужной длины (%d): %s\n", dictionaryWordLength, path);
            throw exp;
        } catch (Exception exp) {
            logger.printf("Непредвиденная ошибка при чтении файла %s: %s\n", path, exp.getMessage());
            throw new DictionaryFileLoadException(exp.getMessage());
        }
       // return null;
    }
}