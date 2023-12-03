package main.java;

public class WebpageDirector {
    public Webpage constructComicsWebpage(WebpageBuilder builder) {
        return builder.setTitle("Marvel comics library")
                .chooseFont(Font.COMIC_SANS)
                .chooseTheme(Theme.SPACE)
                .createSections(new String[] {"Navigation", "Popular", "News", "About"})
                .fillSections(new String[] {"Search bar", "Spider-Man, Iron Man, Avengers", "NEW MOVIE COMING SOON", "We are a community of comics lovers."})
                .build();
    }

    public Webpage constructTravelBlog(WebpageBuilder builder) {
        return builder.setTitle("Travel Blog")
                .chooseFont(Font.TIMES_NEW_ROMAN)
                .chooseTheme(Theme.NATURE)
                .createSections(new String[] {"Recent travels", "World map", "Gallery", "About"})
                .fillSections(new String[] {"Hawaii, 02.08.2023", "a google map with pins on it", "folds: Japan, Spain, Ukraine, Poland, Czech Republic", "This is my travel blog!"})
                .build();
    }
}
