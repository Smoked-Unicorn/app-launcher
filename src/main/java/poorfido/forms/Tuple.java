/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.forms;

/**
 * Created by jose on 19/08/16.
 */
public class Tuple <A,B> {

    private A fst;

    private B snd;

    public Tuple(A a, B b) {
        fst = a;
        snd = b;
    }

    public A fst() {
        return fst;
    }

    public B snd() {
        return snd;
    }

    public String toString() {
        return "< " +  fst + ", " + snd + ">";
    }
}
