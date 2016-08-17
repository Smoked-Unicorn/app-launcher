/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.exceptions;

/**
 * Created by jose on 13/08/16.
 */
public class LauncherAppEx extends RuntimeException {
    public LauncherAppEx() {
        super();
    }

    public LauncherAppEx(String message) {
        super(message);
    }

    public void report() {
        System.err.println("Ex: [LAUNCHER EX] - \n\t " + getMessage() + "\n");
    }
}
