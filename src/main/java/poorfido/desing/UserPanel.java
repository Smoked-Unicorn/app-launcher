/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.desing;

import poorfido.data.App;
import poorfido.forms.Form;
import poorfido.forms.FormDriver;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Created by jose on 15/08/16.
 */
public interface UserPanel {

    void register(ActionListener l);
    void say(String msg);
    void error(String msg);
    void setRightPane(JPanel form);

    void setEnabled(boolean b);
    void buildButtonGrid(Set<App> apps);
    String getIDText();

    void noForm();
}
