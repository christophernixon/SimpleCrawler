package org.example;

import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.RateLimiter;

public class HtmlHelperTest {
    
    @Mock
    public HttpConnection httpConnection;

    public HtmlHelper classUnderTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        classUnderTest = new HtmlHelper(httpConnection);
    }

    @Test
    public void testFetchPage_success() {
        // Given
        final RateLimiter throttler = RateLimiter.create(1.0);
        final String testURL = "https://www.testURL.com";
        final Connection connection = httpConnection.url(testURL);
        final File file = new File("/testPage.hmtl");
        final Document document;
        try {
            document = Jsoup.parse(file, "UTF-8");

            // When
            when(httpConnection.url(testURL)).thenReturn(connection);
            when(connection.get()).thenReturn(document);
            when(connection.response().statusCode()).thenReturn(200);

        } catch (IOException e) {
		    fail("Error parsing test page.", e);
            return;
	    }

        // Then
        Optional<Document> returnValue = classUnderTest.fetchPage(testURL, throttler);
        Assert.assertTrue(returnValue.isPresent());
    }

    @Test
    public void testFetchPage_failure_notFound() {
        // Given
        final RateLimiter throttler = RateLimiter.create(1.0);
        final String testURL = "https://www.testURL.com";
        final Connection connection = httpConnection.url(testURL);
        final Document document = new Document("testURL");
        try {
            // When
            when(httpConnection.url(testURL)).thenReturn(connection);
            when(connection.get()).thenReturn(document);
            when(connection.response().statusCode()).thenReturn(404);

        } catch (IOException e) {
		    fail("Error parsing test page.", e);
            return;
	    }

        // Then
        Optional<Document> returnValue = classUnderTest.fetchPage(testURL, throttler);
        Assert.assertTrue(returnValue.isEmpty());
    }

}
