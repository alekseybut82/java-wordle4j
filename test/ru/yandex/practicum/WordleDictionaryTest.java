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
    public void setUp() {
        dictionary = new WordleDictionary(5);
        dictionary.addAll(List.of("тесто", "ружье", "слово", "вожжа", "ножка"));
    }

    @Test
    public void testNormalizeWord() {
        assertEquals("ружье", WordleDictionary.normalizeWord("Ружьё"));
        assertEquals("ножка", WordleDictionary.normalizeWord("НОжКА"));
        assertEquals("чайка", WordleDictionary.normalizeWord("  Чайка  "));
    }

    @Test
    public void testCheckWordForValid() throws Exception {
        dictionary.checkWord("кабан");  // должно пройти
    }

    @Test
    public void testCheckWordEmpty() {
        assertThrows(InputEmptyWordException.class, () -> dictionary.checkWord(""));
    }

    @Test
    public void testCheckWordWrongLength() {
        assertThrows(InvalidWordLengthException.class, () -> dictionary.checkWord("длинное"));
    }

    @Test
    public void testCheckWordNonCyrillic() {
        assertThrows(NotOnlyRussiaLetterException.class, () -> dictionary.checkWord("hello"));
    }

    @Test
    public void testContainsWordFound() throws Exception {
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("привет"));
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("привет"));
    }

    @Test
    public void testContainsWordNotFoundInDictionary() {
        assertThrows(WordNotFoundException.class, () -> dictionary.containsWord("яблоко"));
    }

    @Test
    public void testGetRandomWordNotNull() {
        assertNotNull(dictionary.getRandomWord());
    }
}