package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;


public class SingleThreadedCrawler implements Crawler {

    private static final int MAX_URLS = 1000;
    private static Logger log = LoggerFactory.getLogger(SingleThreadedCrawler.class);
    private RateLimiter throttler = RateLimiter.create(1.0);
    private HtmlHelper htmlHelper;

    public SingleThreadedCrawler(final HtmlHelper htmlHelper) {
        this.htmlHelper = htmlHelper;
    }

    @Override
    public void crawl(final String url) {
        // Check for and process robots.txt file
        RobotsParser robotsParser = new RobotsParser(url);

        final List<String> visitedURLs = new ArrayList<>();
        final Deque<String> urlsToVisit = new ArrayDeque<>();
        int numURLsVisited = 0;
        String currentURL;
        Optional<Document> currentDocument;
        Set<String> currentLinks;

        urlsToVisit.push(url);
        while (urlsToVisit.size() > 0 && numURLsVisited < MAX_URLS) {
            currentURL = urlsToVisit.pop();
            currentDocument = htmlHelper.fetchPage(currentURL, throttler);
            currentLinks = htmlHelper.getAllLinks(currentDocument);
            visitPage(currentURL, currentLinks, visitedURLs);

            for (final String link : currentLinks) {
                if (canVisitPage(visitedURLs, urlsToVisit, url, link, robotsParser)) {
                    urlsToVisit.push(link);
                }
            }
            numURLsVisited++;
        }
        log.info("Stats: numURLsVisited: <{}>", numURLsVisited);
    }

    private void visitPage(final String url, final Set<String> links, final List<String> visitedURLs) {
        log.info("Visited URL: {} \nLinks on page: {}\n", url, links);
        visitedURLs.add(url);
    }

    /*
     * Only visit page if the following conditions are met:
     * 1. It's allowed by robots.txt file
     * 2. It hasn't already been visited
     * 3. It's not already in the list of URLs to visit
     * 4. It's within the same domain
     */
    private boolean canVisitPage(final List<String> visitedURLs,
                                 final Deque<String> urlsToVisit,
                                 final String startingURL,
                                 final String currentURL,
                                 final RobotsParser robotsParser) {
        if (robotsParser.canAccess(currentURL) &&
            !visitedURLs.contains(currentURL)
            && !urlsToVisit.contains(currentURL)
            && currentURL.startsWith(startingURL)) {
            return true;
        }
        return false;
    }
}
