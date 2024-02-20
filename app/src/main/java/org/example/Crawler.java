package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Crawler {

    public static final int MAX_URLS = 200;

    List<String> visitedURLs;
    Deque<String> urlsToVisit;
    List<String> urlsPrinted;
    
    public Crawler() {
        visitedURLs = new ArrayList<>();
        urlsToVisit = new ArrayDeque<>();
        urlsPrinted = new ArrayList<>();
    }

    public void crawl(final String url) {
        urlsToVisit.push(url);
        String currentURL;
        int numURLsVisited = 0;
        while (urlsToVisit.size() > 0 && numURLsVisited < MAX_URLS) {
            currentURL = urlsToVisit.pop();
            if (!visitedURLs.contains(currentURL)) {
                Document document = request(currentURL);
                Set<String> links = getAllLinks(document);
                visitPage(currentURL, links);
                for (String link : links) {
                    // Only add new link if it hasn't been visited and isn't already on stack and is a sub-page
                    if (!visitedURLs.contains(link) && !urlsToVisit.contains(link) && link.startsWith(url)) {
                        urlsToVisit.push(link);
                    }
                }
            }
            numURLsVisited++;
        }
        System.out.println("STATS \nNumber of URLs visited: " + numURLsVisited);
    }

    private void visitPage(final String url, final Set<String> links) {
        // System.out.println("Visited URL: " + url);
        // System.out.println("Found links: " + links);
        visitedURLs.add(url);
    }

    private Set<String> getAllLinks(final Document document) {
        Set<String> links = new HashSet<String>();
        if (document == null) {
            return links;
        }
        for (Element link : document.select("a[href]")) {
            String linkURL = link.absUrl("href");
            String cleanURL = linkURL.split("#")[0];
            if (cleanURL.endsWith("/")) {
                cleanURL = cleanURL.substring(0, cleanURL.length() - 1);
            }
            links.add(cleanURL);
        }
        return links;
    }

    private Document request(final String url) {
        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            if (connection.response().statusCode() == 200) {
                return document;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
