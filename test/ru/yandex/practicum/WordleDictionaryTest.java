package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle.exception.game.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(5);
        dictionary.addAll(List.of("тесто", "ружье", "слово", "вожжа", "ножка"));
    }

    @Test
    void testNormalizeWord() {
        assertEquals("ружье", WordleDictionary.normalizeWord("Ружьё"));
        assertEquals("ножка", WordleDictionary.normalizeWord("НОжКА"));
        assertEquals("чайка", WordleDictionary.normalizeWord("  Чайка  "));
    }

    @Test
    void testCheckWordForValid() throws Exception {
        dictionary.checkWord("кабан");  // должно пройти
    }

    @Test
    void testCheckWordEmpty() {
        assertThrows(InputEmptyWordException.class, () -> dictionary.checkWord(""));
    }

    @Test
    void testCheckWordWrongLength() {
        assertThrows(InvalidWordLengthException.class, () -> dictionary.checkWord("длинное"));
    }

    @Test
    void testCheckWordNonCyrillic() {
        assertThrows(NotOnlyRussiaLetterException.class, () -> dictionary.checkWord("hello"));
    }

    @Test
    void testContainsWordFound() throws Exception {
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("привет"));
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("привет"));
    }

    @Test
    void testContainsWordNotFoundInDictionary() {
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("яблоко"));
    }

    @Test
    void testGetRandomWordNotNull() {
        assertNotNull(dictionary.getRandomWord());
    }
}