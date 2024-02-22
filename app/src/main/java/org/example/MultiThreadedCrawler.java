package org.example;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.helper.HttpConnection;

import com.google.common.util.concurrent.RateLimiter;

public class MultiThreadedCrawler implements Crawler {
    private final int MAX_URLS = 1000;
    private RateLimiter throttler = RateLimiter.create(1.0);
    private HtmlHelper htmlHelper;
    private RobotsParser robotsParser;
    protected int numURLsVisited;

    public MultiThreadedCrawler(final String startingURL) {
        this.htmlHelper = new HtmlHelper(new HttpConnection());
        this.robotsParser = new RobotsParser(startingURL);
    }

    @Override
    public void crawl(String url) {
        ExecutorService executor = Executors.newCachedThreadPool();
        final ConcurrentLinkedQueue<String> visitedURLs = new ConcurrentLinkedQueue<>();
        final ConcurrentLinkedDeque<String> urlsToVisit = new ConcurrentLinkedDeque<>();

        while (!urlsToVisit.isEmpty() && numURLsVisited < MAX_URLS) {
            executor.submit(new MultiThreadedCrawlerWorker(visitedURLs, urlsToVisit, this));
        }
    }

    protected synchronized void incrementNumURLsVisited() {
        numURLsVisited++;
    }
}
