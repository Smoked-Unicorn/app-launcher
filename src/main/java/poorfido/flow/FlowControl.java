/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.flow;

import poorfido.StaticRef;
import poorfido.data.App;
import poorfido.data.Database;
import poorfido.data.Entry;
import poorfido.desing.UserPanel;
import poorfido.forms.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


/**
 * Created by jose on 15/08/16.
 */
public class FlowControl implements ActionListener, EntryFormListener {

    private UserPanel view;
    private Database db;

    public FlowControl(UserPanel pan, Database model) {
        view = pan;
        db = model;
        view.register(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String com = e.getActionCommand();
        Form entryForm;

        switch (com) {
            case "+":
                entryForm = new EntryForm();
                view.setRightPane(entryForm.getForm());
                view.setEnabled(false);
                entryForm.getDriver().registerListener(this);
//                view.setEnabled(true);
                    break;
            case "s":
                db.save();
                view.say("Data saved!");
                break;
            case "e":
                entryForm = new EditEntryForm(findApp(view.getIDText()));
                view.setRightPane(entryForm.getForm());
                view.setEnabled(false);
                entryForm.getDriver().registerListener(this);
                break;
            case "log":
                view.switchLog();
        }
    }


    private App findApp(String str) {
        int id = -1;
        try {
            id = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            view.error("Could not parse " + str + " to a valid ID.");
            return null;
        }

        Iterator<App> it = db.iterator();
        App res;
        if (id < 0) {
            view.error("Id can't be negative.");
            return null;
        }

        while (it.hasNext()){
            res = it.next();
            if (res.getId() == id)
                return res;
        }
        view.error("App with id " + str + " could not be found!");
        return null;
    }

    @Override
    public void AttendEntryFormListener(EntryFormListenerRequest request) {
        if (request.getAction().equals(FormAction.ADD)) {
            Entry e = (Entry) request.getResource();
            db.add(e);
            view.buildButtonGrid(db.getList());
            view.noForm();
            view.setEnabled(true);
        } else if (request.getAction().equals(FormAction.EDIT)){
            System.out.println("This action is not supported in this Listener.");
            Tuple<Integer,Entry> tuple = (Tuple<Integer,Entry>) request.getResource();
            Entry e = tuple.snd();
            int num = tuple.fst();
            replace(e, num);
        }
    }

    private void replace(Entry neo, int ident) {

        boolean hasIt = false;

        Iterator<App> it = db.iterator();
        App current;
        System.out.println("Looking for the app with id: " + ident);
        while (it.hasNext()) {
            current = it.next();
            if (current.getId() == ident) {
                System.out.println("HIT!");
                hasIt = true;
                db.delete(current);
                break;
            }
        }
        if (!hasIt) {
            view.error("App not found with id: " + ident);
            view.say("Creating a new App with those values.");
            System.out.println(StaticRef.ERROR + " - App not found! Creating a new one with the data specified.");
        }
        db.add(neo);
    }
}
