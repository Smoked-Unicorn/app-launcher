/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.data;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import poorfido.StaticRef;


import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by jose on 13/08/16.
 *
 * The assignation of the variables is trivial and it's just value assignation.
 */
public class AppTest {

    public static Entry e = null;
    public static String base = StaticRef.IMG_FOLDER;

    @BeforeClass
    public static void init() {
        System.out.println("Working on: " + new File("").getAbsolutePath());
        StaticRef.loadStaticFiles(false);
        e = new Entry("test", base + "default.png", "target1", "", false);
        boolean preErrors = false;

        if (!new File(base + "default.png").isFile())
            preErrors = true;
        if (!new File(base + "smile.png").isFile())
            preErrors = true;
        if (preErrors)
            System.err.println("WARNING THE PRECONDITIONS OF THIS TEST ARE NOT SATISFIED! CHECK THE FILES!");
        else
            System.out.println("Preconditions satisfied.");
        assertFalse(preErrors);
    }

    @Test
    public void createWithValidIcon() {
        //This icon exists.
        e.setIcon(base + "smile.png");
        App app = App.createApp(e, 3);
        assertNotEquals(StaticRef.DEF_ICON, app.getIcon());
    }

    @Test
    public void createWithInvalidIcon() {
        e.setIcon(base + "invalid.png");
        App app = App.createApp(e, 3);
        assertEquals(app.getIcon(), StaticRef.DEF_ICON);
    }

    @Test
    public void changingIconToValidIcon() {
        App app = App.createApp(e, 1); //current: default.png (Is valid)
        app.loadNewIcon(base + "smile.png");//Valid
        assertNotEquals(StaticRef.DEF_ICON, app.getIcon()); //Icon changed.
    }

    @Test
    public void changinIconToInvalidIcon() {
        App app = App.createApp(e, 1);
        app.loadNewIcon("iDontExist.pgn");  //Invalid icon.
        assertEquals(app.getIcon(), StaticRef.DEF_ICON); //So same icon as before.
    }

    @Test
    /**
     * For a given entry (witch can have a valid or invalid icon) the returned Entry by the App.toEntry() matches.
     * Input -> App -> toEntry :: Input == toEntry.
     *
     * The paths must be given in the same format (absolute).
     */
    public void inputEntryEqualsToReturnedEntryFromApp() {
        File f = new File(StaticRef.DEF_ICON_PATH);
        Entry src = new Entry("A", f.getAbsolutePath(), new File("target").getAbsolutePath(), "", false);
        App app = App.createApp(src, 2);
        boolean res = App.toEntry(app).equals(src);
        assertTrue(res);
    }
}