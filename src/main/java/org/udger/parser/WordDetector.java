package org.udger.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordDetector implements Serializable {

    private static final long serialVersionUID = -2123898245391386812L;

    private static class WordInfo {
        int id;
        String word;

        public WordInfo(int id, String word) {
            this.id = id;
            this.word = word;
        }
    }

    private static final int ARRAY_DIMENSION = 'z' - 'a';
    private static final int ARRAY_SIZE = (ARRAY_DIMENSION + 1) * (ARRAY_DIMENSION + 1);

    private List<WordInfo> wordArray[];
    private int minWordSize = Integer.MAX_VALUE;

    public WordDetector() {
        wordArray = new List[ARRAY_SIZE];
    }

    public void addWord(int id, String word) {

        if (word.length() < minWordSize) {
            minWordSize = word.length();
        }

        String s = word.toLowerCase();
        int index = (s.charAt(0) - 'a') * ARRAY_DIMENSION + s.charAt(1) - 'a';
        if (index >= 0 && index < ARRAY_SIZE) {
            List<WordInfo> wList = wordArray[index];
            if (wList == null) {
                wList = new ArrayList<>();
                wordArray[index] = wList;
            }
            wList.add(new WordInfo(id, s));
        } else {
            System.out.println("Index out of hashmap" + id + " : "+ s);
        }
    }

    public Set<Integer> findWords(String text) {

        Set<Integer> ret = new HashSet<>();

        final String s = text.toLowerCase();
        final int dimension = 'z' - 'a';
        for(int i=0; i < s.length() - (minWordSize - 1); i++) {
            final char c1 = s.charAt(i);
            final char c2 = s.charAt(i + 1);
            if (c1 >= 'a' && c1 <= 'z' && c2 >= 'a' && c2 <= 'z') {
                final int index = (c1 - 'a') * dimension + c2 - 'a';
                List<WordInfo> l = wordArray[index];
                if (l != null) {
                    final String substr = s.substring(i);
                    for (WordInfo wi : l) {
                        if (substr.startsWith(wi.word)) {
                            ret.add(wi.id);
                        }
                    }
                }
            }
        }
        return ret;
    }

}
