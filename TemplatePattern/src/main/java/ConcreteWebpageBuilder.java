package main.java;

public class ConcreteWebpageBuilder implements WebpageBuilder {

    private Webpage webpage = new Webpage();

    @Override
    public WebpageBuilder setTitle(String title) {
        webpage.setTitle(title);
        return this;
    }

    @Override
    public WebpageBuilder chooseTheme(Theme theme) {
        webpage.setTheme(theme);
        return this;
    }

    @Override
    public WebpageBuilder chooseFont(Font font) {
        webpage.setFont(font);
        return this;
    }

    @Override
    public WebpageBuilder createSections(String[] structure) {
        webpage.setSections(structure);
        return this;
    }

    @Override
    public WebpageBuilder fillSections(String[] filling) {
        webpage.setFillings(filling);
        return this;
    }

    @Override
    public Webpage build() {
        return webpage;
    }
}
