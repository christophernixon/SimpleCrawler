package org.example;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;

public class HtmlHelper {

    private Logger log = LoggerFactory.getLogger(HtmlHelper.class);
    private HttpConnection httpConnection;

    public HtmlHelper(final HttpConnection httpConnection) {
        this.httpConnection = httpConnection;
    }

    public Optional<Document> fetchPage(final String url, final RateLimiter throttler) {
        try {
            throttler.acquire();
            final Connection connection = this.httpConnection.url(url);
            final Document document = connection.get();
            if (isSuccessful(connection.response().statusCode())) {
                return Optional.of(document);
            }
            return Optional.empty();
        } catch (final Exception e) {
            log.warn("Error loading URL <{}>: {}", url, e.getMessage());
            return Optional.empty();
        }
    }

    public Set<String> getAllLinks(final Optional<Document> document) {
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

    private boolean isSuccessful(final int statusCode) {
        return (statusCode / 100 == 2);
    }
}
