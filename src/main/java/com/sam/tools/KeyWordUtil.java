package com.sam.tools;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyWordUtil {

    public static final Set<String> keyWordSet;

    static {

        HashSet<String> set = new HashSet<String>();
        set.add("为何");
        set.add("什么");
        set.add("?");
        set.add("？");
        set.add("吗");
        set.add("哪");
        keyWordSet = Collections.unmodifiableSet(set);
    }


    public static Boolean hasKey(String title) {
        for (String word: keyWordSet) {
            if (title.contains(word)){
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(hasKey("水水水水"));

    }
}
