/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido;

/**
 * Created by jose on 20/08/16.
 */
public class Utilities {

    /**
     * Finds the starting character of a given string. Returns the resulting substring of the string starting in that index.
     * @param str is the input string.
     * @return a substring of str (if any) or blank string if str was formed by \n \t ' ' or '' completely.
     */
    public static String seekStart(String str) {
        int start = 0;
        if (str == null) return "";
        if (str.equals("")) return "";
        while (start < str.length() && (str.charAt(start) == '\n' ||
                str.charAt(start) == ' ' || str.charAt(start) == '\t'))
            ++start;
        return str.substring(start);
    }
}