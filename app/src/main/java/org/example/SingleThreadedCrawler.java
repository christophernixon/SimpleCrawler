package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.text.html.Option;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class SingleThreadedCrawler implements Crawler {

    public static final int MAX_URLS = 200;

    @Override
    public void crawl(final String url) {
        final List<String> visitedURLs = new ArrayList<>();
        final Deque<String> urlsToVisit = new ArrayDeque<>();
        int numURLsVisited = 0;
        String currentURL;
        Optional<Document> currentDocument;
        Set<String> currentLinks;

        urlsToVisit.push(url);
        while (urlsToVisit.size() > 0 && numURLsVisited < MAX_URLS) {
            currentURL = urlsToVisit.pop();

            if (!visitedURLs.contains(currentURL)) {
                currentDocument = fetchPage(currentURL);
                currentLinks = getAllLinks(currentDocument);
                visitPage(currentURL, currentLinks, visitedURLs);

                for (final String link : currentLinks) {
                    // Only add new link if it hasn't been visited and isn't already on stack and is a sub-page
                    if (!visitedURLs.contains(link) && !urlsToVisit.contains(link) && link.startsWith(url)) {
                        urlsToVisit.push(link);
                    }
                }
            }
            numURLsVisited++;
        }
    }

    private void visitPage(final String url, final Set<String> links, final List<String> visitedURLs) {
        System.out.println("Visited URL: " + url + "\nFound links: " + links + '\n');
        visitedURLs.add(url);
    }

    private Set<String> getAllLinks(final Optional<Document> document) {
        Set<String> links = new HashSet<String>();
        if (document.isEmpty()) {
            return links;
        }
        String rawURL;
        String cleanedURL;
        for (Element link : document.get().select("a[href]")) {
            rawURL = link.absUrl("href");
            cleanedURL = rawURL.split("#")[0];
            if (cleanedURL.endsWith("/")) {
                cleanedURL = cleanedURL.substring(0, cleanedURL.length() - 1);
            }
            links.add(cleanedURL);
        }
        return links;
    }

    private Optional<Document> fetchPage(final String url) {
        try {
            final Connection connection = Jsoup.connect(url);
            final Document document = connection.get();
            if (isSuccessful(connection.response().statusCode())) {
                return Optional.of(document);
            }
            return Optional.empty();
        } catch (final Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private boolean isSuccessful(final int statusCode) {
        return (statusCode / 100 == 2);
    }
}
