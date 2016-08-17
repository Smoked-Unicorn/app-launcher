/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import poorfido.StaticRef;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.FileHandler;

import static org.junit.Assert.*;

/**
 * Created by jose on 13/08/16.
 */
public class DatabaseTest {

    public static String ONE_ELEM_F = "testData/one.json";
    public static Database st = new Database("testData/def.json");
    public static Entry
                        e1 = new Entry("N1", "I1", "F1", "", false),
                        e2 = new Entry("N2", "I1", "F2", "", false);

    @Test
    public void load() throws Exception {
        Database db = new Database(ONE_ELEM_F);
        db.load();
        assertTrue(db.size() == 1);
    }

    @After
    public void init() {
        st.add(e1);
    }

    @Test
    public void save() throws Exception {
        File file = new File("testData/null.json");
        Database a = new Database(file.getAbsolutePath());

        a.add(e1);
        a.add(e2);

        a.save();

        Database b = new Database(file.getAbsolutePath());
        b.load();
        try {
            assertTrue(a.getList().equals(b.getList()));
        } finally {
            file.delete();
        }
    }

    @Test
    public void add() throws Exception {
        st.add(e1);
        assertTrue(st.size() == 2);
    }

    @Test
    public void addEqualIsIgnored() throws Exception {
        Database blank = new Database("testData/blank.json");
        blank.load();
        blank.add(e1);
        int i = st.size();
        blank.add(e1);
        assertEquals(i, st.size());
    }

    @Test
    public void addDifferentNotIgnored() throws Exception {
        Database blank = new Database("testData/blank.json");
        blank.load();
        assertEquals(blank.size(), 0);
        blank.add(e1);
        int i = blank.size();
        blank.add(e2);
        assertTrue(i + 1 == blank.size());
    }

    @Test
    public void delete() throws Exception {
        st.add(e1);
        st.add(e2);
        int i = st.size();
        st.delete(App.createApp(e2, 1));
        assertTrue(st.size() == i - 1);
    }

    @Test
    public void getList() throws Exception {
        st.add(e1);
        st.add(e2);
        Set<App> set = st.getList();
        boolean allIn = set.contains(App.createApp(e1,2)) && set.contains(App.createApp(e2,1));
        assertTrue(allIn);
    }

    @Test
    public void dump() throws Exception {
        st.add(e2);
        st.dump();
        assertTrue(st.size() == 0);
    }

    @Test
    public void wrongFileSetToNullList() {
        File wrong = new File("testData/wrong.json");
        Database db = new Database(wrong.getAbsolutePath()); //Wrong json format with nothing.
        db.load();
        try (Scanner sc = new Scanner(wrong)){
            assertEquals(sc.nextLine(), "[]");
        } catch (FileNotFoundException e) {
            fail("File not found.");
        }

    }

    @Test
    public void nullFileCreatedToNullList(){
        File nullFile = new File("testData/null.json");
        Database db = new Database(nullFile.getAbsolutePath());
        db.load();

        try (Scanner sc = new Scanner(nullFile)){
            assertEquals("[]", sc.nextLine());
        } catch (FileNotFoundException e) {
            System.out.print("FAILED HERE!");
            fail();
        }

        nullFile.delete();
    }

}