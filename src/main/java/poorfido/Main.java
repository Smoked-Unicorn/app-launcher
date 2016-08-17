/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself (the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido;

import poorfido.data.Database;
import poorfido.data.Entry;
import poorfido.desing.MainPanel;
import poorfido.desing.UserPanel;
import poorfido.flow.FlowControl;
import poorfido.forms.EntryForm;
import poorfido.forms.Form;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.awt.BorderLayout.*;
/**
 * Created by jose on 13/08/16.
 */
public class Main {

    public static void test(boolean reset) {
        StaticRef.loadStaticFiles(reset);
        UserPanel pan = new MainPanel();
        Database db = new Database(StaticRef.DB_FILE);
        db.load();
        pan.buildButtonGrid(db.getList());
        FlowControl ctr = new FlowControl(pan, db);
    }

    public static void main(String[] args) {
        System.out.println("Arguments : " + Arrays.toString(args));
        boolean reset = false, init = false;
        if (args.length == 1 && args[0].equals("reset"))
            reset = true;
        test(reset);
    }
}
