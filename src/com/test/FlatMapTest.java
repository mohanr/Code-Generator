package com.test;

/**
 * Created by Mohan Radhakrishnan on 7/18/2016.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapTest {

    public static void main( String... argv ){

        List<String> source = new ArrayList<String>();
        source.add( "Peak Usage    : init:2359296, used:13914944, committed:13959168, max:50331648Current Usage : init:2359296, used:13913536, committed:13959168, max:50331648|------------------| committed:13.31Mb+---------------------------------------------------------------------+|//////////////////|                                                  | max:48Mb+---------------------------------------------------------------------+|------------------| used:13.27Mb");
        source.add( "Peak Usage    : init:2359296, used:13916608, committed:13959168, max:50331648Current Usage : init:2359296, used:13915200, committed:13959168, max:50331648|------------------| committed:13.31Mb+---------------------------------------------------------------------+|//////////////////|                                                  | max:48Mb+---------------------------------------------------------------------+|------------------| used:13.27Mb");

        List<Pattern> patterns = Arrays.asList(Pattern.compile("Current.*?[/|]"), Pattern.compile("[0-9]+(/,|/|)"));

        //Style 1
        patterns.stream()
                .flatMap(new Function<Pattern, Stream<String>>() {
                    @Override
                    public Stream<String> apply(Pattern p) {
                        return source.stream()
                                .map(p::matcher)
                                .filter(Matcher::find)
                                .map(Matcher::group);
                    }
                })
                .forEach(System.out::println);

        //Style 2
        patterns.stream().flatMap(( Pattern p1 ) -> source.
                stream().
                map(p1::matcher).
                filter(Matcher::find).map(matcher -> matcher.group())).forEach(x -> System.out.println(x));

    }
}
