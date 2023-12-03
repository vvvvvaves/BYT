package main.java;

import java.util.Arrays;

public class Webpage {

    private String title;
    private Theme theme;
    private Font font;
    private String[] sections;
    private String[] fillings;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setSections(String[] sections) {
        this.sections = sections;
    }

    public void setFillings(String[] fillings) {
        if (this.sections != null) {
            this.fillings = fillings;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "Webpage{" +
                "\ntitle='" + title + '\n' +
                "theme=" + theme +
                "\nfont=" + font +
                "\nsections=" + Arrays.toString(sections) +
                "\nfillings=" + Arrays.toString(fillings) +
                '}';
    }
}
