/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.desing;

import poorfido.Main;
import poorfido.StaticRef;
import poorfido.data.App;
import poorfido.forms.Form;
import poorfido.forms.FormDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

import static poorfido.StaticRef.ERROR;
import static poorfido.StaticRef.OK;

/**
 * Created by jose on 15/08/16.
 */
public class MainPanel implements UserPanel {

    JFrame frame;
    JPanel top, center, left, right, bottom;
    JScrollPane scroll;
    JButton     load,
                save,
                add,
                del;
    JTextField idText;
    JTextArea log;

    public MainPanel() {
        frame = new JFrame("prLauncher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        build();

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        top.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY));
        bottom.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Color.GRAY));
        frame.setMinimumSize(Display.MIN_WINDOW_DIM);
        frame.pack();
        frame.setVisible(true);
    }

    private void build() {
        add = new JButton(new ImageIcon(StaticRef.ADD_ICON));
        del = new JButton(new ImageIcon(StaticRef.DEL_ICON));
        save = new JButton(new ImageIcon(StaticRef.SAVE_ICON));
        load = new JButton(new ImageIcon(StaticRef.LOAD_ICON));

        setEnabled(false);
        add.setActionCommand("+");
        del.setActionCommand("-");
        save.setActionCommand("s");
        load.setActionCommand("l");

        idText = new JTextField("00");
        idText.setPreferredSize(Display.TWO_DIGITS_FIELD);

        top = new JPanel(new FlowLayout());
        top.add(save);
        top.add(load);
        top.add(add);
        top.add(del);
        top.add(idText);
        top.add(new JLabel("# to delete"));

        center = new JPanel(new GridLayout(4,4));
        int i = 0;
        while (i < 16) {
            center.add(new JPanel());
            ++i;
        }

        right = new JPanel();

        log = new JTextArea();
        log.setEditable(false);
        log.setAutoscrolls(true);
        log.setText("------------ SYSTEM LOG ------------\n");
        bottom = new JPanel(new GridLayout(1,1));
        bottom.setPreferredSize(new Dimension(frame.getWidth() - 10, 100));
        scroll = new JScrollPane(log);
        bottom.add(scroll);
    }

    @Override
    public void register(ActionListener l) {
        add.addActionListener(l);
        del.addActionListener(l);
        save.addActionListener(l);
        load.addActionListener(l);
        setEnabled(true);
    }


    @Override
    public void say(String msg) {
        log.append(OK + msg + "\n");
    }

    @Override
    public void error(String msg) {
        log.append(ERROR + msg + "\n");
    }

    @Override
    public void setEnabled(boolean b) {
        add.setEnabled(b);
        del.setEnabled(b);
        load.setEnabled(b);
        save.setEnabled(b);
    }

    @Override
    public void buildButtonGrid(Set<App> apps) {
        int COLS = 4, ROWS = (apps.size()/COLS) + 1;
        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
        JButton button;
        for (App a : apps) {
            button = new AppButton(a);
            grid.add(button);
            if (a.getIcon().equals(StaticRef.DEF_ICON)) {
                error("For <" + a + "> icon format is invalid or does not exist.\n\t" +
                        "The supported formats are: png, bmp, gif and jpg");
            }
        }
        frame.remove(center);
        center = grid;
        frame.add(center, BorderLayout.CENTER);
        frame.pack();
    }

    public void setRightPane(JPanel form) {
        frame.remove(right);
        right = form;
        frame.add(right, BorderLayout.EAST);
        frame.pack();
    }

    @Override
    public String getIDText() {
        return idText.getText();
    }


    @Override
    public void noForm() {
        frame.remove(right);
        frame.pack();
    }

    public void dispose() {
        frame.dispose();
    }
}
