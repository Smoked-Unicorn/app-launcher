/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import poorfido.StaticRef;
import poorfido.exceptions.LauncherAppEx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static poorfido.StaticRef.ERROR;

/**
 * Created by jose on 13/08/16.
 */
public class Database implements DataPersistenceSystem {

    private Set<App> content;
    private File source;
    private int cont;

    public Database(String path) {
        source = new File(path);
        if (!source.isFile()){
            try {
                System.out.print("Creating file <" + source.getAbsolutePath() + ">...");
                source.createNewFile();
                PrintWriter init = new PrintWriter(source);
                init.print("[]");
                init.flush();
                init.close();
                System.out.println("Done!");
            } catch (IOException e) {
                System.err.println(ERROR + " Impossible to create. More details: " + e.getLocalizedMessage());
                e.printStackTrace();
                throw new LauncherAppEx("Impossible to set the database.");
            }
        }
        System.out.println("Database ready. Waiting for requests.");
        content = new TreeSet<>();
        cont = 0;
    }

    public void load() {
        Gson gson = new Gson();
        System.out.print("Loading data...");
        try {
            Entry[] a = gson.fromJson(read(source), Entry[].class);
        if (a.length != 0) {
            for (Entry e : a) {
                add(e);
            }
        }

        } catch (JsonSyntaxException e) {
            System.out.println("The db.json file is corrupt or is not in json format.");
            save();
        }
        System.out.println("Done.");
    }

    public void save() {
        System.out.print("Size: " + size());
        if (size() == 0) {
            try (PrintWriter pw = new PrintWriter(source)){
                pw.print("[]");
                pw.flush();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.print("Saving data...");
        Gson gson = new Gson();

        Entry[] el = new Entry[content.size()];

        int i = 0;
        Iterator<App> it = content.iterator();
        while(it.hasNext()) {
            el[i] = App.toEntry(it.next());
            ++i;
        }

        try (PrintWriter pw = new PrintWriter(source)){
            pw.print(gson.toJson(el));
            pw.flush();
        } catch (FileNotFoundException e) {
            System.err.println("File not found (" + source + ")!" + e.getLocalizedMessage());
        }
    }

    public void add(Entry e) {
        if (e == null) return;
        content.add(App.createApp(e, cont++));
    }


    public void delete(App a) {
        content.remove(a);
    }

    @Deprecated
    public App get(int index) {
        if (index >= content.size() || index < 0) return null;
        Iterator<App> it = content.iterator();
        int i = 0;
        App res = null;
        while(i < index && it.hasNext()) {
            res = it.next();
            ++i;
        }
        return res;
    }

    public Set<App> getList() {
        return content;
    }

    public void dump() {
        content = new TreeSet<>();
        System.out.println("Database dumped. Nothing has been written yet.");
    }

    public int size() {
        return content.size();
    }

    private String read(File file){
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(file)){
            while(sc.hasNext())
                sb.append(sc.next());
        } catch (FileNotFoundException e) {
            System.err.println("File not found (" + file + ")!" + e.getLocalizedMessage());
            sb = new StringBuilder("[]");
        }
        return sb.toString();
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (App a : content) {
            sb.append(a + ",\n\t");
        }

        return "Contains: " + size() + " elements. [\n\t" + sb.toString() + "\n].";
    }
}
