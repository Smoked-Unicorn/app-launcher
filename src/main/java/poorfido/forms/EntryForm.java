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
import poorfido.exceptions.LauncherAppEx;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;


/**
 * Created by jose on 15/08/16.
 */
public class EntryForm implements Form {

    private static Entry instance = null;
    private String name, path, icon, args;
    private boolean inTerminal;

    public boolean wait;

    private JTextField nameField, iconField, pathField, argsField;
    private JButton browseIcon;
    private JButton browsePath;
    private Button submit;
    JPanel west, center, east, main;
    private JFrame  frame;
    private JLabel  notifications;
    private JCheckBox isInTerminal;

    private Ctr ctr;

    public EntryForm() {
        name = null;
        path = null;
        icon = null;
        args = null;
        ctr = new Ctr();
        wait = true;
        inTerminal = false;
        isInTerminal = null;
    }

    private String seekStart(String str) {
        int start = 0;
        if (str == null) return "";
        if (str.equals("")) return "";
        while (start < str.length() && (str.charAt(start) == '\n' ||
                str.charAt(start) == ' ' || str.charAt(start) == '\t'))
            ++start;
        return str.substring(start);
    }

    private boolean valid(String str) {
        return str != null && !seekStart(str).equals("");
    }

    private boolean validForm() {
        name = nameField.getText();
        icon = iconField.getText();
        path = pathField.getText();
        args = argsField.getText();
        return valid(name) && valid(path) && icon != null && args != null;
    }

    @Deprecated
    public Entry displayForm() {
        build();
        show();
        try {
            while (wait) Thread.yield();
        } catch (LauncherAppEx e) {
            e.report();
        }
        return instance;
    }

    private void err(String str) {
        notifications.setForeground(Color.red);
        notifications.setText(str);
    }

    private void say(String str) {
        notifications.setForeground(Color.black);
        notifications.setText(str);
    }

    @Deprecated
    private void show() {

        frame = new JFrame("Entry Form");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new FormFrameListener(ctr));

        frame.setLayout(new BorderLayout());
        frame.add(submit, BorderLayout.SOUTH);
        frame.add(east, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);
        frame.add(west, BorderLayout.WEST);

        JPanel top = new JPanel(new GridLayout(2,1));
        top.add(notifications);
        top.add(new JPanel());

        frame.add(top, BorderLayout.NORTH);
        frame.pack();
        frame.setMinimumSize(new Dimension(Display.MIN_PAN_DIM));
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void build() {
        nameField = initTextField();
        iconField = initTextField();
        pathField = initTextField();
        argsField = initTextField();
        argsField.setEditable(true);
        nameField.setEditable(true);

        notifications = new JLabel("Nothing to be reported at the moment.");

        browseIcon = new JButton(new ImageIcon(StaticRef.SRCH_ICON_ICON));
        browsePath = new JButton(new ImageIcon(StaticRef.SRCH_FILE_ICON));

        submit     = new Button("Submit ->");

        browsePath.setActionCommand("brw_f");
        browseIcon.setActionCommand("brw_ic");
        submit.setActionCommand("submit");
        browseIcon.addActionListener(ctr);
        browsePath.addActionListener(ctr);
        submit.addActionListener(ctr);

        addAll();

        JPanel top = new JPanel(new GridLayout(2,1));
        top.add(notifications);
        top.add(new JPanel());

        main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(east, BorderLayout.EAST);
        main.add(west, BorderLayout.WEST);
        main.add(submit, BorderLayout.SOUTH);
        main.setPreferredSize(Display.MIN_PAN_DIM);
    }

    private void addAll() {
        west = new JPanel(new GridLayout(4,1));

        west.add(new JLabel(" Name: "));
        west.add(new JLabel(" Path: "));
        west.add(new JLabel(" Icon: "));
        west.add(new JLabel(" Args: "));
        west.validate();
        west.setVisible(true);

        isInTerminal = new JCheckBox("Terminal.");

        //center = new JPanel(new GridLayout(4,1));
        center = new JPanel(new FlowLayout());
        nameField.setText("Name here!");
        center.add(nameField);
        center.add(pathField);
        center.add(iconField);
        center.add(argsField);

        east = new JPanel(new GridLayout(4,1));
        east.add(new JPanel());
        east.add(browsePath);
        east.add(browseIcon);
        east.add(isInTerminal);
        east.setPreferredSize(Display.MIN_PAN_DIM);
    }

    @Deprecated
    public Object get() {
        try {
            while (wait) Thread.yield();
        } catch (LauncherAppEx e) {
            e.report();
        }
        return instance;
    }

    private JTextField initTextField() {
        JTextField t = new JTextField("");
        t.setMinimumSize(Display.MIN_BUT_DIM);
        t.setEditable(false);
        return t;
    }

    @Override
    public JPanel getForm() {
        build();
        return main;
    }

    @Override
    public FormDriver getDriver() {
        return ctr;
    }

    private class Ctr implements ActionListener, FormDriver {

        private EntryFormListener listener;

        @Override
        public void registerListener(EntryFormListener efl) {
            listener = efl;
        }

        public void abort() {
            System.out.println("Aborting App creation. " + instance == null ? "null" : instance + " will be returned.");
            wait = false;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String com = e.getActionCommand();
            JFileChooser fc = new JFileChooser();
            String p;
            File f;
            fc.setMultiSelectionEnabled(false);
            switch (com) {
                case "brw_ic":
                    fc.setDialogTitle("Select an icon.");
                    fc.showDialog(null, "This icon");
                    f = fc.getSelectedFile();
                    if (f == null) p = "";
                    else
                        p = f.getAbsolutePath();
                    iconField.setText(p);
                    break;
                case "brw_f":
                    fc.setDialogTitle("Select a file as target.");
                    fc.showDialog(null, "This file");
                    f = fc.getSelectedFile();
                    if (f == null) p = "";
                    else
                        p = f.getAbsolutePath();
                    pathField.setText(p);
                    if (seekStart(pathField.getText()).equals("")) say("The file path field is required.");
                    break;
                case "submit":
                    if (validForm()) {
                        if (isInTerminal.isSelected()) {
                            instance = new Entry(name, icon, "", path + " " + args, true);
                        } else {
                            instance = new Entry(name, icon, path, args, false);
                        }
                        System.out.println("We've got a valid form! <" + instance + ">");
                        listener.AttendEntryFormListener(new EntryFormListenerRequest(instance, FormAction.ADD));
                    } else
                        err("Name and File are required");
                    break;
                default:
                    err("Unknown command!");
            }
        }
    }

    public class FormFrameListener extends WindowAdapter {

        Ctr ctr;

        public FormFrameListener (Ctr ctr) {
            super();
            this.ctr = ctr;
        }

        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            if (JOptionPane.showConfirmDialog(null, "Do you really want to exit?",
                    "Confirmation dialog.", JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION) {
                frame.dispose();
                ctr.abort();
            } else {
                say("Continue editing.");
            }
        }
    }

    public static void main(String[] args) {
        StaticRef.loadStaticFiles(false);
        System.out.print(App.createApp(new EntryForm().displayForm(), 1));
    }
}
