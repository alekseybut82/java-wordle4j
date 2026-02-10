package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle.exception.game.WordNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;
    private StringWriter stringWriter;
    private PrintWriter logger;

    @BeforeEach
    void setUp() throws Exception {
        logger = new PrintWriter("D:\\JavaTraining\\java-wordle4j\\test_log.txt");//(System.out);
        dictionary = new WordleDictionary(5);
        dictionary.addAll(List.of("тесто", "ружье", "слово", "вожжа", "ножка"));

        game = new WordleGame(dictionary, 6, 5, logger);
        game.setHiddenWord("ножка"); // фиксируем слово
        game.init();
    }

    @Test
    void testProcessInputWordCorrectGuess() throws Exception {
        String result = game.processInputWord("ножка");
        assertTrue(game.isPlayerWin());
        assertTrue(game.isGameOver());
    }

    @Test
    void testProcessInputWordIncorrectGuess() throws Exception {
        String result = game.processInputWord("тесто");
        assertFalse(game.isGameOver());
        assertTrue(result.contains("----^"));

        result = game.processInputWord("вожжа");
        assertFalse(game.isGameOver());
        assertTrue(result.contains("-++^+"));

    }

    @Test
    void testCreateHint() throws Exception {
        String hintWord = game.hint();
        assertNotNull(hintWord);
        assertTrue(hintWord.length() == 5);

    }

    @Test
    void testProcessInputWordInvalidWord() {
        assertThrows(WordNotFoundException.class, () -> game.processInputWord("несущ"));
    }

    @Test
    void testHintFiltersPreviousAnswers() throws Exception {
        game.processInputWord("ружье");  // добавляем в answers
        while (!game.isGameOver()) {
            String hintWord = game.hint();
            game.processInputWord(hintWord);
            assertTrue(game.getAnswers().contains(hintWord));  // подсказка не должна повторять ответ
        }
    }

    @Test
    void testGameOverAfterMaxFailedAttempts() throws Exception {
        for (int i = 0; i < 6; i++) {
            game.processInputWord("вожжа");  // неверный ответ
        }
        assertTrue(game.isGameOver());
        assertFalse(game.isPlayerWin());
    }

    @Test
    void testGameWinUsedOnlyHint() throws Exception {
        int i = 1;
        while (!game.isGameOver() ) {
            game.processInputWord("");
            i++;
        }
        assertTrue(game.isGameOver());
        assertTrue(game.isPlayerWin());
    }


}
