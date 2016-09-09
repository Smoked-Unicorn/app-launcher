/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.forms;


import poorfido.StaticRef;
import poorfido.data.App;
import poorfido.data.Entry;
import poorfido.desing.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static java.awt.BorderLayout.*;
import static poorfido.Utilities.seekStart;

/**
 * Created by jose on 19/08/16.
 */
public class EditEntryForm implements Form {

    private FormControl control;
    private JPanel main, left, center, right, top;

    private JTextField name, icon, path, args, dest;
    private JButton browse_icon, browse_path;
    private Button submit;
    private JLabel notifications;
    private JCheckBox inTerminal;

    public EditEntryForm(App source) {

        String tname, tfile, ticon, targs, tid;

        if (source != null) {
            tname = source.getName();
            tfile = source.getFilePath();
            ticon = source.getIconPath();
            targs = source.getArgs();
            tid   = Integer.toString(source.getId());
        } else {
            tname = "Blank";
            tfile = "";
            ticon = "";
            targs = "";
            tid   = "";
        }

        control = new FormControl();
        main = new JPanel(new BorderLayout());
        icon = new JTextField(ticon);
        path = new JTextField(tfile);
        name = new JTextField(tname);
        args = new JTextField(targs);
        dest = new JTextField(tid);

        path.setEditable(false);
        icon.setEditable(false);

        inTerminal = new JCheckBox("In Terminal. ");
        inTerminal.setSelected(source.isTerminal());
        notifications = new JLabel("Edit this entry #" + source.getId() + ".");
        browse_icon = new JButton(new ImageIcon(StaticRef.SRCH_ICON_ICON));
        browse_path = new JButton(new ImageIcon(StaticRef.SRCH_FILE_ICON));
        submit = new Button("Apply changes.");

        left = new JPanel(new GridLayout(4,1));
        left.add(new JLabel("Name: "));
        left.add(new JLabel("File: "));
        left.add(new JLabel("Icon: "));
        left.add(new JLabel("Args"));

        center = new JPanel(new GridLayout(4,1));
        center.setPreferredSize(Display.FORM_FIELDS_DIM);
        center.add(name);
        center.add(path);
        center.add(icon);
        center.add(args);

        browse_icon.setEnabled(false);
        browse_path.setEnabled(false);
        submit.setEnabled(false);

        right = new JPanel(new GridLayout(4,1));
        right.add(dest);
        right.add(browse_path);
        right.add(browse_icon);
        right.add(inTerminal);

        top  = new JPanel(new GridLayout(2,1));
        top.add(notifications);
        top.add(new JPanel());

        main.add(top, NORTH);
        main.add(center, CENTER);
        main.add(left, WEST);
        main.add(right, EAST);
        main.add(submit, SOUTH);
        register(control);
    }

    public void register(ActionListener e) {
        browse_path.addActionListener(e);
        browse_path.setActionCommand("p");
        browse_path.setEnabled(true);
        browse_icon.addActionListener(e);
        browse_icon.setActionCommand("i");
        browse_icon.setEnabled(true);
        submit.addActionListener(e);
        submit.setActionCommand("s");
        submit.setEnabled(true);
    }

    @Override
    public JPanel getForm() {
        return main;
    }

    @Override
    public FormDriver getDriver() {
        return control;
    }

    private class FormControl implements FormDriver {

        EntryFormListener efl;

        @Override
        public void registerListener(EntryFormListener efl) {
            this.efl = efl;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String com = e.getActionCommand();
            JFileChooser fc = new JFileChooser("");
            fc.setMultiSelectionEnabled(false);
            File file;
            String p;
            switch (com) {
                case "f":
                    fc.setDialogTitle("Select the new file.");
                    fc.showDialog(null, "Select");
                    file = fc.getSelectedFile();
                    if (file != null || file.isFile()) {
                        path.setText(file.getAbsolutePath());
                        say("File found.");
                    } else {
                        err("This file does not exist.");
                    }
                    break;
                case "i":
                    fc.setDialogTitle("Select the new Icon.");
                    fc.showDialog(null, "Select");
                    file = fc.getSelectedFile();
                    if (file != null || file.isFile()) {
                        icon.setText(file.getAbsolutePath());
                        say("File found.");
                    } else {
                        err("This file does not exist.");
                    }
                    break;
                case "s":
                    int num = -1;
                    Entry entry = null;
                    try {
                        num = Integer.parseInt(dest.getText());

                        if (checkAllValid()) {
                            say("Working...");
                            if (inTerminal.isSelected())
                                entry = new Entry(name.getText(),icon.getText(),"", path.getText() + " " + args.getText(), true);
                            else
                                entry = new Entry(name.getText(), icon.getText(), path.getText(), args.getText(), false);
                        } else {
                            err("Some fields are not correct.");
                            return;
                        }

                        Tuple<Integer, Entry> tuple = new Tuple<>(num, entry);
                        efl.AttendEntryFormListener(new EntryFormListenerRequest(tuple, FormAction.EDIT));

                    }catch (NumberFormatException e1) {
                        err("Invalid destination: expected int");
                        return;
                    }
                    break;
                default:
                    err("Invalid command.");
                    break;
            }
        }
    }

    private boolean isValid(String str, boolean blank) {
        boolean res = str != null;
        if (!blank)
            res = !seekStart(str).equals("");
        return  res;
    }

    private void err(String msg) {
        notifications.setForeground(Color.RED);
        notifications.setText(msg);
    }

    private void say(String str) {
        notifications.setForeground(Color.BLACK);
        notifications.setText(str);
    }

    private boolean checkAllValid() {
        String f, n, i, arg;
        f = path.getText();
        n = name.getText();
        i = icon.getText();
        arg = args.getText();
        if (inTerminal.isSelected()) {
            return  isValid(f, true) && isValid(n,false) && isValid(i,true) && isValid(arg,false);
        } else {
            return isValid(f, false) && isValid(n, false) && isValid(i, true) && isValid(arg, true);
        }
    }
}
