package ru.yandex.practicum;

import ru.yandex.practicum.wordle.exception.game.*;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    //инициализирующие переменные игры
    private WordleDictionary dictionary;
    private final int maxAttempts;
    private final int wordLength;

    //логгер
    private final PrintWriter logger;

      //переменные игры
    private String hiddenWord;
    private int currentStep = 0;
    private boolean gameOver = false;
    private boolean playerWin = false;
    private Set<Character> hiddenWordCharSet = new HashSet<>();
    private Set<String> candidates;
    private Set<Integer> guessLetterPosition = new HashSet<>();
    private Set<Character> lettersNotContainedWord = new HashSet<>();
    private Set<Character> lettersContainedWord = new HashSet<>();
    private Set<String> answers = new HashSet<>(); //answer;
    private StringBuilder visualResult = new StringBuilder();
    private StringBuilder stringResult = new StringBuilder();

    public WordleGame(WordleDictionary dictionary, int maxAttempts, int wordLength, PrintWriter logger) {
        this.dictionary = dictionary;
        this.maxAttempts = maxAttempts;
        this.wordLength = wordLength;
        this.logger = logger;

    }

    public void chooseWordToHidden() {
        hiddenWord = dictionary.getRandomWord();
        logger.printf("Загадано слово: %s\n", hiddenWord);
    }

    public void init() {
        //алфавит загаданного слова
        for (char e: hiddenWord.toCharArray()) {
            hiddenWordCharSet.add(e);
        }

        //визуальное представление результата
        visualResult.setLength(wordLength);

        //слово с открытыми угаданными буквами
        stringResult.setLength(wordLength);
    }

    public void start() {
        chooseWordToHidden();
        init();
    }

    public String processInputWord(String inputWord)
            throws InputEmptyWordException, InvalidWordLengthException, NotOnlyRussiaLetterException, WordNotFoundException {
        String hintWord = "";
        if (inputWord.isEmpty()) {
            inputWord = hint();
            hintWord = inputWord + "\n";
        } else {
            inputWord = WordleDictionary.normalizeWord(inputWord);
            dictionary.checkWord(inputWord);
            dictionary.containsWord(inputWord);
        }



        answers.add(inputWord);
        currentStep++;
        logger.printf("%d попытка.", currentStep);

        if (matchAnalyzedWord(inputWord)) {
            gameOver = true;
            playerWin = true;
            return String.format("%sПоздравляем! Вы угадали слово!", hintWord);
        } else if (currentStep == maxAttempts) {
            gameOver = true;
            playerWin = false;
            return String.format("%s%s\nПопробуйте еще раз! Вы использователи все попытки", hintWord, visualResult.toString());
        } else return String.format("%s%s\nПродолжай и все получится!", hintWord, visualResult.toString());

    }

    public String hint() {
        if (currentStep != 0) {

            logger.println("Генерируем подсказку по условию: ");
            logger.printf("индексы угаданных букв = %s, текущее слово = %s, букв нет в слове = %s, буквы есть в слове, но индекс не угадан = %s, предыдущие ответы = %s \n"
                    , guessLetterPosition.toString(), stringResult,lettersNotContainedWord, lettersContainedWord, answers);

            if (candidates == null) candidates = new HashSet<>(dictionary.getWordSet());
            Iterator<String> iterator = candidates.iterator();
            String word;
            while (iterator.hasNext()) {
                word = iterator.next();
                boolean skipWord = false;

                if (answers.contains(word)) continue;

                for (int i = 0; i < wordLength; i++) {
                    char checkLetter = word.charAt(i);

                    if (guessLetterPosition.contains(i)) skipWord = checkLetter != stringResult.charAt(i);
                    else skipWord = lettersNotContainedWord.contains(checkLetter);

                    if (skipWord) break;
                }
                if (!skipWord && hasLettersAlreadyContain(word)) {
                    logger.printf("Подобрано слово: \"%s\"\n", word);
                    return word;
                }
                iterator.remove();
            }
        }
        logger.println("Генерируем подсказку произвольно выбирая слово из словаря");
        return dictionary.getRandomWord();
    }

    private boolean hasLettersAlreadyContain(String word) {
        for (Character ch: lettersContainedWord) {
            if (word.indexOf(ch) == -1) {  return false;}
        }
        return true;
    }

    public boolean matchAnalyzedWord(String checkWord) {
        logger.printf("Выполнение анализа слова \"%s\", начальные условия:\n", checkWord);
        logger.printf("индексы угаданных букв = %s, буквы в слове на своих местах = %s, букв нет в слове = %s, буквы есть в слове (индекс не определен) = %s, предыдущие ответы = %s \n"
                , guessLetterPosition.toString(), stringResult,lettersNotContainedWord, lettersContainedWord, answers);
        logger.println("Результат:");
        if (!checkWord.equals(hiddenWord)) {
            for (int i = 0; i < wordLength; i++) {
                char checkLetter = checkWord.charAt(i);

                if (!hiddenWordCharSet.contains(checkLetter)) {
                    visualResult.setCharAt(i, '-');
                    lettersNotContainedWord.add(checkLetter);
                } else if (hiddenWord.charAt(i) == checkLetter) {
                    visualResult.setCharAt(i, '+');
                    stringResult.setCharAt(i, checkLetter);
                    guessLetterPosition.add(i);
                } else {
                    visualResult.setCharAt(i, '^');
                    lettersContainedWord.add(checkLetter);
                }
            }
            logger.printf("индексы угаданных букв = %s, буквы в слове на своих местах = %s, букв нет в слове = %s, буквы есть в слове (индекс не определен)= %s\n"
                    , guessLetterPosition.toString(), stringResult,lettersNotContainedWord, lettersContainedWord);
            logger.println(visualResult);
            return false;
        } else {
            logger.printf("Слово угадано");
            return true;
        }
    }

    public boolean isPlayerWin() {
        return playerWin;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setHiddenWord(String hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public Set<String> getAnswers() {
        return answers;
    }

}


