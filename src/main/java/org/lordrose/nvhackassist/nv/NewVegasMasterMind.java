package org.lordrose.nvhackassist.nv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewVegasMasterMind {

    final String[] words;
    final private int[][] decisionMatrix;

    private NewVegasMasterMind(String[] words, int[][] matrix) {
        this.words = words;
        this.decisionMatrix = matrix;
    }

    public static NewVegasMasterMind from(String... words) {
        String[] validated = ensureSameLengthWord(Objects.requireNonNull(words));

        return new NewVegasMasterMind(validated, initDecisionMatrix(validated));
    }

    private static NewVegasMasterMind from(List<String> words) {
        String[] wordArray = words.toArray(String[]::new);
        return new NewVegasMasterMind(wordArray, initDecisionMatrix(wordArray));
    }

    private static int[][] initDecisionMatrix(String[] words) {
        int[][] decisionMatrix = new int[words.length][words.length];

        for (int assumeCorrectWordIndex = 0; assumeCorrectWordIndex < words.length; assumeCorrectWordIndex++) {
            for (int i = 0; i < words.length; i++) {
                if (i == assumeCorrectWordIndex) {
                    decisionMatrix[i][i] = -1;
                } else {
                    String assumeCorrectWord = words[assumeCorrectWordIndex];
                    int matchedNum = 0;

                    for (int j = 0; j < assumeCorrectWord.length(); j++) {
                        if (assumeCorrectWord.charAt(j) == words[i].charAt(j)) {
                            matchedNum++;
                        }
                    }
                    decisionMatrix[assumeCorrectWordIndex][i] = matchedNum;
                }
            }
        }

        return decisionMatrix;
    }

    private static String[] ensureSameLengthWord(String[] words) {
        int base = words[0].length();
        for (int i = 0, wordsLength = words.length; i < wordsLength; i++) {
            String word = words[i];
            if (base != word.length()) {
                throw new RuntimeException("Words are not the same length");
            }
            words[i] = word.toUpperCase();
        }
        return words;
    }

    public List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            options.add(new Option(i,
                    words[i],
                    Arrays.stream(decisionMatrix[i])
                            .distinct()
                            .filter(matrixVal -> matrixVal != -1)
                            .sorted().toArray()));
        }
        return options;
    }

    public NewVegasMasterMind evaluate(int index, int correctResult) {
        int[] rows = decisionMatrix[index];
        List<Integer> correctResultIndexes = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == correctResult) {
                correctResultIndexes.add(i);
            }
        }

        List<String> filteredWords = new ArrayList<>();
        for (int correctResultIndex : correctResultIndexes) {
            for (int i = 0; i < decisionMatrix.length; i++) {
                if (i != index && decisionMatrix[i][correctResultIndex] == -1) {
                    filteredWords.add(words[i]);
                }
            }
        }

        return NewVegasMasterMind.from(filteredWords);
    }

    public NewVegasMasterMind dud(int index) {
        List<String> filteredWords = IntStream.range(0, words.length)
                .filter(intIndex -> intIndex != index)
                .mapToObj(intIndex -> words[intIndex])
                .collect(Collectors.toList());

        return NewVegasMasterMind.from(filteredWords);
    }
}
