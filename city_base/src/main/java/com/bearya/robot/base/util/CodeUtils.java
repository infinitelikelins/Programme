package com.bearya.robot.base.util;

import java.util.Collection;
import java.util.Random;

public class CodeUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @SafeVarargs
    public static <T> T oneOf(T... array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("array not be empty");
        }
        return array[new Random().nextInt(array.length)];
    }

}