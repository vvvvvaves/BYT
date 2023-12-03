package main.test;

import main.java.ConcreteWebpageBuilder;
import main.java.Webpage;
import main.java.WebpageDirector;

public class BuilderTest {

    public static void main(String args[]) {
        ConcreteWebpageBuilder builder = new ConcreteWebpageBuilder();
        WebpageDirector director = new WebpageDirector();
        Webpage webpage = director.constructComicsWebpage(builder);
        System.out.println(webpage);
        System.out.println();
        webpage = director.constructTravelBlog(builder);
        System.out.println(webpage);
    }
}
