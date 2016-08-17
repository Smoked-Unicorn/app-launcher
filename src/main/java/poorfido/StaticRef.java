/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido;

import poorfido.exceptions.LauncherAppEx;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by jose on 13/08/16.
 */
public class StaticRef {

    public static final String CONFIG_FILE = "config";

    public static  String TERMINAL = "/usr/bin/gnome-terminal";
    public static BufferedImage DEF_ICON        = null,
                                SRCH_FILE_ICON  = null,
                                SRCH_ICON_ICON  = null,
                                SAVE_ICON       = null,
                                LOAD_ICON       = null,
                                ADD_ICON        = null,
                                DEL_ICON        = null;

    public static String BASE_FOLDER    = "data/";
    public static String DB_FILE        = BASE_FOLDER + "db.json";
    public static String IMG_FOLDER     = BASE_FOLDER + "img/";
    public static String DEF_ICON_PATH  = IMG_FOLDER + "default.png";

    public static String    SRCH_FILE_ICON_PATH = IMG_FOLDER + "srch_file.png",
                            SRCH_ICON_ICON_PATH = IMG_FOLDER + "srch_icon.png",
                            LOAD_ICON_PATH      = IMG_FOLDER + "load.png",
                            SAVE_ICON_PATH      = IMG_FOLDER + "disk.png",
                            DEL_ICON_PATH       = IMG_FOLDER + "del.png",
                            ADD_ICON_PATH       = IMG_FOLDER + "add.png",
                            USR_IMG_FOLDER      = BASE_FOLDER + "MyIcons/";

    public static String    WARN    = "Warning: ",
                            OK      = "Success: ",
                            ERROR   = "Error: ";

    /**
     * Run always at the begin of the execution.
     * If the load fails it is suggested to abort the execution (important files missing).
     */
    public static boolean loadStaticFiles(boolean reset) {

        System.out.print("Reading config file...");

        try {
            if (!reset)
                loadConfiguration();
        }catch(LauncherAppEx e){
            e.report();
        }

        System.out.println("DEF_ICON_PATH: " + DEF_ICON_PATH);

        System.out.println("Done.");
            System.out.println("Loading static files...");
            DEF_ICON        = singleImageLoad(DEF_ICON_PATH, "Default icon");
            SRCH_FILE_ICON  = singleImageLoad(SRCH_FILE_ICON_PATH, "Search file icon");
            SRCH_ICON_ICON  = singleImageLoad(SRCH_ICON_ICON_PATH, "Search icon icon");
            SAVE_ICON       = singleImageLoad(SAVE_ICON_PATH, "Disk icon");
            LOAD_ICON       = singleImageLoad(LOAD_ICON_PATH, "Load");
            ADD_ICON        = singleImageLoad(ADD_ICON_PATH, "Add");
            DEL_ICON        = singleImageLoad(DEL_ICON_PATH, "Del");
            System.out.println("Load step finished.");

        System.out.println("Load done.");

        if (reset) {
            createConfigFile();
        }

        if (DEF_ICON == null) {
            JOptionPane.showMessageDialog(null,
                            "The default icon could not be loaded and thus, all the buttons in this app will fail.\n" +
                            "Please, make sure that the default icon at <" + DEF_ICON_PATH + "> exists and that this \n" +
                                    "program has enough access privileges.", "System can't continue.", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static BufferedImage singleImageLoad (String path, String report) {
        File f = new File(path);
        BufferedImage o = null;
        if (!f.isFile()) {
            System.err.println(ERROR + "The file " + path + " does not exist.");
            System.err.flush();
            return o;
        }
        try {
            System.out.print("Loading: " + path + "...");
            o = ImageIO.read(f);
            System.out.println(OK + "Done!");
        } catch (IOException e) {
            System.out.println(ERROR + "Failed!");
            e.printStackTrace();
            System.err.flush();
        } finally {
            return o;
        }
    }

    private static void loadConfiguration() {
        File conf = new File(CONFIG_FILE);

        if (!conf.isFile()) {
            System.out.println("There is no config file at: " + conf.getAbsolutePath() + " create one for custom settings.");
        }

        String key, value;
        Scanner tokenizer;
        try (Scanner lines = new Scanner(conf)){
            while (lines.hasNextLine()) {
                key = "null";
                value = "null";
                tokenizer = new Scanner(lines.nextLine());
                tokenizer.useDelimiter("=");
                if (tokenizer.hasNext())
                    key = tokenizer.next();
                if (tokenizer.hasNext())
                    value = tokenizer.next();
                filter(key,value);
                tokenizer.close();
            }
        }catch (IOException e){
            throw new LauncherAppEx("Problem loading configuration file. Default settings will be used.");
        }
    }

    public static void createConfigFile() {
        System.out.println("Creating a new Config file: With the current settings.");
        File conf = new File(CONFIG_FILE);
        System.out.println("\t\tTrying at: " +conf.getAbsolutePath());
        try {
            if (!conf.isFile())
                conf.createNewFile();
        } catch (IOException e) {
            System.out.println("Unable to create the config file. Check privileges.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(conf)){
            pw.print(printKV("IMG_FOLDER", IMG_FOLDER));
            pw.print(printKV("DB_FILE", DB_FILE));
            pw.print(printKV("BASE_FOLDER", BASE_FOLDER));
            pw.print(printKV("DEF_ICON", DEF_ICON_PATH));
            pw.print(printKV("TERMINAL", TERMINAL));
            pw.print(printKV("USR_IMG_FOLDER", USR_IMG_FOLDER));
            pw.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write data into file. Check write privileges.");
            e.printStackTrace();
        }

    }

    private static String printKV(String k, String v) {
        return k + "=" + v + "\n";
    }

    private static void filter (String key, String value) {
        if (key == null || value == null) {
            System.out.println("Invalid entry: <" + key + ", " + value + ">");
            return;
        }
        switch (key) {
            case "IMG_FOLDER":
                IMG_FOLDER = BASE_FOLDER + value;
                break;
            case "DB_FILE":
                DB_FILE  = value;
                break;
            case "BASE_FOLDER":
                BASE_FOLDER = value;
                break;
            case "DEF_ICON":
                DEF_ICON_PATH = value;
                break;
            case "TERMINAL":
                TERMINAL = value;
                break;
            case "USR_IMG_FOLDER":
                USR_IMG_FOLDER = value;
                break;
            default:
                System.out.print("Unknown key: " + key);
                break;
        }
    }
}
