package org.example;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadedCrawlerWorker implements Runnable {

    final ConcurrentLinkedQueue<String> visitedURLs;
    final Deque<String> urlsToVisit;
    final MultiThreadedCrawler crawler;
    final RobotsParser robotsParser;

    public MultiThreadedCrawlerWorker(final ConcurrentLinkedQueue<String> visitedURLs,
    final ConcurrentLinkedDeque<String> urlsToVisit,
    final MultiThreadedCrawler crawler,
    final RobotsParser robotsParser) {
        this.visitedURLs = visitedURLs;
        this.urlsToVisit = urlsToVisit;
        this.crawler = crawler;
        this.robotsParser = robotsParser;
    }

    @Override
    public void run() {
        // Attempt to poll URL from urlsToVisit
        // Check if URL has been visited before (if so return)
        // Mark URL as visited
        // Visit URL
        // Parse list of links from URL
        // Add all links to urlsToVisit
    }
    
}
