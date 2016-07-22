package com.test;

import java.util.function.Predicate;

/**
 * Created by Mohan Radhakrishnan on 7/18/2016.
 */
public class Overload {

    interface IntPred { boolean test( Integer value); }

    static boolean check(Predicate<Integer> predicate) {
        return false;
    }

//    static boolean check(IntPred predicate) {
//        return false;
//    }

    public static void main(String... argv){
        check( x -> x > 5 );
    }


}
