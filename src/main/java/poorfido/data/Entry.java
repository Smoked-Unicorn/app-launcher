package poorfido.data;

/**
 * Created by jose on 13/08/16.
 *
 * This class is what the db will store as json object in order to make it persistent.
 *
 * The name is the string that will be displayable to the user.
 * The icon is the path to the file that contains the image that will be displayed next
 * to the name.
 * The path is the file that will be executed and the args the command line arguments that
 * will be parsed to the target file.
 *
 * Those data entries must be converted by the db to an App when it's loaded.
 */
public class Entry implements Comparable<Entry>{

    private String name;
    private String icon;
    private String path;
    private String args;
    private boolean terminal;

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Entry(final String name, final String icon, final String path, final String args, boolean inTerm) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        this.args = args;
        this.terminal = inTerm;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getPath() {
        return path;
    }

    public String getArgs() {
        return args;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setArgs(String args) {
        this.args = args;
    }


    public boolean equals(Object o) {
        if (!(o instanceof Entry))
            return false;
        Entry e = (Entry) o;
        return  e.getName().equals(this.getName()) &&
                e.getArgs().equals(this.getArgs()) &&
                e.getIcon().equals(this.getIcon()) &&
                e.getPath().equals(this.getPath()) &&
                e.isTerminal() == this.isTerminal();
    }

    public String toString() {
        return "Name: " + getName() + ". Icon: " + getIcon() + ". Target: " + getPath();
    }

    @Override
    public int compareTo(Entry o) {
        return o.equals(this) ? 0 : 1;
    }
}
