/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.flow;

import poorfido.data.App;
import poorfido.data.Database;
import poorfido.data.Entry;
import poorfido.desing.UserPanel;
import poorfido.forms.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
        EntryForm entryForm;
        switch (com) {
            case "+":
                entryForm = new EntryForm();
                view.setRightPane(entryForm.getForm());
                view.setEnabled(false);
                entryForm.getDriver().registerListener(this);
                view.setEnabled(true);
                    break;
        }
    }


    @Override
    public void AttendEntryFormListener(EntryFormListenerRequest request) {
        if (request.getAction().equals(FormAction.ADD)) {
            Entry e = (Entry) request.getResource();
            db.add(e);
            view.buildButtonGrid(db.getList());
            view.noForm();
            view.setEnabled(true);
        } else {
            System.out.println("This action is not supported in this Listener.");
        }
    }
}
