/*
 * Copyright (c) 2016.
 * This project has been done by José Miguel Vega Moreno for personal use only.
 * I give you zero warranties about the data storage and security of itself ( the data will be stored in plain text format).
 * You can make use of this software if you don't own the authority of it.
 */

package poorfido.data;

import poorfido.StaticRef;
import poorfido.exceptions.FileNotFoundEx;
import poorfido.exceptions.LauncherAppEx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static poorfido.StaticRef.WARN;

/**
 * Created by jose on 13/08/16.
 * This class is the functional object that loads its data from an entry of the db.
 *
 * The App will try to load the icon file that its provided. If an error occurs with it,
 * the default icon will be used instead.
 */
public class App implements Comparable<App>{

    private String  name,
                    args;

    private int id;
    private boolean terminal;

    private File    file, imgFile;

    private BufferedImage icon;

    public static App createApp(Entry entry, int id) {
        App res = null;

        String ipath = entry.getIcon();

        String name;
        BufferedImage img = getIconIfCorrect(ipath);

        if (img == null)
            img = StaticRef.DEF_ICON;

        name = entry.getName();
        if (name.equals(""))
            name = "Blank Name";

        if (entry.getPath().equals(""))
            System.out.println(WARN + " There is no file attached to this app.");



        return new App(name, new File(entry.getPath()), entry.getArgs(), new File(ipath), img, id, entry.isTerminal());
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    private App(String name, File file, String args, File iconFile, BufferedImage icon, int id, boolean inTerm) {
        this.name = name;
        this.args = args;
        this.id = id;
        this.file = file;
        this.imgFile = iconFile;
        this.icon = icon;
        this.terminal = inTerm;
    }

    /**
     * Reads the image pointed by path.
     * @param path The image path to be loaded.
     * @return BufferedImage read from path or null if an error occurs.
     */
    private static BufferedImage getIconIfCorrect(String path) {
        if (path == null || path.equals("")) return null;

        BufferedImage img = null;

        try {
            //Checking if the file exists.
            File f = new File(path);
            if (!f.isFile())
                throw new FileNotFoundEx("The file " + path + " does not exist.");
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LauncherAppEx e) {
            e.report();
        } finally {
            return img;
        }
    }

    public String getName() {
        return name;
    }

    public String getArgs() {
        return args;
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public String getIconPath()  {
        if (imgFile != null)
            return imgFile.getAbsolutePath();
        return "";
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private void setIcon(BufferedImage icon) {
        if (icon == null) return;   //In order to dont destroy the before.
        this.icon = icon;
    }

    public void loadNewIcon(String path) {

        BufferedImage img = getIconIfCorrect(path);
        if (img == null) {
            System.out.println("Invalid new icon. Ignoring.");
            return;
        }
        imgFile = new File(path);
        setIcon(img);
    }

    public static Entry toEntry(App a) {
        return new Entry(a.getName(), a.getIconPath(), a.getFilePath(), a.getArgs(), a.isTerminal());
    }

    /**
     * Equals if name and target and args matches.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (!(o instanceof App))
            return false;
        App ao = (App)o;
        return  ao.getName().equals(this.name) &&
                ao.getFilePath().equals(this.getFilePath()) &&
                ao.getArgs().equals(this.getArgs()) &&
                ao.isTerminal() == this.isTerminal();
    }

    public String toString() {
        return "{\n\t" +
                "Name: " + name + ",\n\t" +
                "Icon: " + getIconPath() + ", \n\t" +
                "File: " + getFilePath() + ",\n\t" +
                "Args: " + getArgs() + "\n\t" +
                "term: " + isTerminal() + "\n}";
    }

    @Override
    public int compareTo(App o) {
        if (o.equals(this)) return 0;
        return 1;
    }
}
