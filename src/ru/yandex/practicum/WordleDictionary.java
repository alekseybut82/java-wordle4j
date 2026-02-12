package ru.yandex.practicum;

import ru.yandex.practicum.wordle.exception.game.*;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final Set<String> set = new HashSet<>();
    private static final Random RANDOM = new Random();
    private final int wordLength;

    public WordleDictionary(int wordLength) {
        this.wordLength = wordLength;
    }

    public WordleDictionary addAll(Collection<String> addedWord) {
        set.addAll(addedWord);
        return this;
    }

    public String getRandomWord() {
        int random = RANDOM.nextInt(set.size());
        Iterator<String> iterator = set.iterator();
        String result = "";

        for (int i = 0; i <= random; i++) {
            result = iterator.next();
        }
        return result;

    }

    public void containsWord(String word) throws WordNotFoundException {
        if (!set.contains(word)) throw new WordNotFoundException("Такого слова нет в словаре, придумайте другое");
    }

    public Set<String> getWordSet() {
        return set;
    }

    public static String normalizeWord(String word) {
        return word.strip().toLowerCase().replace('ё', 'е');
    }

    public void checkWord(String word) throws InputEmptyWordException, InvalidWordLengthException, NotOnlyRussiaLetterException {
        if (word.isEmpty()) throw new InputEmptyWordException("Вводить слово, состоящее из пробелов, нельзя.");
        else if (word.length() != wordLength) throw new InvalidWordLengthException("Длина слова дожна быть равна " + wordLength);

        for (int i = 0; i < wordLength; i++) {
            if (word.charAt(i) < 'а' || word.charAt(i) > 'я')
                throw new NotOnlyRussiaLetterException("Слово должно состоять только из русских букв = " + word.charAt(i));
        }
    }
}
