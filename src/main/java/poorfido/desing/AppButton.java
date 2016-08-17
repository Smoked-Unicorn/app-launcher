/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.desing;

import poorfido.StaticRef;
import poorfido.data.App;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Created by jose on 15/08/16.
 */
public class AppButton extends JButton {

    private App target;
    private executer exec;

    public AppButton(App a) {
        super(new ImageIcon(a.getIcon()));
        setEnabled(false);
        target = a;
        setText("#" + a.getId() + " " + a.getName());
        setToolTipText(a.getFilePath());
        exec = new executer();
        addMouseListener(exec);
        setEnabled(true);
    }

    private class executer implements MouseListener {

        public void execute(String file, String args) throws IOException {
            Process p = new ProcessBuilder(file, args).start();
            System.out.println("Launched!");
        }

        public void runInTerminal (String file, String args) throws IOException {
            Runtime rt = Runtime.getRuntime();
            String command = StaticRef.TERMINAL + " -e " + args;
            rt.exec(command);
            System.out.println("Launched in terminal!");
            System.out.println(rt.maxMemory());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                try {
                    if (target.isTerminal())
                        runInTerminal(target.getFilePath(), target.getArgs());
                    else
                        execute(target.getFilePath(), target.getArgs());
                } catch (IOException e1) {
                    System.err.println("IOException at " + target.getName() + " || " + target.getFilePath());
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
