/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.forms;

import poorfido.data.Entry;

/**
 * Created by jose on 16/08/16.
 */
public class EntryFormListenerRequest {
    private Object resource;
    private FormAction action;

    public FormAction getAction() {
        return action;
    }

    public Object getResource() {
        return resource;
    }

    public EntryFormListenerRequest(Object o, FormAction a) {
        resource = o;
        action = a;

    }
}
