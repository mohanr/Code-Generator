package com.test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Mohan Radhakrishnan on 7/13/2016.
 */
public class OptionalTest {

    private static List<String> newImports = new ArrayList<>();

    public static Optional<List<String>> getOptionalNewImports() {

        return Optional.ofNullable(newImports);
    }

    public static void main( String... argv ){

        newImports.add("org.apache.struts");

        if( getOptionalNewImports().isPresent() ) {
            List<String> imports = new ArrayList<>();
                    getOptionalNewImports().get().stream()
                    .map(p -> "import " + p + ";")
                    .collect(Collectors.toCollection(() -> imports));
            imports.forEach( System.out::println);
        }

    }
}
