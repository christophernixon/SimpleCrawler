package org.example;

public class App {

    public static void main(String[] args) {
        Crawler crawler = new SingleThreadedCrawler();
        crawler.crawl("https://bendunnegyms.com/");
    }
}
