package com.test;

/**
 * Created by Mohan Radhakrishnan on 7/19/2016.
 */
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class WordCount {

    public int x = 26;

    public static void main(String... argv){

        WordCount wc = new WordCount();
        wc.count();
    }

    private void count() {

        String s  = "__mainn__".replaceAll("[^a-z\\s]", "");

        System.out.println(s);


        final Map<Character, Integer> count = s.chars().
                map(Character::toLowerCase).
                collect(TreeMap::new, (m, c) -> m.merge((char) c, 1, Integer::sum), Map::putAll);
        System.out.println( Arrays.toString(count.entrySet().toArray()) );

        count.entrySet().stream().
                sorted((l, r) -> r.getValue().compareTo(l.getValue())).
                forEach(e -> count.merge(e.getKey(), x--, Math::multiplyExact));
        //Stop when x == 0.Not tested

        System.out.println(count.entrySet().stream().mapToInt(e -> e.getValue()).sum());
        //Treating these numbers as double to sum them. Doesn't seem to matter.
    }
}
