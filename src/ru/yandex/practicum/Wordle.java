package ru.yandex.practicum;

import ru.yandex.practicum.wordle.exception.game.GameException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final String WORDS_FILE_NAME = "words_ru.txt";
    private static final Integer MAX_ATTAMPS = 6;
    private static final Integer WORD_LENGTH = 5;
    private static final String LOG_FILE_NAME = "log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (
                FileOutputStream fos = new FileOutputStream(LOG_FILE_NAME);
                Writer charDecode = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                PrintWriter logger = new PrintWriter(charDecode, true)
        ) {
            //приглашение
            printInviteMessage();

            //читаем словарь и отбираем слова требуемой длины
            WordleDictionary wordleDictionary = WordleDictionaryLoader.loadFromFile(WORDS_FILE_NAME, WORD_LENGTH, logger);

            //создаем объект игры со словарем и запускаем её
            WordleGame game = new WordleGame(wordleDictionary, MAX_ATTAMPS, WORD_LENGTH, logger);
            game.start();

            while (!game.isGameOver()) {
                String inputWord = scanner.nextLine();
                try {
                    System.out.println(game.processInputWord(inputWord));
                } catch (GameException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (RuntimeException e) {
            System.out.println("\nРабота программы прервана. \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nНепредвиденная ошибка\n" + e.getMessage());
        }
    }

    private static void printInviteMessage() {
        System.out.println("Добро пожаловать в  игру Wordle");
        System.out.printf("Игра Wordle загадала слово из %s букв. Попробуй угадать :)\n", WORD_LENGTH);
        System.out.println("Если сложно придумать вариант, просто нажми \"Enter\", игра поможет тебе");
        System.out.printf("Обозначения: \"+\" - буква угадана, \"^\" - буква есть в слове, \"-\" - такой буквы нет слове\n", WORD_LENGTH);
        System.out.printf("Вводи свои варианты из %d букв\n", WORD_LENGTH);
    }
}
