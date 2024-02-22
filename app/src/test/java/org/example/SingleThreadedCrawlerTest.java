package org.example;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SingleThreadedCrawlerTest {
    
    @Mock
    public HtmlHelper htmlHelper;

    public SingleThreadedCrawler classUnderTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        classUnderTest = new SingleThreadedCrawler(htmlHelper);
    }

    @Test
    public void testCrawl() {
        // Given
        Optional<Document> document = Optional.of(new Document("")); 
        Set<String> links = new HashSet<>();
        links.add("https://www.examplelink.com");
        links.add("https://www.examplelink.com/page");
        String initialURL = "testURL";

        // When
        when(htmlHelper.fetchPage(null, null)).thenReturn(document);
        when(htmlHelper.getAllLinks(document)).thenReturn(links);

        // Then
        classUnderTest.crawl(initialURL);
        verify(htmlHelper, times(3)).fetchPage(anyString(), any());
        verify(htmlHelper, times(3)).getAllLinks(any());
    }
}
