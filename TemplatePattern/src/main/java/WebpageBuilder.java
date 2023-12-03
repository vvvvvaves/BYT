package main.java;

public interface WebpageBuilder {

     WebpageBuilder setTitle(String title);

     WebpageBuilder chooseTheme(Theme theme);

     WebpageBuilder chooseFont(Font font);

     WebpageBuilder createSections(String[] structure);

     WebpageBuilder fillSections(String[] filling);

     Webpage build();
}